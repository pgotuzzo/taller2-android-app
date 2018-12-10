package ar.uba.fi.tallerii.comprameli.data.db

import ar.uba.fi.tallerii.comprameli.data.db.FirebaseDatabaseDao.Collection.CHAT
import ar.uba.fi.tallerii.comprameli.data.db.FirebaseDatabaseDao.Collection.MESSAGING_TOKENS
import com.google.firebase.firestore.*
import io.reactivex.Completable
import io.reactivex.Observable


class FirebaseDatabaseDao : DatabaseDao {

    object Collection {
        const val CHAT = "chats"
        const val MESSAGING_TOKENS = "messagingTokens"
    }

    override fun createChat(chatId: String, content: Chat): Completable {
        var subscription: ListenerRegistration? = null
        return Completable.create { emitter ->
            val documentRef = FirebaseFirestore.getInstance().collection(CHAT).document(chatId)
            subscription = documentRef.addSnapshotListener(EventListener<DocumentSnapshot> { snapshot, e ->
                if (e != null) {
                    emitter.onError(e)
                    emitter.onComplete()
                    return@EventListener
                }

                if (snapshot != null && snapshot.exists()) {
                    emitter.onError(Exception("Chat has been already created"))
                } else {
                    subscription?.remove()
                    // Nonexistent chat - Create it
                    documentRef.set(content)
                            .addOnSuccessListener { emitter.onComplete() }
                            .addOnFailureListener { exception -> emitter.onError(exception) }
                }
            })
        }.doOnComplete {
            subscription?.remove()
        }.doOnError {
            subscription?.remove()
        }
    }

    override fun getChatById(chatId: String): Observable<Chat> = Observable.create { emitter ->
        val documentRef = FirebaseFirestore.getInstance().collection(CHAT).document(chatId)
        val subscription = documentRef.addSnapshotListener(EventListener<DocumentSnapshot> { snapshot, e ->
            if (e != null) {
                emitter.onError(e)
                emitter.onComplete()
                return@EventListener
            }

            if (snapshot != null && snapshot.exists()) {
                val chat = snapshot.toObject(Chat::class.java)
                emitter.onNext(chat!!)
            } else {
                emitter.onError(Exception("Nonexistent chat"))
            }
        })
        emitter.setCancellable { subscription.remove() }
    }

    override fun addChatMessage(chatId: String, message: Message): Completable = Completable.create { emitter ->
        val documentRef: DocumentReference = FirebaseFirestore.getInstance().collection(CHAT).document(chatId)
        documentRef.get()
                .addOnFailureListener { exception -> emitter.onError(exception) }
                .addOnSuccessListener { document ->
                    val messages: MutableList<Message> = ArrayList()
                    if (document != null && document.exists()) {
                        val chat = document.toObject(Chat::class.java)
                        chat!!.messages!!.apply { messages.addAll(this) }
                        messages.add(message)
                        documentRef.set(chat.copy(messages = messages))
                                .addOnSuccessListener { emitter.onComplete() }
                                .addOnFailureListener { exception -> emitter.onError(exception) }
                    } else {
                        emitter.onError(Exception("Trying to add messages to a nonexistent chat"))
                    }
                }
    }

    override fun setMessagingToken(userId: String, token: String): Completable = Completable.create { emitter ->
        val documentRef: DocumentReference = FirebaseFirestore.getInstance().collection(MESSAGING_TOKENS).document(userId)
        documentRef.set(MessagingToken(token))
                .addOnSuccessListener { emitter.onComplete() }
                .addOnFailureListener { exception -> emitter.onError(exception) }
    }

    override fun removeMessagingToken(userId: String): Completable = Completable.create { emitter ->
        val documentRef: DocumentReference = FirebaseFirestore.getInstance().collection(MESSAGING_TOKENS).document(userId)
        documentRef.set(MessagingToken(null))
                .addOnSuccessListener { emitter.onComplete() }
                .addOnFailureListener { exception -> emitter.onError(exception) }
    }

}