package ar.uba.fi.tallerii.comprameli.presentation.publish.di

import ar.uba.fi.tallerii.comprameli.di.scope.PerActivity
import ar.uba.fi.tallerii.comprameli.presentation.publish.PublishActivity
import dagger.Subcomponent


@PerActivity
@Subcomponent(modules = [PublishModule::class])
interface PublishComponent {
    fun inject(activity: PublishActivity)
}