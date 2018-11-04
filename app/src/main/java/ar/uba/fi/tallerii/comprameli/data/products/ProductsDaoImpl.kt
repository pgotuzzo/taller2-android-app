package ar.uba.fi.tallerii.comprameli.data.products

import ar.uba.fi.tallerii.comprameli.data.repository.AppServerRestApi
import ar.uba.fi.tallerii.comprameli.model.ProductFilter
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException
import io.reactivex.Single

class ProductsDaoImpl(private val AppServerApi: AppServerRestApi) : ProductsDao {

    override fun getProducts(filter: ProductFilter): Single<List<Product>> =
            AppServerApi
                    .products(
                            name = filter.name,
                            description = filter.description,
                            categories = filter.categories,
                            units = filter.units,
                            x = filter.x,
                            y = filter.y,
                            seller = filter.seller,
                            paymentMethods = filter.paymentMethods,
                            price = filter.price
                    )
                    .map { products -> products.productsList }
                    .onErrorReturn { error: Throwable ->
                        when (error) {
                            is HttpException ->
                                if (error.code() == 404) ArrayList() else throw error
                            else -> throw error
                        }
                    }

    override fun getProductById(productId: String): Single<Product> = AppServerApi.productById(productId)

}