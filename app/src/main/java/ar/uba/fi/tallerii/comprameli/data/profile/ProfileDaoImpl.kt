package ar.uba.fi.tallerii.comprameli.data.profile

import ar.uba.fi.tallerii.comprameli.data.repository.AppServerRestApi
import io.reactivex.Single

class ProfileDaoImpl(private val mAppServerRestApi: AppServerRestApi) : ProfileDao {

    override fun getProfile(): Single<Profile> = mAppServerRestApi.userProfile()

}