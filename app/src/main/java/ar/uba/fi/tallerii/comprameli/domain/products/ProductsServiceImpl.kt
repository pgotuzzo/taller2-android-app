package ar.uba.fi.tallerii.comprameli.domain.products

import ar.uba.fi.tallerii.comprameli.data.products.ProductsDao
import io.reactivex.Single

class ProductsServiceImpl(private val mProductsDao: ProductsDao) : ProductsService {

    override fun getProducts(): Single<List<Product>> =
            mProductsDao
                    .getProducts()
                    .map { list ->
                        list.map {
                            Product(it.id, it.name, it.description, it.images, it.price, it.ownerId)
                        }
                    }

}