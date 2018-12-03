package ar.uba.fi.tallerii.comprameli.presentation.dashboard.chat

import ar.uba.fi.tallerii.comprameli.domain.chats.ChatsService
import ar.uba.fi.tallerii.comprameli.presentation.base.BasePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable

class ChatPresenter(private val mChatsService: ChatsService) :
        BasePresenter<ChatContract.View>(), ChatContract.Presenter {

    private val mDisposables = CompositeDisposable()
    private lateinit var mChatId: String
    private var mText: String = ""

    override fun onViewDetached() {
        mDisposables.clear()
        super.onViewDetached()
    }

    override fun onInit(chatId: String) {
        mChatId = chatId
        val disposable = mChatsService
                .getChatById(chatId)
                .map { messageList ->
                    messageList.map {
                        ChatContract.Message(text = it.text, isCurrentUser = it.isCurrentUser, userName = it.userName)
                    }
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { messages -> getView()?.showMessages(messages) }
        mDisposables.add(disposable)
    }

    override fun onTextInputChanged(text: String) {
        mText = text
    }

    override fun onSendBtnClick() {
        if (!mText.isEmpty()) {
            val disposable = mChatsService
                    .sendMessage(mChatId, mText)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe { getView()?.clearInput() }
            mDisposables.add(disposable)
        }
    }

}