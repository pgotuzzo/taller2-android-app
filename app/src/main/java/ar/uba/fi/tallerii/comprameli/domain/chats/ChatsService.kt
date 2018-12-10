package ar.uba.fi.tallerii.comprameli.domain.chats

import io.reactivex.Completable
import io.reactivex.Observable

interface ChatsService {

    fun getChatById(chatId: String, isCurrentUserOwner: Boolean): Observable<List<Message>>

    fun sendMessage(chatId: String, message: String): Completable

}