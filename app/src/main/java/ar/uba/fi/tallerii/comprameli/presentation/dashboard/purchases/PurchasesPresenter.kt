package ar.uba.fi.tallerii.comprameli.presentation.dashboard.purchases

import ar.uba.fi.tallerii.comprameli.domain.orders.OrdersService
import ar.uba.fi.tallerii.comprameli.domain.orders.Purchase
import ar.uba.fi.tallerii.comprameli.presentation.base.BasePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber

class PurchasesPresenter(private val mOrdersService: OrdersService) :
        BasePresenter<PurchasesContract.View>(), PurchasesContract.Presenter {

    private val mDisposables = CompositeDisposable()

    override fun onViewDetached() {
        mDisposables.clear()
        super.onViewDetached()
    }

    override fun onInit() {
        val disposable = mOrdersService.getPurchases().map { fromSale(it) }.toList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { transactions -> getView()?.showTransactions(transactions) },
                        { throwable -> Timber.e(throwable) }
                )

        mDisposables.add(disposable)
    }

    private fun fromSale(purchase: Purchase): PurchasesContract.Transaction =
            PurchasesContract.Transaction(
                    productName = purchase.productName,
                    productImage = purchase.productImage,
                    total = purchase.total,
                    status = purchase.status,
                    units = purchase.units,
                    transactionId = purchase.transactionId
            )
}