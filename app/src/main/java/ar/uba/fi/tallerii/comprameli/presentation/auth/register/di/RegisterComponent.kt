package ar.uba.fi.tallerii.comprameli.presentation.auth.register.di

import ar.uba.fi.tallerii.comprameli.di.scope.PerFragment
import ar.uba.fi.tallerii.comprameli.presentation.auth.register.RegisterFragment
import dagger.Subcomponent


@PerFragment
@Subcomponent(modules = [RegisterModule::class])
interface RegisterComponent {
    fun inject(fragment: RegisterFragment)
}