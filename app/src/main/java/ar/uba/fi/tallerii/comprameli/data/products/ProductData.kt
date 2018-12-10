package ar.uba.fi.tallerii.comprameli.data.products

import ar.uba.fi.tallerii.comprameli.data.Location
import com.google.gson.annotations.SerializedName

data class ProductData(@SerializedName("description") val description: String,
                       @SerializedName("name") val name: String,
                       @SerializedName("pictures") val images: List<String>,
                       @SerializedName("price") val price: Float,
                       @SerializedName("units") val units: Int,
                       @SerializedName("questions") val questions: List<Question> = ArrayList(),
                       @SerializedName("categories") val categories: List<String>,
                       @SerializedName("payment_methods") val paymentMethods: List<String>,
                       @SerializedName("location") val location: Location)