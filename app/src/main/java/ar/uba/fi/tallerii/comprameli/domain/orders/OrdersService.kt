package ar.uba.fi.tallerii.comprameli.domain.orders

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

interface OrdersService {

    fun createCashOrder(productId: String, units: Int, cashPaymentName: String, deliveryIncluded: Boolean): Completable

    fun createCardOrder(productId: String, units: Int, cardPaymentName: String, cardData: CardData, deliveryIncluded: Boolean): Completable

    fun getSales(): Observable<Order>

    fun getPurchases(): Observable<Order>

    fun getDeliveryCost(productId: String, units: Int): Single<DeliveryEstimation>

    fun rateSeller(trackingNumber: Int, rate: String): Completable

}