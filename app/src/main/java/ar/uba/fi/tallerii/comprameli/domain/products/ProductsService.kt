package ar.uba.fi.tallerii.comprameli.domain.products

import ar.uba.fi.tallerii.comprameli.model.ProductFilter
import io.reactivex.Single

interface ProductsService {

    /**
     * Fetches all the available products
     *
     * @return list of products (could be empty)
     */
    fun getProducts(): Single<List<Product>>

    /**
     * Fetches all the available products, filtering them by product name
     *
     * @return list of products (could be empty)
     */
    fun getProductsByName(productName: String): Single<List<Product>>

    /**
     * Fetches all the available products, filtering them by customize filter
     *
     * @return list of products (could be empty)
     */
    fun getProductsByFilter(filter: ProductFilter): Single<List<Product>>

}

