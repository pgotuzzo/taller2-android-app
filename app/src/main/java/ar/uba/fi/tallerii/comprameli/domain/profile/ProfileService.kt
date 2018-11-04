package ar.uba.fi.tallerii.comprameli.domain.profile

import ar.uba.fi.tallerii.comprameli.model.Profile
import io.reactivex.Single

interface ProfileService {

    fun getProfile(): Single<Profile>

}

