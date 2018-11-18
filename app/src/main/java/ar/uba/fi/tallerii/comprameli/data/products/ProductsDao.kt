package ar.uba.fi.tallerii.comprameli.data.products

import ar.uba.fi.tallerii.comprameli.model.ProductFilter
import io.reactivex.Single

interface ProductsDao {

    fun getProducts(filter: ProductFilter): Single<List<Product>>

    fun getProductById(productId: String): Single<Product>

    fun getCategories(): Single<List<Category>>

    fun getPaymentMethods(): Single<List<PaymentMethod>>

}