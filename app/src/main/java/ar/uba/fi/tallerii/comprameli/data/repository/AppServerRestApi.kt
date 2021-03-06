package ar.uba.fi.tallerii.comprameli.data.repository

import ar.uba.fi.tallerii.comprameli.data.orders.*
import ar.uba.fi.tallerii.comprameli.data.products.*
import ar.uba.fi.tallerii.comprameli.data.profile.Profile
import ar.uba.fi.tallerii.comprameli.data.profile.ProfileChanges
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

    @POST("/user/register")
    fun registerUser(@Body profile: Profile): Completable

    @GET("/user/profile")
    fun userProfile(): Single<Profile>

    @PUT("/user/profile")
    fun updateUserProfile(@Body profileChanges: ProfileChanges): Completable

    /**
     * PRODUCTS
     */
    @GET("products")
    fun products(@Query("text") text: String?,
                 @Query("seller") seller: String?,
                 @Query("units") units: Int?,
                 @Query("min_price") priceMin: Float?,
                 @Query("max_price") priceMax: Float?,
                 @Query("longitude") longitude: Float?,
                 @Query("latitude") latitude: Float?,
                 @Query("max_distance") maxDistance: Float?,
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

    /**
     * ORDERS
     */
    @POST("/orders")
    fun newOrder(@Body order: OrderData): Single<OrderTracking>

    @GET("/orders/sales")
    fun sales(): Single<Orders>

    @GET("/orders/purchases")
    fun purchases(): Single<Orders>

    @POST("/orders/shipping/estimate")
    fun estimateDelivery(@Body orderToEstimate: EstimateData): Single<DeliveryEstimation>

    @POST("/orders/ratings/{tracking_number}")
    fun rateSeller(@Path("tracking_number") trackingNumber: Int, @Body rate: Rate): Completable

    @GET("/orders/tracking/{tracking_number}")
    fun trackOrder(@Path("tracking_number") trackingNumber: Int): Single<OrderTrackingDetails>

}
