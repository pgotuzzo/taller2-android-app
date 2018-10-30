package ar.uba.fi.tallerii.comprameli.data.repository

import ar.uba.fi.tallerii.comprameli.data.products.Products
import ar.uba.fi.tallerii.comprameli.data.session.LogInBody
import ar.uba.fi.tallerii.comprameli.data.session.Token
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST


interface AppServerRestApi {

    @POST("user/auth")
    fun logIn(@Body body: LogInBody): Single<Token>

    @GET("products")
    fun products(): Single<Products>

}