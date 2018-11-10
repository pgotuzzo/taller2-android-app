package ar.uba.fi.tallerii.comprameli.domain.products

import ar.uba.fi.tallerii.comprameli.data.products.ProductsDao
import ar.uba.fi.tallerii.comprameli.model.ProductFilter
import io.reactivex.Single

class ProductsServiceImpl(private val mProductsDao: ProductsDao) : ProductsService {

    override fun getProductsByFilter(filter: ProductFilter): Single<List<Product>> =
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

    override fun getCategories(): Single<List<Category>> =
            mProductsDao.getCategories().map { categoriesList ->
                categoriesList.map { category -> Category(name = category.name, image = category.image) }
            }

    override fun getPaymentMethods(): Single<List<PaymentMethod>> =
            mProductsDao.getPaymentMethods().map { methodsList ->
                methodsList.map { method -> PaymentMethod(name = method.name) }
            }

}