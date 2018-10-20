package comprameli.tallerii.fi.uba.ar.comprameli.di.component

import com.pgotuzzo.llavero.di.scope.PerApplication
import comprameli.tallerii.fi.uba.ar.comprameli.di.module.ApplicationModule
import comprameli.tallerii.fi.uba.ar.comprameli.presentation.auth.di.AuthComponent
import comprameli.tallerii.fi.uba.ar.comprameli.presentation.auth.di.AuthModule
import comprameli.tallerii.fi.uba.ar.comprameli.presentation.splash.di.SplashComponent
import comprameli.tallerii.fi.uba.ar.comprameli.presentation.splash.di.SplashModule
import dagger.Component

@PerApplication
@Component(modules = [ApplicationModule::class])
interface ApplicationComponent {
    fun plus(module: SplashModule): SplashComponent
    fun plus(module: AuthModule): AuthComponent
}