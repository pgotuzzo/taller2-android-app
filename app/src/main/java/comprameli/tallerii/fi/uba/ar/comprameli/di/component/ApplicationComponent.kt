package comprameli.tallerii.fi.uba.ar.comprameli.di.component

import com.pgotuzzo.llavero.di.scope.PerApplication
import comprameli.tallerii.fi.uba.ar.comprameli.di.module.ApplicationModule
import comprameli.tallerii.fi.uba.ar.comprameli.di.module.DomainModule
import comprameli.tallerii.fi.uba.ar.comprameli.di.module.PersistenceModule
import comprameli.tallerii.fi.uba.ar.comprameli.presentation.auth.di.AuthComponent
import comprameli.tallerii.fi.uba.ar.comprameli.presentation.auth.di.AuthModule
import comprameli.tallerii.fi.uba.ar.comprameli.presentation.auth.signin.di.SignInComponent
import comprameli.tallerii.fi.uba.ar.comprameli.presentation.auth.signin.di.SignInModule
import comprameli.tallerii.fi.uba.ar.comprameli.presentation.splash.di.SplashComponent
import comprameli.tallerii.fi.uba.ar.comprameli.presentation.splash.di.SplashModule
import dagger.Component

@PerApplication
@Component(modules = [ApplicationModule::class, DomainModule::class, PersistenceModule::class])
interface ApplicationComponent {
    fun plus(module: SplashModule): SplashComponent
    fun plus(module: AuthModule): AuthComponent
    fun plus(module: SignInModule): SignInComponent
}