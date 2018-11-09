package ar.uba.fi.tallerii.comprameli.domain.products

import ar.uba.fi.tallerii.comprameli.data.products.ProductsDao
import ar.uba.fi.tallerii.comprameli.model.ProductFilter
import io.reactivex.Single

class ProductsServiceImpl(private val mProductsDao: ProductsDao) : ProductsService {

    override fun getProducts(): Single<List<Product>> = getProducts(filter = ProductFilter())

    override fun getProductsByName(productName: String): Single<List<Product>> =
            getProducts(filter = ProductFilter(text = productName))


    override fun getProductsByFilter(filter: ProductFilter): Single<List<Product>> =
            getProducts(filter = filter)

    private fun getProducts(filter: ProductFilter): Single<List<Product>> =
            mProductsDao
                    .getProducts(filter = filter)
                    .map { list ->
                        list.map {
                            Product(productId = it.id,
                                    title = it.name,
                                    description = it.description,
                                    images = it.images,
                                    price = it.price,
                                    seller = it.seller,
                                    units = it.units)
                        }
                    }

    override fun getProductById(productId: String): Single<Product> =
            mProductsDao
                    .getProductById(productId)
                    .map {
                        Product(productId = it.id,
                                title = it.name,
                                description = it.description,
                                images = it.images,
                                price = it.price,
                                seller = it.seller,
                                units = it.units)
                    }

}