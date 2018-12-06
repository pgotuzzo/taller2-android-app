package ar.uba.fi.tallerii.comprameli.presentation.map

import ar.uba.fi.tallerii.comprameli.domain.products.Product
import ar.uba.fi.tallerii.comprameli.domain.products.ProductsService
import ar.uba.fi.tallerii.comprameli.model.ProductFilter
import ar.uba.fi.tallerii.comprameli.presentation.base.BasePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable

class MapPresenter(private val mProductsService: ProductsService) :
        BasePresenter<MapContract.View>(), MapContract.Presenter {

    private val mDisposables = CompositeDisposable()
    private var mProductsFound: MutableList<Product> = ArrayList()

    override fun onViewDetached() {
        mDisposables.clear()
        super.onViewDetached()
    }

    override fun onMapIdle(longitude: Float, latitude: Float, radius: Float) {
        val filter = ProductFilter(longitude = longitude, latitude = latitude, maxDistance = radius)
        val disposable = mProductsService.getProductsByFilter(filter)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { products ->
                    val newProducts = products.filter { !mProductsFound.contains(it) }
                    getView()?.addMarkers(
                            newProducts.map {
                                MapContract.Marker(
                                        longitude = it.longitude,
                                        latitude = it.latitude,
                                        productId = it.productId,
                                        productImage = if (it.images.isEmpty()) null else it.images[0],
                                        productName = it.title
                                )
                            }
                    )
                    mProductsFound.addAll(newProducts)

                }
        mDisposables.add(disposable)
    }

}