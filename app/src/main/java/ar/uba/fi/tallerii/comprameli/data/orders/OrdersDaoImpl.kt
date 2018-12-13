package ar.uba.fi.tallerii.comprameli.data.orders

import ar.uba.fi.tallerii.comprameli.data.repository.AppServerRestApi
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class OrdersDaoImpl(private val mAppServerRestApi: AppServerRestApi) : OrdersDao {

    override fun createOrder(order: OrderData): Single<String> =
            mAppServerRestApi.newOrder(order).map { it.trackingNumber }.subscribeOn(Schedulers.io())

    override fun getSales(): Single<List<Order>> =
            mAppServerRestApi.sales().map { it.orders }.subscribeOn(Schedulers.io())

    override fun getPurchases(): Single<List<Order>> =
            mAppServerRestApi.purchases().map { it.orders }.subscribeOn(Schedulers.io())

    override fun estimateOrderDelivery(productId: String, units: Int): Single<Float> =
            mAppServerRestApi
                    .estimateDelivery(EstimateData(productId = productId, units = units))
                    .map { it.cost }
                    .subscribeOn(Schedulers.io())

    override fun rateSeller(trackingNumber: Int, rate: String): Completable =
            mAppServerRestApi.rateSeller(trackingNumber, Rate(rate)).subscribeOn(Schedulers.io())

}