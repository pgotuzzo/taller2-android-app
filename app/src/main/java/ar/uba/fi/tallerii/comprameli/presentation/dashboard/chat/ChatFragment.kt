package ar.uba.fi.tallerii.comprameli.presentation.dashboard.chat

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.LinearLayoutManager.VERTICAL
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ar.uba.fi.tallerii.comprameli.R
import ar.uba.fi.tallerii.comprameli.presentation.base.BaseFragment
import ar.uba.fi.tallerii.comprameli.presentation.dashboard.chat.ChatFragment.BundleExtras.CHAT_ID
import ar.uba.fi.tallerii.comprameli.presentation.dashboard.chat.ChatFragment.BundleExtras.IS_CURRENT_USER_OWNER
import ar.uba.fi.tallerii.comprameli.presentation.dashboard.chat.di.ChatModule
import ar.uba.fi.tallerii.comprameli.presentation.utils.SimpleTextWatcher
import kotlinx.android.synthetic.main.dashboard_chat_fragment.*
import javax.inject.Inject

class ChatFragment : BaseFragment(), ChatContract.View {

    object BundleExtras {
        const val CHAT_ID = "chatId"
        const val IS_CURRENT_USER_OWNER = "isCurrentUserOwner"
    }

    companion object {
        fun getInstance(chatId: String, isCurrentUserOwner: Boolean): ChatFragment {
            val fragment = ChatFragment()
            val args = Bundle()
            args.putString(CHAT_ID, chatId)
            args.putBoolean(IS_CURRENT_USER_OWNER, isCurrentUserOwner)
            fragment.arguments = args
            return fragment
        }
    }

    @Inject
    lateinit var mPresenter: ChatContract.Presenter

    private val mComponent by lazy { app()!!.component.plus(ChatModule()) }

    private val mAdapter = ChatListAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.dashboard_chat_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mPresenter.attachView(this)
        mPresenter.onInit(
                arguments?.getString(CHAT_ID)!!,
                arguments?.getBoolean(IS_CURRENT_USER_OWNER)!!
        )

        messagesList.layoutManager = LinearLayoutManager(context, VERTICAL, false)
        messagesList.adapter = mAdapter

        input.addTextChangedListener(object : SimpleTextWatcher() {
            override fun afterTextChanged(s: Editable?) {
                mPresenter.onTextInputChanged(s?.toString() ?: "")
            }
        })

        sendBtn.setOnClickListener { mPresenter.onSendBtnClick() }
    }

    override fun onDestroyView() {
        mPresenter.detachView()
        super.onDestroyView()
    }

    override fun showMessages(messages: List<ChatContract.Message>) {
        mAdapter.setItems(messages)
    }

    override fun clearInput() {
        input.text = null
    }

}