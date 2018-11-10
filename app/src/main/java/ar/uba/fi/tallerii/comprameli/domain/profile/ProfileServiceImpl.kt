package ar.uba.fi.tallerii.comprameli.domain.profile

import ar.uba.fi.tallerii.comprameli.data.files.FilesDao
import ar.uba.fi.tallerii.comprameli.data.profile.ProfileDao
import io.reactivex.Completable
import io.reactivex.Single

class ProfileServiceImpl(private val mProfileDao: ProfileDao,
                         private val mFilesDao: FilesDao) : ProfileService {

    override fun getProfile(): Single<Profile> = mProfileDao.getProfile()
            .map {
                Profile(name = it.name,
                        surname = it.surname,
                        userId = it.id,
                        facebook = it.facebook,
                        google = it.google,
                        avatar = it.avatar,
                        email = it.email)
            }

    override fun updateProfile(profile: Profile): Completable =
            mProfileDao.updateProfile(
                    ar.uba.fi.tallerii.comprameli.data.profile.Profile(
                            name = profile.name,
                            surname = profile.surname,
                            id = profile.userId,
                            facebook = profile.facebook,
                            google = profile.google,
                            avatar = profile.avatar,
                            email = profile.email)
            )

    override fun updateProfile(profile: Profile, avatarUri: String): Single<Profile> =
            getProfile().flatMap {
                mFilesDao.uploadFile(avatarUri, "images/${it.userId}/profile.png")
            }.flatMap {
                val profileUpdated = profile.copy(avatar = it)
                updateProfile(profileUpdated).toSingleDefault(profileUpdated)
            }

}