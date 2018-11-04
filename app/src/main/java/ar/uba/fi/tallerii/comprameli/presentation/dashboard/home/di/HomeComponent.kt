package ar.uba.fi.tallerii.comprameli.presentation.dashboard.home.di

import ar.uba.fi.tallerii.comprameli.di.scope.PerFragment
import ar.uba.fi.tallerii.comprameli.presentation.dashboard.home.HomeFragment
import dagger.Subcomponent


@PerFragment
@Subcomponent(modules = [HomeModule::class])
interface HomeComponent {
    fun inject(fragment: HomeFragment)
}