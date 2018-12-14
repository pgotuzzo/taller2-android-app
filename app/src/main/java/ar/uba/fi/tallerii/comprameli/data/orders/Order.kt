package ar.uba.fi.tallerii.comprameli.data.orders

import com.google.gson.annotations.SerializedName

data class Order(@SerializedName("_id") val transactionId: String,
                 @SerializedName("buyer") val buyer: String,
                 @SerializedName("seller") val seller: String,
                 @SerializedName("payment_method") val paymentMethod: String,
                 @SerializedName("product_id") val productId: String,
                 @SerializedName("product_name") val productName: String,
                 @SerializedName("units") val units: Int,
                 @SerializedName("status") val status: String,
                 @SerializedName("rate") val rate: String?,
                 @SerializedName("tracking_number") val trackingNumber: Int,
                 @SerializedName("has_to_be_shipped") val deliveryIncluded: Boolean,
                 @SerializedName("total") val total: Float)
