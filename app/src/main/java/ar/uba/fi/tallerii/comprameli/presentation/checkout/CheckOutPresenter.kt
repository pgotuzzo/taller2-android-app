package ar.uba.fi.tallerii.comprameli.presentation.checkout

import ar.uba.fi.tallerii.comprameli.domain.orders.CardData
import ar.uba.fi.tallerii.comprameli.domain.orders.OrdersService
import ar.uba.fi.tallerii.comprameli.domain.products.PaymentMethod
import ar.uba.fi.tallerii.comprameli.domain.products.Product
import ar.uba.fi.tallerii.comprameli.presentation.base.BasePresenter
import ar.uba.fi.tallerii.comprameli.presentation.checkout.CheckOutContract.Companion.CARD
import ar.uba.fi.tallerii.comprameli.presentation.checkout.CheckOutContract.Companion.CASH
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable

class CheckOutPresenter(private val mOrdersService: OrdersService) :
        BasePresenter<CheckOutContract.View>(), CheckOutContract.Presenter {

    private val mDisposables = CompositeDisposable()

    private var mProduct: Product? = null
    private var mPaymentSelection: Int? = null
    private var mCashPaymentName: String? = null
    private var mUnits = 1
    private var mCardDetails: CardDetails? = null

    override fun onViewDetached() {
        mDisposables.clear()
        super.onViewDetached()
    }

    override fun onInit(product: Product) {
        mProduct = product
        getView()?.setMaxUnits(product.units)
        val cashPayment = product.paymentMethods.find { method -> method.type == PaymentMethod.PaymentType.CASH }
        cashPayment?.apply { mCashPaymentName = cashPayment.name }
        val isCashEnabled = cashPayment != null
        val isCardEnabled = product.paymentMethods.find { method -> method.type == PaymentMethod.PaymentType.CARD } != null
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
                mPaymentSelection = CASH
            }
            isCardEnabled -> {
                getView()?.apply {
                    enablePayment(CARD, true)
                    getView()?.showSinglePaymentMessage(CARD)
                    showNextBtn()
                }
                mPaymentSelection = CARD
            }
        }
    }

    override fun onPaymentTypeSelected(paymentType: Int) {
        mPaymentSelection = paymentType
        getView()?.showNextBtn()
    }

    override fun onUnitsChanged(units: Int) {
        mUnits = units
        getView()?.showNextBtn()
    }

    override fun onNextButtonClick() {
        getView()?.apply {
            if (mPaymentSelection == CARD) {
                val cardsAvailable = mProduct!!.paymentMethods
                        .filter { method -> method.type == PaymentMethod.PaymentType.CARD }
                        .map { paymentMethod -> CheckOutContract.Card(paymentMethod.name, paymentMethod.image) }
                showCardDetailsForm(cardsAvailable)
            } else {
                showConfirmationDialog()
            }
        }
    }

    override fun onCardDetailsInput(cardDetails: CardDetails) {
        mCardDetails = cardDetails
        getView()?.showConfirmationDialog()
    }

    override fun onPaymentConfirmed() {
        val disposable = when (mPaymentSelection) {
            CASH -> {
                mOrdersService.createCashOrder(mProduct!!.productId, mUnits, mCashPaymentName!!)
            }
            CARD -> {
                val cardData = CardData(
                        cardNumber = mCardDetails!!.cardNumber,
                        cardHolderName = mCardDetails!!.cardHolder,
                        expirationDate = mCardDetails!!.expirationDate,
                        securityCode = mCardDetails!!.securityCode)
                mOrdersService.createCardOrder(mProduct!!.productId, mUnits, mCardDetails!!.cardName, cardData)
            }
            else -> {
                Completable.create { getView()?.showError() }
            }
        }.observeOn(AndroidSchedulers.mainThread())
                .subscribe { getView()?.dismiss() }

        mDisposables.add(disposable)
    }

}