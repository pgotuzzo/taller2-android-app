package ar.uba.fi.tallerii.comprameli.presentation.dashboard.di

import ar.uba.fi.tallerii.comprameli.di.scope.PerActivity
import ar.uba.fi.tallerii.comprameli.domain.profile.ProfileService
import ar.uba.fi.tallerii.comprameli.domain.session.SessionService
import ar.uba.fi.tallerii.comprameli.presentation.dashboard.DashboardContract
import ar.uba.fi.tallerii.comprameli.presentation.dashboard.DashboardPresenter
import dagger.Module
import dagger.Provides

@Module
class DashboardModule {

    @Provides
    @PerActivity
    fun providePresenter(sessionService: SessionService,
                         profileService: ProfileService): DashboardContract.Presenter =
            DashboardPresenter(sessionService, profileService)

}