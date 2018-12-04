package ar.uba.fi.tallerii.comprameli.presentation.dashboard.purchases

import ar.uba.fi.tallerii.comprameli.presentation.MvpPresenter

interface PurchasesContract {

    data class Transaction(val productName: String,
                           val productImage: String?,
                           val total: Float,
                           val status: String,
                           val units: Int,
                           val transactionId: String)

    interface View {
        fun showTransactions(transactionsList: List<Transaction>)
    }

    interface Presenter : MvpPresenter<View> {
        fun onInit()
    }
}