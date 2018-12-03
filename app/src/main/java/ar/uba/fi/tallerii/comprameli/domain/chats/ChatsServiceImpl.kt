package ar.uba.fi.tallerii.comprameli.domain.chats

import ar.uba.fi.tallerii.comprameli.data.db.DatabaseDao
import ar.uba.fi.tallerii.comprameli.data.profile.ProfileDao
import io.reactivex.Completable
import io.reactivex.Observable

class ChatsServiceImpl(private val mDatabaseDao: DatabaseDao,
                       private val mProfileDao: ProfileDao) : ChatsService {

    override fun getChatById(chatId: String): Observable<List<Message>> =
            mDatabaseDao
                    .getChatById(chatId).map { it.messages }
                    .flatMapSingle { messages ->
                        mProfileDao.getProfile().map { it.id }.map { userId ->
                            messages.map {
                                Message(
                                        text = it.text
                                                ?: "",
                                        isCurrentUser = it.userId == userId,
                                        userName = it.userName
                                                ?: "")
                            }
                        }
                    }

    override fun sendMessage(chatId: String, message: String): Completable =
            mProfileDao.getProfile().flatMapCompletable { profile ->
                mDatabaseDao.addChatMessage(chatId, toMessage(profile.id, profile.name, message))
            }

    private fun toMessage(userId: String, userName: String, text: String) =
            ar.uba.fi.tallerii.comprameli.data.db.Message(text = text, userName = userName, userId = userId)

}