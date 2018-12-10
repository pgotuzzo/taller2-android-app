package ar.uba.fi.tallerii.comprameli.presentation.dashboard

interface ChatsEventHandler {
    fun onChatSelected(transactionId: String, isCurrentUserOwner: Boolean)
}