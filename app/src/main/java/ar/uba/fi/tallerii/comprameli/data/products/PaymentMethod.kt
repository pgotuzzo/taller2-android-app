package ar.uba.fi.tallerii.comprameli.data.products

import com.google.gson.annotations.SerializedName


data class PaymentMethod(@SerializedName("name") val name: String,
                         @SerializedName("image") val image: String,
                         @SerializedName("type") val type: Int)