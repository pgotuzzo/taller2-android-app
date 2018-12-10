package ar.uba.fi.tallerii.comprameli.data.db

data class MessagingToken(val token: String?) {
    constructor() : this(null)
}