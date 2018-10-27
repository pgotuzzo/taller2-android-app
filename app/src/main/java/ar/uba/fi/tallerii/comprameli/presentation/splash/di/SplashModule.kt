package ar.uba.fi.tallerii.comprameli.presentation.splash.di

import ar.uba.fi.tallerii.comprameli.di.scope.PerActivity
import ar.uba.fi.tallerii.comprameli.domain.SessionService
import ar.uba.fi.tallerii.comprameli.presentation.splash.SplashContract
import ar.uba.fi.tallerii.comprameli.presentation.splash.SplashPresenter
import dagger.Module
import dagger.Provides

@Module
class SplashModule {

    @Provides
    @PerActivity
    fun providePresenter(sessionService: SessionService): SplashContract.Presenter =
            SplashPresenter(sessionService)

}