package ar.uba.fi.tallerii.comprameli.data.products

import ar.uba.fi.tallerii.comprameli.data.Location
import com.google.gson.annotations.SerializedName

data class Product(@SerializedName("_id") val id: String,
                   @SerializedName("description") val description: String,
                   @SerializedName("name") val name: String,
                   @SerializedName("pictures") val images: List<String>,
                   @SerializedName("price") val price: Float,
                   @SerializedName("published") val publishedDate: String,
                   @SerializedName("units") val units: Int,
                   @SerializedName("seller") val seller: String,
                   @SerializedName("questions") val questions: List<Question>,
                   @SerializedName("categories") val categories: List<String>,
                   @SerializedName("payment_methods") val paymentMethods: List<PaymentMethod>,
                   @SerializedName("location") val location: Location)
