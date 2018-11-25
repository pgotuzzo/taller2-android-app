package ar.uba.fi.tallerii.comprameli.data.repository

import ar.uba.fi.tallerii.comprameli.data.products.*
import ar.uba.fi.tallerii.comprameli.data.profile.Profile
import ar.uba.fi.tallerii.comprameli.data.session.LogInBody
import ar.uba.fi.tallerii.comprameli.data.session.Token
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.http.*


interface AppServerRestApi {

    /**
     * USER
     */
    @POST("user/auth")
    fun logIn(@Body body: LogInBody): Single<Token>

    @GET("/user/profile")
    fun userProfile(): Single<Profile>

    @PUT("/user/profile")
    fun updateUserProfile(@Body profile: Profile): Completable

    /**
     * PRODUCTS
     */
    @GET("products")
    fun products(@Query("text") text: String?,
                 @Query("seller") seller: String?,
                 @Query("units") units: Int?,
                 @Query("min_price") priceMin: Float?,
                 @Query("max_price") priceMax: Float?,
                 @Query("x") x: Float?,
                 @Query("y") y: Float?,
                 @Query("categories") categories: List<String>,
                 @Query("payment_methods") paymentMethods: List<String>): Single<Products>

    @GET("products/{id}")
    fun productById(@Path("id") productId: String): Single<Product>

    @POST("products")
    fun newProduct(@Body product: ProductData): Completable

    @GET("/products/categories")
    fun categories(): Single<List<Category>>

    @POST("/products/{id}/questions")
    fun addQuestionToProduct(@Path("id") productId: String,
                             @Body questionData: QuestionData): Single<Product>

    @POST("/products/{id}/questions/{questionId}/answers")
    fun answer(@Path("id") productId: String,
               @Path("questionId") questionId: String,
               @Body answer: AnswerBody): Single<Product>

    /**
     * PAYMENTS
     */
    @GET("/payments")
    fun paymentMethods(): Single<List<PaymentMethod>>

    @POST("/user/register")
    fun registerUser(@Body body: Profile): Completable

}
