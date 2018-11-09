package ar.uba.fi.tallerii.comprameli.model

data class ProductFilter(val text: String? = null,
                         val seller: String? = null,
                         val units: Int? = null,
                         val price: Float? = null,
                         val x: Float? = null,
                         val y: Float? = null,
                         val categories: String? = null,
                         val paymentMethods: String? = null)
