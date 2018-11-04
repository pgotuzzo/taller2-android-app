package ar.uba.fi.tallerii.comprameli.presentation.search

import ar.uba.fi.tallerii.comprameli.domain.products.ProductsService
import ar.uba.fi.tallerii.comprameli.domain.profile.ProfileService
import ar.uba.fi.tallerii.comprameli.model.ProductFilter
import ar.uba.fi.tallerii.comprameli.presentation.base.BasePresenter
import ar.uba.fi.tallerii.comprameli.presentation.search.SearchContract.Companion.PRODUCTS_FETCH
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class SearchPresenter(private val mProductsService: ProductsService,
                      private val mProfileService: ProfileService) :
        BasePresenter<SearchContract.View>(), SearchContract.Presenter {

    private val mCompositeDisposable = CompositeDisposable()
    private var mIsListFiltered = false

    override fun onViewDetached() {
        super.onViewDetached()
        mCompositeDisposable.clear()
    }

    override fun onInit() {
        val disposable = fetchFullProductList()
                .subscribe({ processSearch(it, false) }, { processSearchError() })
        mCompositeDisposable.add(disposable)
    }

    override fun onInit(category: String) {
        val disposable = fetchFilteredProductList(ProductFilter(categories = category))
                .subscribe({ processSearch(it, true) }, { processSearchError() })
        mCompositeDisposable.add(disposable)
    }

    override fun onInit(showOnlyOwnerProducts: Boolean) {
        val singleSearch =
                if (!showOnlyOwnerProducts) {
                    fetchFullProductList()
                } else {
                    mProfileService
                            .getProfile()
                            .flatMap {
                                fetchFilteredProductList(ProductFilter(seller = it.sellerId))
                            }.subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                }
        val disposable = singleSearch.subscribe(
                { processSearch(it, showOnlyOwnerProducts) },
                { throwable -> processSearchError() }
        )
        mCompositeDisposable.add(disposable)
    }

    override fun onEmptySearch() {
        // Check if the full list was already fetched
        if (mIsListFiltered) {
            fetchFullProductList()
        }
    }

    override fun onSearchSubmit(query: String?) {
        val isFiltered = !query.isNullOrEmpty()
        val querySingle = if (!isFiltered) {
            fetchFullProductList()
        } else {
            mProductsService.getProductsByName(query!!)
                    .map { products -> products.map { SearchContract.SearchItem(it.productId, it.images, it.title, it.price) } }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
        }
        val disposable = querySingle.subscribe({ processSearch(it, isFiltered) }, { processSearchError() })
        mCompositeDisposable.add(disposable)
    }

    override fun onItemClicked(productId: String) {
        getView()?.goProductDetails(productId)
    }

    private fun fetchFullProductList(): Single<List<SearchContract.SearchItem>> =
            mProductsService.getProducts()
                    .map { products ->
                        products.map {
                            SearchContract.SearchItem(it.productId, it.images, it.title, it.price)
                        }
                    }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())

    private fun fetchFilteredProductList(filter: ProductFilter): Single<List<SearchContract.SearchItem>> =
            mProductsService.getProductsByFilter(filter)
                    .map { products ->
                        products.map { SearchContract.SearchItem(it.productId, it.images, it.title, it.price) }
                    }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())

    private fun processSearch(items: List<SearchContract.SearchItem>, isFiltered: Boolean) {
        getView()?.apply {
            mIsListFiltered = isFiltered
            if (items.isEmpty())
                this.showEmptyListMessage()
            else
                this.refreshList(items)
        }
    }

    private fun processSearchError() {
        getView()?.showError(PRODUCTS_FETCH)
    }

}