package ar.uba.fi.tallerii.comprameli.data.db

import ar.uba.fi.tallerii.comprameli.data.db.FirebaseDatabaseDao.Collection.CHAT
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import io.reactivex.Completable
import io.reactivex.Observable


class FirebaseDatabaseDao : DatabaseDao {

    object Collection {
        const val CHAT = "chats"
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
                // Nonexistent chat - Create it
                val emptyChat = Chat(ArrayList())
                documentRef.set(emptyChat)
                        .addOnFailureListener { exception -> emitter.onError(exception) }
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
                        chat?.messages?.apply { messages.addAll(this) }
                    }
                    messages.add(message)
                    documentRef.set(Chat(messages))
                            .addOnSuccessListener { emitter.onComplete() }
                            .addOnFailureListener { exception -> emitter.onError(exception) }
                }
    }

}