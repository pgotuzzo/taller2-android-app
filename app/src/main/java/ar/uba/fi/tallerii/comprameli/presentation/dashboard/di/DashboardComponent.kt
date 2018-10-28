package ar.uba.fi.tallerii.comprameli.presentation.dashboard.di

import ar.uba.fi.tallerii.comprameli.di.scope.PerActivity
import ar.uba.fi.tallerii.comprameli.presentation.dashboard.DashboardActivity
import dagger.Subcomponent

@PerActivity
@Subcomponent(modules = [DashboardModule::class])
interface DashboardComponent {
    fun inject(activity: DashboardActivity)
}