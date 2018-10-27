package ar.uba.fi.tallerii.comprameli.data.repository

import ar.uba.fi.tallerii.comprameli.data.session.LogInBody
import ar.uba.fi.tallerii.comprameli.data.session.Token
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.POST


interface AppServerRestApi {

    @POST("user/auth")
    fun logIn(@Body body: LogInBody): Observable<Token>

}