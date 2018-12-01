package ar.uba.fi.tallerii.comprameli.domain.orders

import ar.uba.fi.tallerii.comprameli.data.orders.OrderData
import ar.uba.fi.tallerii.comprameli.data.orders.OrdersDao
import ar.uba.fi.tallerii.comprameli.data.orders.PaymentData
import io.reactivex.Completable

class OrdersServiceImpl(private val mOrdersDao: OrdersDao) : OrdersService {

    override fun createCashOrder(productId: String, units: Int, cashPaymentName: String): Completable {
        val paymentData = PaymentData(paymentMethodName = cashPaymentName)
        val order = OrderData(productId = productId, units = units, paymentData = paymentData)
        return mOrdersDao.createOrder(order).flatMapCompletable { Completable.complete() }
    }

    override fun createCardOrder(productId: String, units: Int, cardPaymentName: String, cardData: CardData): Completable {
        val paymentData = PaymentData(
                paymentMethodName = cardPaymentName,
                cardNumber = cardData.cardNumber,
                cardHolderName = cardData.cardHolderName,
                expirationDate = cardData.expirationDate,
                securityCode = cardData.securityCode)
        val order = OrderData(productId = productId, units = units, paymentData = paymentData)
        return mOrdersDao.createOrder(order).flatMapCompletable { Completable.complete() }
    }

}