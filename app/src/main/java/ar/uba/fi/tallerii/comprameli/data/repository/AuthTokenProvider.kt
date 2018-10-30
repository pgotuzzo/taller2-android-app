package ar.uba.fi.tallerii.comprameli.data.repository

import io.reactivex.Completable
import io.reactivex.Single

interface AuthTokenProvider {

    fun invalidateToken(): Completable

    fun setAuthToken(token: String): Completable

    fun existAuthToken(): Single<Boolean>

    fun getAuthToken(): Single<String>

}