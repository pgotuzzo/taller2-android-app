package ar.uba.fi.tallerii.comprameli.data.orders

import io.reactivex.Single

interface OrdersDao {

    /**
     * @return order id created
     */
    fun createOrder(order: OrderData): Single<String>

    fun getSales(): Single<List<Sale>>

    fun getPurchases(): Single<List<Purchase>>

}