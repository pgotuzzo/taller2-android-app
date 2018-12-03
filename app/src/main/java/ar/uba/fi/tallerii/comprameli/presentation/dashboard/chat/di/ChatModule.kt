package ar.uba.fi.tallerii.comprameli.presentation.dashboard.chat.di

import ar.uba.fi.tallerii.comprameli.di.scope.PerFragment
import ar.uba.fi.tallerii.comprameli.domain.chats.ChatsService
import ar.uba.fi.tallerii.comprameli.presentation.dashboard.chat.ChatContract
import ar.uba.fi.tallerii.comprameli.presentation.dashboard.chat.ChatPresenter
import dagger.Module
import dagger.Provides


@Module
class ChatModule {

    @Provides
    @PerFragment
    fun providePresenter(chatsService: ChatsService): ChatContract.Presenter =
            ChatPresenter(chatsService)

}