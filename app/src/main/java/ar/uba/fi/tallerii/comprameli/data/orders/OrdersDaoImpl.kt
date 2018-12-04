package ar.uba.fi.tallerii.comprameli.data.orders

import ar.uba.fi.tallerii.comprameli.data.repository.AppServerRestApi
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class OrdersDaoImpl(private val mAppServerRestApi: AppServerRestApi) : OrdersDao {

    override fun createOrder(order: OrderData): Single<String> =
            mAppServerRestApi.newOrder(order).map { it.trackingNumber }.subscribeOn(Schedulers.io())

    override fun getSales(): Single<List<Sale>> =
            mAppServerRestApi.sales().map { it.sales }.subscribeOn(Schedulers.io())

    override fun getPurchases(): Single<List<Purchase>> =
            mAppServerRestApi.purchases().map { it.purchases }.subscribeOn(Schedulers.io())

}