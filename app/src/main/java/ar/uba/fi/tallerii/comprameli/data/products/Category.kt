package ar.uba.fi.tallerii.comprameli.data.products

import com.google.gson.annotations.SerializedName

data class Category(@SerializedName("name") val name: String,
                    @SerializedName("image") val image: String?)