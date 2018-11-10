package ar.uba.fi.tallerii.comprameli.domain.products

import ar.uba.fi.tallerii.comprameli.model.ProductFilter
import io.reactivex.Single

interface ProductsService {

    /**
     * Fetches all the available products, filtering them by customize filter
     *
     * @return list of products (could be empty)
     */
    fun getProductsByFilter(filter: ProductFilter): Single<List<Product>>

    fun getProductById(productId: String): Single<Product>

    fun getCategories(): Single<List<Category>>

    fun getPaymentMethods(): Single<List<PaymentMethod>>

}

