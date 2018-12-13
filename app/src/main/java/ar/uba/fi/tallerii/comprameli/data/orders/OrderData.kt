package ar.uba.fi.tallerii.comprameli.data.orders

import com.google.gson.annotations.SerializedName

class OrderData(@SerializedName("payment_info") val paymentData: PaymentData,
                @SerializedName("product_id") val productId: String,
                @SerializedName("units") val units: Int,
                @SerializedName("has_to_be_shipped") val deliveryIncluded: Boolean)
