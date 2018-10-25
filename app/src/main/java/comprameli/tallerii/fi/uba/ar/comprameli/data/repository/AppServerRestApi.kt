package comprameli.tallerii.fi.uba.ar.comprameli.data.repository

import comprameli.tallerii.fi.uba.ar.comprameli.data.Ping
import io.reactivex.Observable
import retrofit2.http.GET


interface AppServerRestApi {

    @GET("ping")
    fun ping(): Observable<Ping>

}