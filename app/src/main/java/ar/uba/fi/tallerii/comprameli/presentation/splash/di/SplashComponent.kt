package ar.uba.fi.tallerii.comprameli.presentation.splash.di

import ar.uba.fi.tallerii.comprameli.di.scope.PerActivity
import ar.uba.fi.tallerii.comprameli.presentation.splash.SplashActivity
import dagger.Subcomponent

@PerActivity
@Subcomponent(modules = [SplashModule::class])
interface SplashComponent {
    fun inject(activity: SplashActivity)
}