package ar.uba.fi.tallerii.comprameli.presentation.dashboard.chat

import ar.uba.fi.tallerii.comprameli.presentation.MvpPresenter

interface ChatContract {

    data class Message(val isCurrentUser: Boolean,
                       val userName: String,
                       val text: String)

    interface View {
        fun showMessages(messages: List<Message>)
        fun clearInput()
    }

    interface Presenter : MvpPresenter<View> {
        fun onInit(chatId: String, isCurrentUserOwner: Boolean)
        fun onTextInputChanged(text: String)
        fun onSendBtnClick()
    }
}