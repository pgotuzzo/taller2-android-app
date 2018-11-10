package ar.uba.fi.tallerii.comprameli.presentation.auth.register.di

import ar.uba.fi.tallerii.comprameli.di.scope.PerFragment
import ar.uba.fi.tallerii.comprameli.domain.session.SessionService
import ar.uba.fi.tallerii.comprameli.presentation.auth.register.RegisterContract
import ar.uba.fi.tallerii.comprameli.presentation.auth.register.RegisterPresenter
import dagger.Module
import dagger.Provides

@Module
class RegisterModule {
    @Provides
    @PerFragment
    fun providePresenter(sessionService: SessionService): RegisterContract.Presenter =
            RegisterPresenter(sessionService)
}