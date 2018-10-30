package ar.uba.fi.tallerii.comprameli.data.session

import ar.uba.fi.tallerii.comprameli.data.session.exception.NonexistentSessionException
import io.reactivex.Completable
import io.reactivex.Single

interface SessionDao {

    fun getAuthToken(userName: String, password: String): Single<String>

    fun storeSession(session: Session): Completable

    /**
     * @see storeSession
     *
     * @return previously stored session
     * @exception NonexistentSessionException if there no session
     */
    fun getSession(): Single<Session>

    fun clearSession(): Completable

}