package ar.uba.fi.tallerii.comprameli.di.component

import ar.uba.fi.tallerii.comprameli.di.module.ApplicationModule
import ar.uba.fi.tallerii.comprameli.di.module.DomainModule
import ar.uba.fi.tallerii.comprameli.di.module.PersistenceModule
import ar.uba.fi.tallerii.comprameli.di.scope.PerApplication
import ar.uba.fi.tallerii.comprameli.presentation.auth.di.AuthComponent
import ar.uba.fi.tallerii.comprameli.presentation.auth.di.AuthModule
import ar.uba.fi.tallerii.comprameli.presentation.auth.signin.di.SignInComponent
import ar.uba.fi.tallerii.comprameli.presentation.auth.signin.di.SignInModule
import ar.uba.fi.tallerii.comprameli.presentation.splash.di.SplashComponent
import ar.uba.fi.tallerii.comprameli.presentation.splash.di.SplashModule
import dagger.Component

@PerApplication
@Component(modules = [ApplicationModule::class, DomainModule::class, PersistenceModule::class])
interface ApplicationComponent {
    fun plus(module: SplashModule): SplashComponent
    fun plus(module: AuthModule): AuthComponent
    fun plus(module: SignInModule): SignInComponent
}