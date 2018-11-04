package ar.uba.fi.tallerii.comprameli.data.profile

import io.reactivex.Single

interface ProfileDao {

    fun getProfile(): Single<Profile>

}