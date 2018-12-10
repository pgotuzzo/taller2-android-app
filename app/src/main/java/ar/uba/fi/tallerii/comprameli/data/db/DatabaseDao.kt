package ar.uba.fi.tallerii.comprameli.data.db

import io.reactivex.Completable
import io.reactivex.Observable

interface DatabaseDao {

    fun createChat(chatId: String, content: Chat): Completable

    fun getChatById(chatId: String): Observable<Chat>

    fun addChatMessage(chatId: String, message: Message): Completable

    fun setMessagingToken(userId: String, token: String): Completable

    fun removeMessagingToken(userId: String): Completable

}