package ar.uba.fi.tallerii.comprameli.data.db

data class Message(val text: String?,
                   val userName: String?,
                   val userId: String?) {
    constructor() : this(null, null, null)
}