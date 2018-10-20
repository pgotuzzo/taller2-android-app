package comprameli.tallerii.fi.uba.ar.comprameli.presentation.auth.di

import com.pgotuzzo.llavero.di.scope.PerActivity
import comprameli.tallerii.fi.uba.ar.comprameli.presentation.auth.AuthContract
import comprameli.tallerii.fi.uba.ar.comprameli.presentation.auth.AuthPresenter
import dagger.Module
import dagger.Provides

@Module
class AuthModule {

    @Provides
    @PerActivity
    fun providePresenter(): AuthContract.Presenter = AuthPresenter()

}