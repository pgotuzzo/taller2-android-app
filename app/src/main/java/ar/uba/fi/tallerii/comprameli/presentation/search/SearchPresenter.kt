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

    override fun onViewDetached() {
        super.onViewDetached()
        mCompositeDisposable.clear()
    }

    override fun onInit() {
        val disposable =
                mProductsService
                        .getProducts()
                        .map { products ->
                            products.map {
                                SearchContract.SearchItem(it.productId, it.images, it.title, it.price)
                            }
                        }
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                { getView()?.refreshList(it) },
                                { getView()?.showError(PRODUCTS_FETCH) }
                        )
        mCompositeDisposable.add(disposable)
    }

}