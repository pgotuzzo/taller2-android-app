package ar.uba.fi.tallerii.comprameli.data.profile

import ar.uba.fi.tallerii.comprameli.data.repository.AppServerRestApi
import io.reactivex.Completable
import io.reactivex.Single

class ProfileDaoImpl(private val mAppServerRestApi: AppServerRestApi) : ProfileDao {

    override fun getProfile(): Single<Profile> = mAppServerRestApi.userProfile()

    override fun updateProfile(profile: Profile): Completable =
            mAppServerRestApi.updateUserProfile(profile)

}