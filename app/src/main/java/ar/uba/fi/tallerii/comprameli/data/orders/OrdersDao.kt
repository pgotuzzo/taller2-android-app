package ar.uba.fi.tallerii.comprameli.data.orders

import io.reactivex.Completable
import io.reactivex.Single

interface OrdersDao {

    /**
     * @return order id created
     */
    fun createOrder(order: OrderData): Single<String>

    fun getSales(): Single<List<Order>>

    fun getPurchases(): Single<List<Order>>

    fun estimateOrderDelivery(productId: String, units: Int): Single<Float>

    fun rateSeller(trackingNumber: Int, rate: String): Completable

}