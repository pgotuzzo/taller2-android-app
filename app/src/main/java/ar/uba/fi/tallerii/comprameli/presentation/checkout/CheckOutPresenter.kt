package ar.uba.fi.tallerii.comprameli.presentation.checkout

import ar.uba.fi.tallerii.comprameli.domain.products.Product
import ar.uba.fi.tallerii.comprameli.presentation.base.BasePresenter
import ar.uba.fi.tallerii.comprameli.presentation.checkout.CheckOutContract.Companion.CARD
import ar.uba.fi.tallerii.comprameli.presentation.checkout.CheckOutContract.Companion.CASH
import io.reactivex.disposables.CompositeDisposable

class CheckOutPresenter : BasePresenter<CheckOutContract.View>(), CheckOutContract.Presenter {

    private val mDisposables = CompositeDisposable()

    private var mProduct: Product? = null
    private var mPaymentMethodType: Int? = null
    private var mUnits = 1

    override fun onViewDetached() {
        mDisposables.clear()
        super.onViewDetached()
    }

    override fun onInit(product: Product) {
        mProduct = product
        getView()?.setMaxUnits(product.units)
        // FIXME - El server deberia estar añadiendo esto a la responses
        val isCashEnabled = product.paymentMethods.find { method -> method == "Efectivo" } != null
        val isCardEnabled = product.paymentMethods.find { method -> method == "Visa" || method == "American Express" || method == "Mastercard" } != null
        when {
            isCardEnabled && isCashEnabled -> {
                getView()?.enablePayment(CASH, true)
                getView()?.enablePayment(CARD, true)
            }
            isCashEnabled -> {
                getView()?.apply {
                    enablePayment(CASH, true)
                    showSinglePaymentMessage(CASH)
                    showNextBtn()
                }
                mPaymentMethodType = CASH
            }
            isCardEnabled -> {
                getView()?.apply {
                    enablePayment(CARD, true)
                    getView()?.showSinglePaymentMessage(CARD)
                    showNextBtn()
                }
                mPaymentMethodType = CARD
            }
        }
    }

    override fun onPaymentTypeSelected(paymentType: Int) {
        mPaymentMethodType = paymentType
        getView()?.showNextBtn()
    }

    override fun onUnitsChanged(units: Int) {
        mUnits = units
        getView()?.showNextBtn()
    }

    override fun onNextButtonClick() {
        // FIXME
    }

}