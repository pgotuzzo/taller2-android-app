package ar.uba.fi.tallerii.comprameli.data.session

import ar.uba.fi.tallerii.comprameli.data.session.exception.NonexistentSessionException
import com.google.firebase.auth.AuthCredential
import io.reactivex.Completable
import io.reactivex.Single

interface SessionDao {

    fun getAuthToken(userName: String, password: String): Single<String>

    fun getAuthToken(credentials: FirebaseCredentials): Single<String>

    fun storeSession(session: Session): Completable

    /**
     * @see storeSession
     *
     * @return previously stored session
     * @exception NonexistentSessionException if there no session
     */
    fun getSession(): Single<Session>

    fun clearSession(): Completable

    fun getFirebaseTokenFromFacebookToken(credential: AuthCredential): Single<FirebaseCredentials>

}