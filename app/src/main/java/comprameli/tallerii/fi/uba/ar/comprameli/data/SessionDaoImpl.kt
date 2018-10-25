package comprameli.tallerii.fi.uba.ar.comprameli.data

import comprameli.tallerii.fi.uba.ar.comprameli.data.repository.AppServerRestApi
import io.reactivex.Observable


class SessionDaoImpl(private val mAppServerApi: AppServerRestApi) : SessionDao {

    override fun test(): Observable<Boolean> = mAppServerApi.ping().map { true }

}