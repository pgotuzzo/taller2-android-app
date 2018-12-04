package ar.uba.fi.tallerii.comprameli.presentation.dashboard.purchases

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ar.uba.fi.tallerii.comprameli.R
import ar.uba.fi.tallerii.comprameli.presentation.base.BaseFragment
import ar.uba.fi.tallerii.comprameli.presentation.dashboard.ChatsEventHandler
import ar.uba.fi.tallerii.comprameli.presentation.dashboard.purchases.di.PurchasesComponent
import ar.uba.fi.tallerii.comprameli.presentation.dashboard.purchases.di.PurchasesModule
import kotlinx.android.synthetic.main.dashboard_purchases_fragment.*
import timber.log.Timber
import javax.inject.Inject

class PurchasesFragment : BaseFragment(), PurchasesContract.View {

    companion object {
        fun getInstance() = PurchasesFragment()
    }

    @Inject
    lateinit var mPresenter: PurchasesContract.Presenter
    private lateinit var mChatsEventHandler: ChatsEventHandler

    private val mComponent: PurchasesComponent by lazy { app()!!.component.plus(PurchasesModule()) }

    private val mAdapter = PurchasesListAdapter()

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try {
            mChatsEventHandler = context as ChatsEventHandler
        } catch (e: Exception) {
            Timber.e(e, "Activity must implement Chats Event Handler interface")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.dashboard_purchases_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mPresenter.attachView(this)
        mPresenter.onInit()

        itemList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        itemList.adapter = mAdapter
    }

    override fun onDestroyView() {
        mPresenter.detachView()
        super.onDestroyView()
    }

    override fun showTransactions(transactionsList: List<PurchasesContract.Transaction>) {
        mAdapter.setItems(transactionsList) { transactionId ->
            mChatsEventHandler.onChatSelected(transactionId)
        }
        itemList.visibility = if (transactionsList.isEmpty()) View.GONE else View.VISIBLE
        notFoundImg.visibility = if (transactionsList.isEmpty()) View.VISIBLE else View.GONE
        notFoundLabel.visibility = if (transactionsList.isEmpty()) View.VISIBLE else View.GONE
    }

}