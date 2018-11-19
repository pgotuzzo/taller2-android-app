package ar.uba.fi.tallerii.comprameli.presentation.search

import ar.uba.fi.tallerii.comprameli.domain.products.ProductsService
import ar.uba.fi.tallerii.comprameli.domain.profile.ProfileService
import ar.uba.fi.tallerii.comprameli.model.ProductFilter
import ar.uba.fi.tallerii.comprameli.presentation.base.BasePresenter
import ar.uba.fi.tallerii.comprameli.presentation.search.SearchContract.Companion.PRODUCTS_FETCH
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class SearchPresenter(private val mProductsService: ProductsService,
                      private val mProfileService: ProfileService) :
        BasePresenter<SearchContract.View>(), SearchContract.Presenter {

    private val mDisposables = CompositeDisposable()
    private var mFilter: ProductFilter = ProductFilter()

    override fun onViewDetached() {
        super.onViewDetached()
        mDisposables.clear()
    }

    override fun onInit() {
        applyFilter(ProductFilter(), true)
    }

    override fun onInit(category: String) {
        applyFilter(ProductFilter(categories = listOf(category)), true)
    }

    override fun onInit(showOnlyOwnerProducts: Boolean) {
        if (showOnlyOwnerProducts) {
            applyFilter(ProductFilter(), true)
        } else {
            val disposable = mProfileService
                    .getProfile()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ applyFilter(ProductFilter(seller = it.userId), true) }, { processSearchError(it) })
            mDisposables.add(disposable)
        }
    }

    override fun onEmptySearch() {
        applyFilter(mFilter.copy(text = null), false)
    }

    override fun onSearchSubmit(query: String?) {
        applyFilter(mFilter.copy(text = query), false)
    }

    override fun onItemClicked(productId: String) {
        getView()?.goProductDetails(productId)
    }

    override fun onSearchSubmit(productFilter: ProductFilter) {
        applyFilter(productFilter, false)
    }

    override fun onFiltersClick() {
        getView()?.goFilters(mFilter)
    }

    private fun applyFilter(productFilter: ProductFilter, isInitializing: Boolean) {
        if (isInitializing || productFilter != mFilter) {
            // Only applies a filter different from the current one
            mFilter = productFilter
            val disposable = mProductsService
                    .getProductsByFilter(productFilter)
                    .map { products ->
                        products.map { SearchContract.SearchItem(it.productId!!, it.images, it.title, it.price) }
                    }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ processSearch(it) }, { processSearchError(it) })

            mDisposables.add(disposable)
        }
    }

    private fun processSearch(items: List<SearchContract.SearchItem>) {
        getView()?.apply {
            if (items.isEmpty())
                this.showEmptyListMessage()
            else
                this.refreshList(items)
        }
    }

    private fun processSearchError(throwable: Throwable) {
        Timber.e(throwable)
        getView()?.showError(PRODUCTS_FETCH)
    }

}