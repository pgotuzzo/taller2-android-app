package ar.uba.fi.tallerii.comprameli.data.profile

import ar.uba.fi.tallerii.comprameli.data.repository.AppServerRestApi
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class ProfileDaoImpl(private val mAppServerRestApi: AppServerRestApi) : ProfileDao {

    override fun registerProfile(profile: Profile): Completable =
            mAppServerRestApi.registerUser(profile)

    override fun getProfile(): Single<Profile> =
            mAppServerRestApi.userProfile().subscribeOn(Schedulers.io())

    override fun updateProfile(profileChanges: ProfileChanges): Completable =
            mAppServerRestApi.updateUserProfile(profileChanges).subscribeOn(Schedulers.io())

}