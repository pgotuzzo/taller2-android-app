package ar.uba.fi.tallerii.comprameli.presentation.dashboard.sales

import ar.uba.fi.tallerii.comprameli.domain.orders.OrdersService
import ar.uba.fi.tallerii.comprameli.domain.orders.Sale
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
                .getSales()
                .map { sale -> fromSale(sale) }
                .toList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { transactions -> getView()?.showTransactions(transactions) },
                        { throwable -> Timber.e(throwable) }
                )

        mDisposables.add(disposable)
    }

    private fun fromSale(sale: Sale): SalesContract.Transaction =
            SalesContract.Transaction(
                    productName = sale.productName,
                    productImage = sale.productImage,
                    total = sale.total,
                    status = sale.status,
                    units = sale.units,
                    transactionId = sale.transactionId
            )


}