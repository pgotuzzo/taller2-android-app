package ar.uba.fi.tallerii.comprameli.data.db

data class Chat(val ownerId: String?,
                val buyerId: String?,
                val product: ChatProduct?,
                val messages: List<Message>?) {
    constructor() : this(null, null, null, null)
}