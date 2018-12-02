package ar.uba.fi.tallerii.comprameli.presentation.dashboard.sales.di

import ar.uba.fi.tallerii.comprameli.di.scope.PerFragment
import ar.uba.fi.tallerii.comprameli.presentation.dashboard.sales.SalesFragment
import dagger.Subcomponent

@PerFragment
@Subcomponent(modules = [SalesModule::class])
interface SalesComponent {
    fun inject(fragment: SalesFragment)
}