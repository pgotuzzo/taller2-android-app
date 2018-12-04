package ar.uba.fi.tallerii.comprameli.presentation.dashboard.purchases.di

import ar.uba.fi.tallerii.comprameli.di.scope.PerFragment
import ar.uba.fi.tallerii.comprameli.presentation.dashboard.purchases.PurchasesFragment
import dagger.Subcomponent

@PerFragment
@Subcomponent(modules = [PurchasesModule::class])
interface PurchasesComponent {
    fun inject(fragment: PurchasesFragment)
}