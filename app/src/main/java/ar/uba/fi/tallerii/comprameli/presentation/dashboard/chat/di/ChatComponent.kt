package ar.uba.fi.tallerii.comprameli.presentation.dashboard.chat.di

import ar.uba.fi.tallerii.comprameli.di.scope.PerFragment
import ar.uba.fi.tallerii.comprameli.presentation.dashboard.chat.ChatFragment
import dagger.Subcomponent


@PerFragment
@Subcomponent(modules = [ChatModule::class])
interface ChatComponent {
    fun inject(fragment: ChatFragment)
}