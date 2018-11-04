package ar.uba.fi.tallerii.comprameli.domain.profile

import ar.uba.fi.tallerii.comprameli.data.profile.ProfileDao
import ar.uba.fi.tallerii.comprameli.model.Profile
import io.reactivex.Single

class ProfileServiceImpl(private val mProfileDao: ProfileDao) : ProfileService {

    override fun getProfile(): Single<Profile> = mProfileDao.getProfile()
            .map {
                Profile(name = it.name,
                        surname = it.surname,
                        sellerId = it.id,
                        facebook = it.facebook,
                        google = it.google,
                        avatar = it.avatar,
                        email = it.email)
            }

}