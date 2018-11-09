package ar.uba.fi.tallerii.comprameli.domain.profile

import ar.uba.fi.tallerii.comprameli.model.Profile
import io.reactivex.Completable
import io.reactivex.Single

interface ProfileService {

    fun getProfile(): Single<Profile>

    fun updateProfile(profile: Profile): Completable

    fun updateProfile(profile: Profile, avatarUri: String): Single<Profile>

}

