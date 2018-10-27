package ar.uba.fi.tallerii.comprameli.presentation.auth.signin.di

import ar.uba.fi.tallerii.comprameli.di.scope.PerFragment
import ar.uba.fi.tallerii.comprameli.presentation.auth.signin.SignInFragment
import dagger.Subcomponent


@PerFragment
@Subcomponent(modules = [SignInModule::class])
interface SignInComponent {
    fun inject(fragment: SignInFragment)
}