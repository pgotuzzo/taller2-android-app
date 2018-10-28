package ar.uba.fi.tallerii.comprameli.domain

import ar.uba.fi.tallerii.comprameli.data.session.Session
import ar.uba.fi.tallerii.comprameli.data.session.SessionDao
import io.reactivex.Completable
import io.reactivex.Observable


class SessionServiceImpl(private val mSessionDao: SessionDao) : SessionService {

    override fun isSessionActive(): Observable<Boolean> {
        return mSessionDao.getSession().map { true }.onErrorReturn { false }
    }

    override fun logIn(userName: String, password: String): Completable =
            mSessionDao.getAuthToken(userName, password)
                    .flatMapCompletable { token ->
                        mSessionDao.storeSession(Session(authToken = token, userName = userName))
                    }

    override fun logOut(): Completable {
        return mSessionDao.clearSession()
    }

}