package ar.uba.fi.tallerii.comprameli.presentation.dashboard.profile.di

import ar.uba.fi.tallerii.comprameli.di.scope.PerFragment
import ar.uba.fi.tallerii.comprameli.presentation.dashboard.profile.ProfileFragment
import dagger.Subcomponent


@PerFragment
@Subcomponent(modules = [ProfileModule::class])
interface ProfileComponent {
    fun inject(fragment: ProfileFragment)
}