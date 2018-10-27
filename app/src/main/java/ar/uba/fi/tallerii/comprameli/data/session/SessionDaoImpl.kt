package ar.uba.fi.tallerii.comprameli.data.session

import ar.uba.fi.tallerii.comprameli.data.repository.AppServerRestApi
import ar.uba.fi.tallerii.comprameli.data.repository.PreferencesMap
import ar.uba.fi.tallerii.comprameli.data.repository.PreferencesMap.Companion.SESSION
import ar.uba.fi.tallerii.comprameli.data.session.exception.InvalidUserCredentialsException
import ar.uba.fi.tallerii.comprameli.data.session.exception.NonexistentSessionException
import com.google.firebase.auth.FirebaseAuth
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.rxkotlin.Observables
import io.reactivex.schedulers.Schedulers


class SessionDaoImpl(private val mAppServerApi: AppServerRestApi,
                     private val mPreferencesMap: PreferencesMap) : SessionDao {

    companion object {
        const val KEY_SESSION_AUTH_TOKEN = "auth_token"
        const val KEY_SESSION_USER_NAME = "user_name"
    }

    override fun getAuthToken(userName: String, password: String): Observable<String> =
    // Fetch FireBase Token
            getFireBaseAuthToken(userName, password).observeOn(Schedulers.io())
                    // Fetch App Server Token
                    .flatMap { fireBaseToken -> mAppServerApi.logIn(LogInBody(fireBaseToken)) }
                    .map { token: Token -> token.token }

    override fun getSession(): Observable<Session> =
            Observables.zip(
                    mPreferencesMap.getString(SESSION, KEY_SESSION_AUTH_TOKEN).onErrorReturn { "" },
                    mPreferencesMap.getString(SESSION, KEY_SESSION_USER_NAME).onErrorReturn { "" }
            ) { authToken: String, userName: String ->
                if (authToken.isNotEmpty() && userName.isNotEmpty())
                    Session(authToken, userName)
                else
                    throw NonexistentSessionException()
            }

    override fun storeSession(session: Session): Completable =
            Completable.merge(listOf(
                    mPreferencesMap.store(SESSION, KEY_SESSION_AUTH_TOKEN, session.authToken),
                    mPreferencesMap.store(SESSION, KEY_SESSION_USER_NAME, session.userName)
            ))

    private fun getFireBaseAuthToken(userName: String, password: String): Observable<String> =
            Observable.create {
                FirebaseAuth
                        .getInstance()
                        .signInWithEmailAndPassword(userName, password)
                        .addOnCompleteListener { task ->
                            run {
                                if (task.isSuccessful) {
                                    // Success
                                    task.result.user.getIdToken(true).addOnCompleteListener { idTokenResult ->
                                        it.onNext(idTokenResult.result.token!!)
                                        it.onComplete()
                                    }
                                } else {
                                    // Failure
                                    it.onError(InvalidUserCredentialsException())
                                }
                            }
                        }
            }

}
