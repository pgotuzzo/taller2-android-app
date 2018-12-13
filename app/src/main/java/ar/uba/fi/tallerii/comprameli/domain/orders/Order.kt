package ar.uba.fi.tallerii.comprameli.domain.orders

class Order(val transactionId: String,
            val trackingNumber: Int,
            val productImage: String?,
            val productName: String,
            val rate: String?,
            val deliveryIncluded: Boolean,
            val total: Float,
            val units: Int,
            val status: String)