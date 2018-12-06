package ar.uba.fi.tallerii.comprameli.model

import java.io.Serializable

data class ProductFilter(val text: String? = null,
                         val seller: String? = null,
                         val units: Int? = null,
                         val priceMin: Float? = null,
                         val priceMax: Float? = null,
                         val longitude: Float? = null,
                         val latitude: Float? = null,
                         val maxDistance: Float? = null,
                         val categories: List<String> = ArrayList(),
                         val paymentMethods: List<String> = ArrayList()) : Serializable
