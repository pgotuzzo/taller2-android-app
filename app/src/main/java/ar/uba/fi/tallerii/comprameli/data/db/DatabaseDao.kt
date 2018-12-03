package ar.uba.fi.tallerii.comprameli.data.db

import io.reactivex.Completable
import io.reactivex.Observable

interface DatabaseDao {

    fun getChatById(chatId: String): Observable<Chat>

    fun addChatMessage(chatId: String, message: Message): Completable

}