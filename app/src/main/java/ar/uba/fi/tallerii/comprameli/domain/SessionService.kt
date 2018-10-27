package ar.uba.fi.tallerii.comprameli.domain

import io.reactivex.Completable
import io.reactivex.Observable


interface SessionService {

    /**
     * Check if there is a session already created
     *
     * @return true if the user is already signed, false otherwise
     */
    fun isSessionActive(): Observable<Boolean>

    /**
     * Logs the user by the usage of the credentials passed through param.
     * Session is created as part of the process
     * @throws RuntimeException
     * @throws ar.uba.fi.tallerii.comprameli.domain.exception.InvalidUserCredentialsException
     * if user name and password are not valid
     */
    fun logIn(userName: String, password: String): Completable

}