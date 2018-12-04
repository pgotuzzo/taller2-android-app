package ar.uba.fi.tallerii.comprameli.presentation.dashboard.home

import ar.uba.fi.tallerii.comprameli.domain.products.ProductsService
import ar.uba.fi.tallerii.comprameli.presentation.base.BasePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber

class HomePresenter(private val mProductsService: ProductsService) :
        BasePresenter<HomeContract.View>(), HomeContract.Presenter {

    private val mDisposables = CompositeDisposable()

    override fun onViewDetached() {
        mDisposables.clear()
        super.onViewDetached()
    }

    override fun onInit() {
        val disposable = mProductsService.getCategories()
                .map { categories ->
                    categories.filter { it.image != null }.map { fromCategory(it.name, it.image!!) }
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { categories -> getView()?.refreshCarousel(categories) },
                        { throwable -> Timber.e(throwable) }
                )

        mDisposables.add(disposable)
    }

    private fun fromCategory(name: String, image: String): HomeContract.Category =
            HomeContract.Category(name = name, image = image)


}
