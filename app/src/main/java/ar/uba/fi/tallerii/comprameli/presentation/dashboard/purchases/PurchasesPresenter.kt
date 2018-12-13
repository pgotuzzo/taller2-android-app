package ar.uba.fi.tallerii.comprameli.presentation.dashboard.purchases

import ar.uba.fi.tallerii.comprameli.domain.orders.Order
import ar.uba.fi.tallerii.comprameli.domain.orders.OrdersService
import ar.uba.fi.tallerii.comprameli.presentation.base.BasePresenter
import ar.uba.fi.tallerii.comprameli.presentation.dashboard.purchases.PurchasesContract.Companion.CHAT
import ar.uba.fi.tallerii.comprameli.presentation.dashboard.purchases.PurchasesContract.Companion.NONE
import ar.uba.fi.tallerii.comprameli.presentation.dashboard.purchases.PurchasesContract.Companion.RATE
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber

class PurchasesPresenter(private val mOrdersService: OrdersService) :
        BasePresenter<PurchasesContract.View>(), PurchasesContract.Presenter {

    private val mDisposables = CompositeDisposable()

    private var mTrackingNumber: Int? = null

    override fun onViewDetached() {
        mDisposables.clear()
        super.onViewDetached()
    }

    override fun onInit() {
        val disposable = mOrdersService.getPurchases().map { fromOrder(it) }.toList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { transactions -> getView()?.showTransactions(transactions) },
                        { throwable -> Timber.e(throwable) }
                )

        mDisposables.add(disposable)
    }

    override fun onRateBtnClick(trackingNumber: Int) {
        mTrackingNumber = trackingNumber
        getView()?.showRateDialog()
    }

    override fun onRate(@PurchasesContract.Companion.Rate value: String) {
        val disposable = mOrdersService
                .rateSeller(mTrackingNumber!!, value)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { onInit() },
                        { throwable -> Timber.e(throwable) }
                )
        mDisposables.add(disposable)
    }

    private fun fromOrder(order: Order): PurchasesContract.Transaction {
        val action = when {
            order.rate != null -> NONE
            order.deliveryIncluded && order.status == "ENVIO REALIZADO" -> RATE
            !order.deliveryIncluded && order.status == "PAGO ACEPTADO" -> RATE
            else -> CHAT
        }
        return PurchasesContract.Transaction(
                productName = order.productName,
                productImage = order.productImage,
                total = order.total,
                status = order.status,
                units = order.units,
                transactionId = order.transactionId,
                action = action,
                trackingNumber = order.trackingNumber
        )
    }

}