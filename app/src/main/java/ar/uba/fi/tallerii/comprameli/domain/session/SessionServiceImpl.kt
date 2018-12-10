package ar.uba.fi.tallerii.comprameli.domain.session

import ar.uba.fi.tallerii.comprameli.data.db.DatabaseDao
import ar.uba.fi.tallerii.comprameli.data.profile.ProfileDao
import ar.uba.fi.tallerii.comprameli.data.repository.AuthTokenProvider
import ar.uba.fi.tallerii.comprameli.data.session.FirebaseCredentials
import ar.uba.fi.tallerii.comprameli.data.session.Session
import ar.uba.fi.tallerii.comprameli.data.session.SessionDao
import com.google.firebase.auth.AuthCredential
import com.google.firebase.iid.FirebaseInstanceId
import io.reactivex.Completable
import io.reactivex.Single


class SessionServiceImpl(private val mSessionDao: SessionDao,
                         private val mAuthTokenProvider: AuthTokenProvider,
                         private val mDatabaseDao: DatabaseDao,
                         private val mProfileDao: ProfileDao) : SessionService {

    override fun isSessionActive(): Single<Boolean> {
        return mSessionDao.getSession().map { true }.onErrorReturn { false }
    }


    override fun logIn(userName: String, password: String): Completable =
            mSessionDao.getAuthToken(userName, password)
                    .flatMapCompletable { token ->
                        Completable.merge(listOf(
                                // Store session data
                                mSessionDao.storeSession(Session(authToken = token, userName = userName)),
                                // Set up token provider
                                mAuthTokenProvider.setAuthToken(token)
                        )).andThen(
                                // Active messaging token
                                getMessagingToken().flatMapCompletable { messageToken ->
                                    mProfileDao.getProfile().flatMapCompletable { profile ->
                                        mDatabaseDao.setMessagingToken(profile.id, messageToken)
                                    }
                                }
                        )
                    }

    override fun logIn(credentials: FirebaseCredentials): Completable =
            mSessionDao.getAuthToken(credentials)
                    .flatMapCompletable { token ->
                        Completable.merge(listOf(
                                // Store session data
                                mSessionDao.storeSession(Session(authToken = token, userName = credentials.userName)),
                                // Set up token provider
                                mAuthTokenProvider.setAuthToken(token)
                        )).andThen(
                                // Active messaging token
                                getMessagingToken().flatMapCompletable { messageToken ->
                                    mProfileDao.getProfile().flatMapCompletable { profile ->
                                        mDatabaseDao.setMessagingToken(profile.id, messageToken)
                                    }
                                }
                        )
                    }

    override fun logOut(): Completable =
    // Clear messaging token
            mProfileDao.getProfile().flatMapCompletable { mDatabaseDao.removeMessagingToken(it.id) }.andThen(
                    Completable.merge(listOf(
                            // Clear session data
                            mSessionDao.clearSession(),
                            // Invalidate token provider
                            mAuthTokenProvider.invalidateToken()
                    ))
            )

    override fun logInWithFacebookToken(credential: AuthCredential): Single<FirebaseCredentials> =
            mSessionDao.getFirebaseTokenFromFacebookToken(credential)


    override fun refreshMessagingToken(token: String): Completable =
            isSessionActive().flatMapCompletable { isSessionActive ->
                if (isSessionActive) {
                    // Session Active - Assign token to an existing and active account
                    mProfileDao.getProfile().map { it.id }.flatMapCompletable {
                        mDatabaseDao.setMessagingToken(it, token)
                    }
                } else {
                    // Session Not Active - No op
                    Completable.complete()
                }
            }

    private fun getMessagingToken(): Single<String> = Single.create { emitter ->
        FirebaseInstanceId.getInstance().instanceId
                .addOnSuccessListener { emitter.onSuccess(it.token) }
                .addOnFailureListener { emitter.onError(it) }
    }

}