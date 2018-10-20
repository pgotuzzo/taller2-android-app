package comprameli.tallerii.fi.uba.ar.comprameli.presentation.splash.di

import com.pgotuzzo.llavero.di.scope.PerActivity
import comprameli.tallerii.fi.uba.ar.comprameli.presentation.splash.SplashContract
import comprameli.tallerii.fi.uba.ar.comprameli.presentation.splash.SplashPresenter
import dagger.Module
import dagger.Provides

@Module
class SplashModule {

    @Provides
    @PerActivity
    fun providePresenter(): SplashContract.Presenter = SplashPresenter()

}