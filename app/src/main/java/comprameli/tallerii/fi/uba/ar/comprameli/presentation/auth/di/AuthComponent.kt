package comprameli.tallerii.fi.uba.ar.comprameli.presentation.auth.di

import com.pgotuzzo.llavero.di.scope.PerActivity
import comprameli.tallerii.fi.uba.ar.comprameli.presentation.auth.AuthActivity
import dagger.Subcomponent


@PerActivity
@Subcomponent(modules = [AuthModule::class])
interface AuthComponent {
    fun inject(activity: AuthActivity)
}