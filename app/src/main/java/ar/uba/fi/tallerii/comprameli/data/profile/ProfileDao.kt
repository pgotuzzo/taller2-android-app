package ar.uba.fi.tallerii.comprameli.data.profile

import io.reactivex.Completable
import io.reactivex.Single

interface ProfileDao {

    fun getProfile(): Single<Profile>

    fun updateProfile(profile: Profile): Completable

}