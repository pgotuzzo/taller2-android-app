package ar.uba.fi.tallerii.comprameli.presentation.auth.di

import ar.uba.fi.tallerii.comprameli.di.scope.PerActivity
import ar.uba.fi.tallerii.comprameli.presentation.auth.AuthContract
import ar.uba.fi.tallerii.comprameli.presentation.auth.AuthPresenter
import dagger.Module
import dagger.Provides

@Module
class AuthModule {

    @Provides
    @PerActivity
    fun providePresenter(): AuthContract.Presenter = AuthPresenter()

}