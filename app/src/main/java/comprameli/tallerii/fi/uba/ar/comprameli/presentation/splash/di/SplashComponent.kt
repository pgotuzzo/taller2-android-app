package comprameli.tallerii.fi.uba.ar.comprameli.presentation.splash.di

import com.pgotuzzo.llavero.di.scope.PerActivity
import comprameli.tallerii.fi.uba.ar.comprameli.presentation.splash.SplashActivity
import dagger.Subcomponent

@PerActivity
@Subcomponent(modules = [SplashModule::class])
interface SplashComponent {
    fun inject(activity: SplashActivity)
}