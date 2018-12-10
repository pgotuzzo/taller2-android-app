package ar.uba.fi.tallerii.comprameli.di.component

import ar.uba.fi.tallerii.comprameli.di.scope.PerService
import ar.uba.fi.tallerii.comprameli.services.CloudMessagingService
import dagger.Subcomponent

@PerService
@Subcomponent()
interface ServiceComponent {
    fun inject(service: CloudMessagingService)
}