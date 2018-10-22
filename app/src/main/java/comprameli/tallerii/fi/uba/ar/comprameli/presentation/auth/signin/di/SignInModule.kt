package comprameli.tallerii.fi.uba.ar.comprameli.presentation.auth.signin.di

import com.pgotuzzo.llavero.di.scope.PerFragment
import comprameli.tallerii.fi.uba.ar.comprameli.presentation.auth.signin.SignInContract
import comprameli.tallerii.fi.uba.ar.comprameli.presentation.auth.signin.SignInPresenter
import dagger.Module
import dagger.Provides

@Module
class SignInModule {
    @Provides
    @PerFragment
    fun providePresenter(): SignInContract.Presenter = SignInPresenter()
}