package ar.uba.fi.tallerii.comprameli.domain.chats

import ar.uba.fi.tallerii.comprameli.data.db.Chat
import ar.uba.fi.tallerii.comprameli.data.db.ChatProduct
import ar.uba.fi.tallerii.comprameli.data.db.DatabaseDao
import ar.uba.fi.tallerii.comprameli.data.orders.OrdersDao
import ar.uba.fi.tallerii.comprameli.data.profile.ProfileDao
import io.reactivex.Completable
import io.reactivex.Observable

class ChatsServiceImpl(private val mDatabaseDao: DatabaseDao,
                       private val mProfileDao: ProfileDao,
                       private val mOrdersDao: OrdersDao) : ChatsService {

    override fun getChatById(chatId: String, isCurrentUserOwner: Boolean): Observable<List<Message>> =
            getChatById(chatId).onErrorResumeNext(
                    // Error - Create chat
                    createChat(chatId, isCurrentUserOwner)
                            // Try again
                            .andThen(mDatabaseDao.getChatById(chatId).map { it.messages }
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
                            )
            )


    override fun sendMessage(chatId: String, message: String): Completable =
            mProfileDao.getProfile().flatMapCompletable { profile ->
                mDatabaseDao.addChatMessage(chatId, toMessage(profile.id, profile.name, message))
            }

    private fun toMessage(userId: String, userName: String, text: String) =
            ar.uba.fi.tallerii.comprameli.data.db.Message(text = text, userName = userName, userId = userId)

    private fun getChatById(chatId: String): Observable<List<Message>> =
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

    private fun createChat(chatId: String, isCurrentUserOwner: Boolean): Completable {
        val findOrderTask =
                if (isCurrentUserOwner) mOrdersDao.getSales().map { salesList -> salesList.first { sale -> sale.transactionId == chatId } }
                else mOrdersDao.getPurchases().map { purchasesList -> purchasesList.first { purchase -> purchase.transactionId == chatId } }
        return findOrderTask.map {
            Chat(
                    messages = ArrayList(),
                    buyerId = it.buyer,
                    ownerId = it.seller,
                    product = ChatProduct(name = it.productName)
            )
        }.flatMapCompletable { chat -> mDatabaseDao.createChat(chatId, chat) }
    }

}