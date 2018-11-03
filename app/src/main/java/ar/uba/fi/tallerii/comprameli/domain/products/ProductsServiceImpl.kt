package ar.uba.fi.tallerii.comprameli.domain.products

import ar.uba.fi.tallerii.comprameli.data.products.ProductsDao
import ar.uba.fi.tallerii.comprameli.model.ProductFilter
import io.reactivex.Single

class ProductsServiceImpl(private val mProductsDao: ProductsDao) : ProductsService {

    override fun getProducts(): Single<List<Product>> = getProducts(filter = ProductFilter())

    override fun getProductsByName(productName: String): Single<List<Product>> =
            getProducts(filter = ProductFilter(name = productName))


    override fun getProductsByFilter(filter: ProductFilter): Single<List<Product>> =
            getProducts(filter = filter)

    private fun getProducts(filter: ProductFilter): Single<List<Product>> =
            mProductsDao
                    .getProducts(filter = filter)
                    .map { list ->
                        list.map {
                            Product(it.id, it.name, it.description, it.images, it.price, it.ownerId)
                        }
                    }
}