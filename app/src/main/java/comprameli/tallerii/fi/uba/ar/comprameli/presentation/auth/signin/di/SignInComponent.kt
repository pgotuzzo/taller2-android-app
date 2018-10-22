package comprameli.tallerii.fi.uba.ar.comprameli.presentation.auth.signin.di

import com.pgotuzzo.llavero.di.scope.PerFragment
import comprameli.tallerii.fi.uba.ar.comprameli.presentation.auth.signin.SignInFragment
import dagger.Subcomponent


@PerFragment
@Subcomponent(modules = [SignInModule::class])
interface SignInComponent {
    fun inject(fragment: SignInFragment)
}