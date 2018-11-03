package ar.uba.fi.tallerii.comprameli.data.repository

import ar.uba.fi.tallerii.comprameli.data.products.Products
import ar.uba.fi.tallerii.comprameli.data.session.LogInBody
import ar.uba.fi.tallerii.comprameli.data.session.Token
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query


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

}
