package ar.uba.fi.tallerii.comprameli.presentation.dashboard.purchases

import android.support.annotation.IntDef
import android.support.annotation.StringDef
import ar.uba.fi.tallerii.comprameli.presentation.MvpPresenter

interface PurchasesContract {

    companion object {

        @IntDef(CHAT, RATE, NONE)
        @Retention(AnnotationRetention.SOURCE)
        annotation class Action

        const val CHAT = 0
        const val RATE = 1
        const val NONE = 2

        @StringDef(POSITIVE, NEGATIVE, NEUTRAL)
        @Retention(AnnotationRetention.SOURCE)
        annotation class Rate

        const val POSITIVE = "POSITIVE"
        const val NEGATIVE = "NEGATIVE"
        const val NEUTRAL = "NEUTRAL"
    }

    data class Transaction(val productName: String,
                           val productImage: String?,
                           val total: Float,
                           val status: String,
                           val units: Int,
                           val transactionId: String,
                           val trackingNumber: Int,
                           @Action val action: Int)

    interface View {
        fun showTransactions(transactionsList: List<Transaction>)
        fun showRateDialog()
    }

    interface Presenter : MvpPresenter<View> {
        fun onInit()
        fun onRateBtnClick(trackingNumber: Int)
        fun onRate(@Rate value: String)
    }
}