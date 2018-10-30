package ar.uba.fi.tallerii.comprameli.domain

import io.reactivex.Completable
import io.reactivex.Single


interface SessionService {

    /**
     * Check if there is a session already created
     *
     * @return true if the user is already signed, false otherwise
     */
    fun isSessionActive(): Single<Boolean>

    /**
     * Tries to log the user by the usage of the credentials passed through param.
     * Session is created as part of the process
     */
    fun logIn(userName: String, password: String): Completable

    /**
     * Logs out the user. Session is deleted as part of the process
     */
    fun logOut(): Completable

}