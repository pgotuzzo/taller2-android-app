package comprameli.tallerii.fi.uba.ar.comprameli.domain

import comprameli.tallerii.fi.uba.ar.comprameli.data.SessionDao
import io.reactivex.Observable


class SessionServiceImpl(private val mSessionDao: SessionDao) : SessionService {

    override fun test(): Observable<Boolean> = mSessionDao.test().map { true }

}