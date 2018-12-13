package ar.uba.fi.tallerii.comprameli.domain.orders

import ar.uba.fi.tallerii.comprameli.data.orders.OrderData
import ar.uba.fi.tallerii.comprameli.data.orders.OrdersDao
import ar.uba.fi.tallerii.comprameli.data.orders.PaymentData
import ar.uba.fi.tallerii.comprameli.data.products.ProductsDao
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

class OrdersServiceImpl(private val mOrdersDao: OrdersDao,
                        private val mProductsDao: ProductsDao) : OrdersService {

    override fun createCashOrder(productId: String,
                                 units: Int,
                                 cashPaymentName: String,
                                 deliveryIncluded: Boolean): Completable {
        val paymentData = PaymentData(paymentMethodName = cashPaymentName)
        val order = OrderData(
                productId = productId,
                units = units,
                paymentData = paymentData,
                deliveryIncluded = deliveryIncluded)
        return mOrdersDao.createOrder(order).flatMapCompletable { Completable.complete() }
    }

    override fun createCardOrder(productId: String,
                                 units: Int,
                                 cardPaymentName: String,
                                 cardData: CardData,
                                 deliveryIncluded: Boolean): Completable {
        val paymentData = PaymentData(
                paymentMethodName = cardPaymentName,
                cardNumber = cardData.cardNumber,
                cardHolderName = cardData.cardHolderName,
                expirationDate = cardData.expirationDate,
                securityCode = cardData.securityCode)
        val order = OrderData(
                productId = productId,
                units = units,
                paymentData = paymentData,
                deliveryIncluded = deliveryIncluded)
        return mOrdersDao.createOrder(order).flatMapCompletable { Completable.complete() }
    }

    override fun getSales(): Observable<Order> =
            mOrdersDao.getSales()
                    .flatMapObservable { sales -> Observable.fromIterable(sales) }
                    .flatMapSingle { sale ->
                        mProductsDao
                                .getProductById(sale.productId)
                                .map { product -> orderFrom(sale, product.images) }
                    }

    override fun getPurchases(): Observable<Order> =
            mOrdersDao.getPurchases()
                    .flatMapObservable { Observable.fromIterable(it) }
                    .flatMapSingle { purchase ->
                        mProductsDao
                                .getProductById(purchase.productId)
                                .map { product -> orderFrom(purchase, product.images) }
                    }

    override fun getDeliveryCost(productId: String, units: Int): Single<DeliveryEstimation> =
            mOrdersDao.estimateOrderDelivery(productId, units).map { cost ->
                val isAvailable = cost >= 0
                val realCost = if (isAvailable) cost else null
                DeliveryEstimation(isAvailable = isAvailable, cost = realCost)
            }

    override fun rateSeller(trackingNumber: Int, rate: String): Completable =
            mOrdersDao.rateSeller(trackingNumber, rate)

    private fun orderFrom(order: ar.uba.fi.tallerii.comprameli.data.orders.Order, productImages: List<String>): Order =
            Order(
                    productImage = if (productImages.isEmpty()) null else productImages[0],
                    units = order.units,
                    total = order.total,
                    productName = order.productName,
                    status = order.status,
                    transactionId = order.transactionId,
                    deliveryIncluded = order.deliveryIncluded,
                    rate = order.rate,
                    trackingNumber = order.trackingNumber
            )

}