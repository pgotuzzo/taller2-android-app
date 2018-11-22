package ar.uba.fi.tallerii.comprameli.domain.products

import ar.uba.fi.tallerii.comprameli.data.files.FilesDao
import ar.uba.fi.tallerii.comprameli.data.products.Location
import ar.uba.fi.tallerii.comprameli.data.products.ProductsDao
import ar.uba.fi.tallerii.comprameli.data.profile.ProfileDao
import ar.uba.fi.tallerii.comprameli.model.ProductFilter
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.rxkotlin.toObservable
import java.text.SimpleDateFormat
import java.util.*

class ProductsServiceImpl(private val mProductsDao: ProductsDao,
                          private val mProfileDao: ProfileDao,
                          private val mFilesDao: FilesDao) : ProductsService {

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

    override fun createProduct(product: Product): Completable {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.US)
        val timeStamp = dateFormat.format(Calendar.getInstance().time)
        val imagesUrls: MutableList<String> = MutableList(0) { "" }
        var count = -1

        // Fetch User Id
        return mProfileDao.getProfile().map { profile -> profile.id }.flatMapObservable { userId ->
            // Upload images
            product.images.toObservable().flatMapSingle { imageUri ->
                count++
                mFilesDao.uploadFile(imageUri, "images/$userId/products/$timeStamp/$count.png")
            }.map { url ->
                // Update image list
                imagesUrls.add(url)
            }
        }.flatMapCompletable { Completable.complete() }
                // Modify product to make use of urls
                .andThen {
                    val updatedProduct = product.copy(images = imagesUrls)
                    val disposable = mProductsDao
                            .createProduct(productTo(updatedProduct))
                            .doOnComplete { it.onComplete() }
                            .doOnError { e -> it.onError(e) }
                            .subscribe()
                    it.onSubscribe(disposable)
                }
    }

    override fun addQuestionToProduct(productId: String, question: String): Single<Product> =
            mProductsDao.addQuestionToProduct(productId, question).map { p -> productFrom(p) }

    override fun answerQuestion(productId: String,
                                questionId: String,
                                answer: String): Single<Product> =
            mProductsDao.answerQuestion(productId, questionId, answer).map { p -> productFrom(p) }

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
                    categories = product.categories,
                    paymentMethods = product.paymentMethods,
                    questions = product.questions.map { q -> questionFrom(q) }
            )

    private fun productTo(product: Product) =
            ar.uba.fi.tallerii.comprameli.data.products.Product(
                    id = product.productId,
                    name = product.title,
                    description = product.description,
                    images = product.images,
                    price = product.price,
                    units = product.units,
                    categories = product.categories,
                    paymentMethods = product.paymentMethods,
                    location = Location(0.0, 0.0)
            )

}