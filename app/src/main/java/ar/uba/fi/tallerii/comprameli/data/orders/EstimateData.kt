package ar.uba.fi.tallerii.comprameli.data.orders

import com.google.gson.annotations.SerializedName

data class EstimateData(@SerializedName("units") val units: Int,
                        @SerializedName("product_id") val productId: String)
