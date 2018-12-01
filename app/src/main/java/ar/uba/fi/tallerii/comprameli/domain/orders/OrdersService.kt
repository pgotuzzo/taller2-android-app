package ar.uba.fi.tallerii.comprameli.domain.orders

import io.reactivex.Completable

interface OrdersService {

    fun createCashOrder(productId: String, units: Int, cashPaymentName: String): Completable

    fun createCardOrder(productId: String, units: Int, cardPaymentName: String, cardData: CardData): Completable

}