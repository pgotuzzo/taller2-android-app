package ar.uba.fi.tallerii.comprameli.domain.products

import ar.uba.fi.tallerii.comprameli.data.products.ProductsDao
import ar.uba.fi.tallerii.comprameli.model.ProductFilter
import io.reactivex.Single

class ProductsServiceImpl(private val mProductsDao: ProductsDao) : ProductsService {

    override fun getProductsByFilter(filter: ProductFilter): Single<List<Product>> =
            mProductsDao
                    .getProducts(filter = filter)
                    .map { list -> list.map { p -> productFrom(p) } }

    override fun getProductById(productId: String): Single<Product> =
            mProductsDao
                    .getProductById(productId)
                    .map { p -> productFrom(p) }

    override fun getCategories(): Single<List<Category>> =
            mProductsDao.getCategories().map { categoriesList ->
                categoriesList.map { category -> Category(name = category.name, image = category.image) }
            }

    override fun getPaymentMethods(): Single<List<PaymentMethod>> =
            mProductsDao.getPaymentMethods().map { methodsList ->
                methodsList.map { method -> PaymentMethod(name = method.name) }
            }

    private fun answerFrom(answer: ar.uba.fi.tallerii.comprameli.data.products.Answer?) =
            if (answer == null) null else Answer(id = answer.id, text = answer.text)


    private fun questionFrom(question: ar.uba.fi.tallerii.comprameli.data.products.Question) =
            Question(id = question.id, text = question.text, answer = answerFrom(question.answer))

    private fun productFrom(product: ar.uba.fi.tallerii.comprameli.data.products.Product) =
            Product(
                    productId = product.id,
                    title = product.name,
                    description = product.description,
                    images = product.images,
                    price = product.price,
                    seller = product.seller,
                    units = product.units,
                    question = product.questions.map { q -> questionFrom(q) }
            )


}