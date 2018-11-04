package ar.uba.fi.tallerii.comprameli.presentation.search

import ar.uba.fi.tallerii.comprameli.domain.products.ProductsService
import ar.uba.fi.tallerii.comprameli.presentation.base.BasePresenter
import ar.uba.fi.tallerii.comprameli.presentation.search.SearchContract.Companion.PRODUCTS_FETCH
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class SearchPresenter(private val mProductsService: ProductsService) :
        BasePresenter<SearchContract.View>(), SearchContract.Presenter {

    private val mCompositeDisposable = CompositeDisposable()
    private var mIsListFiltered = false

    override fun onViewDetached() {
        super.onViewDetached()
        mCompositeDisposable.clear()
    }

    override fun onInit() {
        fetchFullProductList()
    }

    override fun onEmptySearch() {
        // Check if the full list was already fetched
        if (mIsListFiltered) {
            fetchFullProductList()
        }
    }

    override fun onSearchSubmit(query: String?) {
        var querySingle = mProductsService.getProducts()
        query?.apply { querySingle = mProductsService.getProductsByName(this) }
        val disposable = querySingle
                .map { products ->
                    products.map {
                        SearchContract.SearchItem(it.productId, it.images, it.title, it.price)
                    }
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            getView()?.apply {
                                mIsListFiltered = true
                                if (it.isEmpty())
                                    this.showEmptyListMessage()
                                else
                                    this.refreshList(it)
                            }
                        },
                        { getView()?.showError(PRODUCTS_FETCH) }
                )
        mCompositeDisposable.add(disposable)
    }

    override fun onItemClicked(productId: String) {
        getView()?.goProductDetails(productId)
    }

    private fun fetchFullProductList() {
        val disposable = mProductsService.getProducts()
                .map { products ->
                    products.map {
                        SearchContract.SearchItem(it.productId, it.images, it.title, it.price)
                    }
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            mIsListFiltered = false
                            getView()?.apply {
                                if (it.isEmpty())
                                    this.showEmptyListMessage()
                                else
                                    this.refreshList(it)
                            }
                        },
                        { getView()?.showError(PRODUCTS_FETCH) }
                )
        mCompositeDisposable.add(disposable)
    }

}