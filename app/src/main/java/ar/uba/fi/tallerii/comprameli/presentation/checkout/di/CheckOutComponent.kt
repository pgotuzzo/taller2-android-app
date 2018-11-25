package ar.uba.fi.tallerii.comprameli.presentation.checkout.di

import ar.uba.fi.tallerii.comprameli.di.scope.PerActivity
import ar.uba.fi.tallerii.comprameli.presentation.checkout.CheckOutActivity
import dagger.Subcomponent

@PerActivity
@Subcomponent(modules = [CheckOutModule::class])
interface CheckOutComponent {
    fun inject(activity: CheckOutActivity)
}