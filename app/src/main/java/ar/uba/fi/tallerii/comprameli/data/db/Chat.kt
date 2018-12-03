package ar.uba.fi.tallerii.comprameli.data.db

data class Chat(val messages: List<Message>?) {
    constructor() : this(null)
}