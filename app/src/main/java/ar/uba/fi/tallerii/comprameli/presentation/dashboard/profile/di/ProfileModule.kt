package ar.uba.fi.tallerii.comprameli.presentation.dashboard.profile.di

import ar.uba.fi.tallerii.comprameli.di.scope.PerFragment
import ar.uba.fi.tallerii.comprameli.domain.profile.ProfileService
import ar.uba.fi.tallerii.comprameli.presentation.dashboard.profile.ProfileContract
import ar.uba.fi.tallerii.comprameli.presentation.dashboard.profile.ProfilePresenter
import dagger.Module
import dagger.Provides


@Module
class ProfileModule {

    @Provides
    @PerFragment
    fun providePresenter(profileService: ProfileService): ProfileContract.Presenter =
            ProfilePresenter(profileService)

}