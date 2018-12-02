package ar.uba.fi.tallerii.comprameli.presentation.dashboard.sales

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.LinearLayoutManager.VERTICAL
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import ar.uba.fi.tallerii.comprameli.R
import ar.uba.fi.tallerii.comprameli.presentation.base.BaseFragment
import ar.uba.fi.tallerii.comprameli.presentation.dashboard.sales.di.SalesComponent
import ar.uba.fi.tallerii.comprameli.presentation.dashboard.sales.di.SalesModule
import kotlinx.android.synthetic.main.dashboard_sales_fragment.*
import timber.log.Timber
import javax.inject.Inject

class SalesFragment : BaseFragment(), SalesContract.View {

    companion object {
        fun getInstance(): SalesFragment {
            return SalesFragment()
        }
    }

    @Inject
    lateinit var mPresenter: SalesContract.Presenter
    private lateinit var mSalesEventHandler: SalesEventHandler

    private val mComponent: SalesComponent by lazy { app()!!.component.plus(SalesModule()) }

    private val mAdapter = SalesListAdapter()


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try {
            mSalesEventHandler = context as SalesEventHandler
        } catch (e: Exception) {
            Timber.e(e, "Activity must implement Sales Event Handler interface")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.dashboard_sales_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mPresenter.attachView(this)
        mPresenter.onInit()

        itemList.layoutManager = LinearLayoutManager(context, VERTICAL, false)
        itemList.adapter = mAdapter
    }

    override fun onDestroyView() {
        mPresenter.detachView()
        super.onDestroyView()
    }

    override fun showTransactions(transactionsList: List<SalesContract.Transaction>) {
        mAdapter.setItems(transactionsList) { transactionId ->
            mSalesEventHandler.onChatSelected(transactionId)
        }
        itemList.visibility = if (transactionsList.isEmpty()) GONE else VISIBLE
        notFoundImg.visibility = if (transactionsList.isEmpty()) VISIBLE else GONE
        notFoundLabel.visibility = if (transactionsList.isEmpty()) VISIBLE else GONE
    }

}