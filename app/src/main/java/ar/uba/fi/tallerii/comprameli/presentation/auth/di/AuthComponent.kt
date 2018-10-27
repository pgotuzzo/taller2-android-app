package ar.uba.fi.tallerii.comprameli.presentation.auth.di

import ar.uba.fi.tallerii.comprameli.di.scope.PerActivity
import ar.uba.fi.tallerii.comprameli.presentation.auth.AuthActivity
import dagger.Subcomponent


@PerActivity
@Subcomponent(modules = [AuthModule::class])
interface AuthComponent {
    fun inject(activity: AuthActivity)
}