package ar.uba.fi.tallerii.comprameli.data.repository

import ar.uba.fi.tallerii.comprameli.data.products.Product
import ar.uba.fi.tallerii.comprameli.data.products.Products
import ar.uba.fi.tallerii.comprameli.data.profile.Profile
import ar.uba.fi.tallerii.comprameli.data.session.LogInBody
import ar.uba.fi.tallerii.comprameli.data.session.Token
import io.reactivex.Single
import retrofit2.http.*


interface AppServerRestApi {

    @POST("user/auth")
    fun logIn(@Body body: LogInBody): Single<Token>

    @GET("products")
    fun products(@Query("name") name: String?,
                 @Query("description") description: String?,
                 @Query("seller") seller: String?,
                 @Query("units") units: Int?,
                 @Query("price") price: Float?,
                 @Query("x") x: Float?,
                 @Query("y") y: Float?,
                 @Query("categories") categories: String?,
                 @Query("payment_methods") paymentMethods: String?): Single<Products>

    @GET("products/{id}")
    fun productById(@Path("id") productId: String): Single<Product>

    @GET("/user/profile")
    fun userProfile(): Single<Profile>

}
