package ar.uba.fi.tallerii.comprameli.data.products

import ar.uba.fi.tallerii.comprameli.data.repository.AppServerRestApi
import ar.uba.fi.tallerii.comprameli.model.ProductFilter
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class ProductsDaoImpl(private val mAppServerApi: AppServerRestApi) : ProductsDao {

    override fun getProducts(filter: ProductFilter): Single<List<Product>> =
            mAppServerApi
                    .products(
                            text = filter.text,
                            categories = filter.categories,
                            units = filter.units,
                            longitude = filter.longitude,
                            latitude = filter.latitude,
                            maxDistance = filter.maxDistance,
                            seller = filter.seller,
                            paymentMethods = filter.paymentMethods,
                            priceMin = filter.priceMin,
                            priceMax = filter.priceMax
                    )
                    .map { products -> products.productsList }
                    .onErrorReturn { error: Throwable ->
                        when (error) {
                            is HttpException ->
                                if (error.code() == 404) ArrayList() else throw error
                            else -> throw error
                        }
                    }.subscribeOn(Schedulers.io())

    override fun getProductById(productId: String): Single<Product> =
            mAppServerApi.productById(productId).subscribeOn(Schedulers.io())

    override fun getCategories(): Single<List<Category>> =
            mAppServerApi.categories().subscribeOn(Schedulers.io())

    override fun getPaymentMethods(): Single<List<PaymentMethod>> =
            mAppServerApi.paymentMethods().subscribeOn(Schedulers.io())

    override fun createProduct(productData: ProductData): Completable =
            mAppServerApi.newProduct(productData).subscribeOn(Schedulers.io())

    override fun addQuestionToProduct(productId: String, question: String): Single<Product> =
            mAppServerApi
                    .addQuestionToProduct(productId, QuestionData(text = question))
                    .subscribeOn(Schedulers.io())

    override fun answerQuestion(productId: String,
                                questionId: String,
                                answer: String): Single<Product> =
            mAppServerApi
                    .answer(productId, questionId, AnswerBody(answer))
                    .subscribeOn(Schedulers.io())


}