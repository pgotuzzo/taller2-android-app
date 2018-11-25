package ar.uba.fi.tallerii.comprameli.presentation.checkout

import android.support.annotation.IntDef
import ar.uba.fi.tallerii.comprameli.domain.products.Product
import ar.uba.fi.tallerii.comprameli.presentation.MvpPresenter

interface CheckOutContract {

    companion object {

        @IntDef(CASH, CARD)
        @Retention(AnnotationRetention.SOURCE)
        annotation class PaymentType

        const val CASH = 0
        const val CARD = 1
    }

    interface View {
        fun enablePayment(@PaymentType paymentType: Int, enable: Boolean)
        fun showSinglePaymentMessage(@PaymentType paymentType: Int)
        fun setMaxUnits(max: Int)
        fun showNextBtn()
    }

    interface Presenter : MvpPresenter<View> {
        fun onInit(product: Product)
        fun onPaymentTypeSelected(@PaymentType paymentType: Int)
        fun onUnitsChanged(units: Int)
        fun onNextButtonClick()
    }

}