package ar.uba.fi.tallerii.comprameli.data.products

import ar.uba.fi.tallerii.comprameli.data.repository.AppServerRestApi
import io.reactivex.Single

class ProductsDaoImpl(private val AppServerApi: AppServerRestApi) : ProductsDao {

    override fun getProducts(): Single<List<Product>> =
            AppServerApi.products().map { products -> products.productsList }

}