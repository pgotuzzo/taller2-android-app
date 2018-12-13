package ar.uba.fi.tallerii.comprameli.domain.products

import ar.uba.fi.tallerii.comprameli.data.Location
import ar.uba.fi.tallerii.comprameli.data.files.FilesDao
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
                methodsList.map { method -> paymentMethodFrom(method) }
            }

    override fun createProduct(productData: ProductData): Completable {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.US)
        val timeStamp = dateFormat.format(Calendar.getInstance().time)
        val imagesUrls: MutableList<String> = MutableList(0) { "" }
        var count = -1

        // Fetch User Id
        return mProfileDao.getProfile().map { profile -> profile.id }.flatMapObservable { userId ->
            // Upload images
            productData.images.toObservable().flatMapSingle { imageUri ->
                count++
                mFilesDao.uploadFile(imageUri, "images/$userId/products/$timeStamp/$count.png")
            }.map { url ->
                // Update image list
                imagesUrls.add(url)
            }
        }.flatMapCompletable { Completable.complete() }
                // Modify product to make use of urls
                .andThen {
                    val updatedProduct = productData.copy(images = imagesUrls)
                    val disposable = mProductsDao
                            .createProduct(productDataTo(updatedProduct))
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

    private fun paymentMethodFrom(paymentMethod: ar.uba.fi.tallerii.comprameli.data.products.PaymentMethod) =
            PaymentMethod(name = paymentMethod.name, image = paymentMethod.image, type = paymentMethod.type)

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
                    longitude = product.location.longitude.toFloat(),
                    latitude = product.location.latitude.toFloat(),
                    categories = product.categories,
                    paymentMethods = product.paymentMethods.map { p -> paymentMethodFrom(p) },
                    questions = product.questions.map { q -> questionFrom(q) }
            )

    private fun productDataTo(productData: ProductData) =
            ar.uba.fi.tallerii.comprameli.data.products.ProductData(
                    name = productData.title,
                    description = productData.description,
                    images = productData.images,
                    price = productData.price,
                    units = productData.units,
                    categories = productData.categories,
                    paymentMethods = productData.paymentMethods,
                    location = Location(longitude = productData.longitude, latitude = productData.latitude)
            )

}