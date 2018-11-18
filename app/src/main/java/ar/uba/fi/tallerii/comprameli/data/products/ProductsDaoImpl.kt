package ar.uba.fi.tallerii.comprameli.data.products

import ar.uba.fi.tallerii.comprameli.data.repository.AppServerRestApi
import ar.uba.fi.tallerii.comprameli.model.ProductFilter
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException
import io.reactivex.Single

class ProductsDaoImpl(private val mAppServerApi: AppServerRestApi) : ProductsDao {

    override fun getProducts(filter: ProductFilter): Single<List<Product>> =
            mAppServerApi
                    .products(
                            text = filter.text,
                            categories = filter.categories,
                            units = filter.units,
                            x = filter.x,
                            y = filter.y,
                            seller = filter.seller,
                            paymentMethods = filter.paymentMethods,
                            priceMin = filter.priceMin,
                            priceMax = filter.priceMax
                    )
                    .map { products -> products.productsList }
                    .onErrorReturn { error: Throwable ->
                        when (error) {
                            is HttpException ->
                                if (error.code() == 404) ArrayList() else throw error
                            else -> throw error
                        }
                    }

    override fun getProductById(productId: String): Single<Product> = mAppServerApi.productById(productId)

    override fun getCategories(): Single<List<Category>> = mAppServerApi.categories()

    override fun getPaymentMethods(): Single<List<PaymentMethod>> = mAppServerApi.paymentMethods()

}