package ar.uba.fi.tallerii.comprameli.domain.orders

class Order(val transactionId: String,
            val productImage: String?,
            val productName: String,
            val total: Float,
            val units: Int,
            val status: String)