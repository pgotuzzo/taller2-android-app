package ar.uba.fi.tallerii.comprameli.presentation.map.di

import ar.uba.fi.tallerii.comprameli.di.scope.PerActivity
import ar.uba.fi.tallerii.comprameli.presentation.map.MapActivity
import dagger.Subcomponent

@PerActivity
@Subcomponent(modules = [MapModule::class])
interface MapComponent {
    fun inject(activity: MapActivity)
}