package ar.uba.fi.tallerii.comprameli.data.products

import com.google.gson.annotations.SerializedName

data class Product(@SerializedName("_id") val id: String? = null,
                   @SerializedName("description") val description: String,
                   @SerializedName("name") val name: String,
                   @SerializedName("pictures") val images: List<String>,
                   @SerializedName("price") val price: Float,
                   @SerializedName("published") val publishedDate: String? = null,
                   @SerializedName("units") val units: Int,
                   @SerializedName("seller") val seller: String? = null,
                   @SerializedName("questions") val questions: List<Question> = ArrayList(),
                   @SerializedName("categories") val categories: List<String>,
                   @SerializedName("payment_methods") val paymentMethods: List<String>,
                   @SerializedName("location") val location: Location)