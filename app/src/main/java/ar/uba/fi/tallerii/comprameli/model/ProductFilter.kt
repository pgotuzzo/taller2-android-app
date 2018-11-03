package ar.uba.fi.tallerii.comprameli.model

data class ProductFilter(val name: String? = null,
                         val description: String? = null,
                         val seller: String? = null,
                         val units: Int? = null,
                         val price: Float? = null,
                         val x: Float? = null,
                         val y: Float? = null,
                         val categories: String? = null,
                         val paymentMethods: String? = null)
