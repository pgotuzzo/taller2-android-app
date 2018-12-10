package ar.uba.fi.tallerii.comprameli.presentation.dashboard.sales

import ar.uba.fi.tallerii.comprameli.domain.orders.Order
import ar.uba.fi.tallerii.comprameli.domain.orders.OrdersService
import ar.uba.fi.tallerii.comprameli.presentation.base.BasePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber

class SalesPresenter(private val mOrdersService: OrdersService) :
        BasePresenter<SalesContract.View>(), SalesContract.Presenter {

    private val mDisposables = CompositeDisposable()

    override fun onViewDetached() {
        mDisposables.clear()
        super.onViewDetached()
    }

    override fun onInit() {
        val disposable = mOrdersService
                .getSales().map { fromOrder(it) }.toList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { transactions -> getView()?.showTransactions(transactions) },
                        { throwable -> Timber.e(throwable) }
                )

        mDisposables.add(disposable)
    }

    private fun fromOrder(order: Order): SalesContract.Transaction =
            SalesContract.Transaction(
                    productName = order.productName,
                    productImage = order.productImage,
                    total = order.total,
                    status = order.status,
                    units = order.units,
                    transactionId = order.transactionId
            )

}