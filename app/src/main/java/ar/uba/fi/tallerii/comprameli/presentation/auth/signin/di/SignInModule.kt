package ar.uba.fi.tallerii.comprameli.presentation.auth.signin.di

import ar.uba.fi.tallerii.comprameli.di.scope.PerFragment
import ar.uba.fi.tallerii.comprameli.domain.session.SessionService
import ar.uba.fi.tallerii.comprameli.presentation.auth.signin.SignInContract
import ar.uba.fi.tallerii.comprameli.presentation.auth.signin.SignInPresenter
import dagger.Module
import dagger.Provides

@Module
class SignInModule {
    @Provides
    @PerFragment
    fun providePresenter(sessionService: SessionService): SignInContract.Presenter =
            SignInPresenter(sessionService)
}