package ar.uba.fi.tallerii.comprameli.domain.chats

data class Message(val isCurrentUser: Boolean,
                   val userName: String,
                   val text: String)
