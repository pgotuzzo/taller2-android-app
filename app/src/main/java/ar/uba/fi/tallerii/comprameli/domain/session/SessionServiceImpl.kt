package ar.uba.fi.tallerii.comprameli.domain.session

import ar.uba.fi.tallerii.comprameli.data.repository.AuthTokenProvider
import ar.uba.fi.tallerii.comprameli.data.session.Session
import ar.uba.fi.tallerii.comprameli.data.session.SessionDao
import io.reactivex.Completable
import io.reactivex.Single


class SessionServiceImpl(private val mSessionDao: SessionDao,
                         private val mAuthTokenProvider: AuthTokenProvider) : SessionService {

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
                        ))
                    }

    override fun logOut(): Completable =
            Completable.merge(listOf(
                    // Clear session data
                    mSessionDao.clearSession(),
                    // Invalidate token provider
                    mAuthTokenProvider.invalidateToken()
            ))

}