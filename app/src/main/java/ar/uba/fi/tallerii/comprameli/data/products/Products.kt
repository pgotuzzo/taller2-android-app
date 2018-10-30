package ar.uba.fi.tallerii.comprameli.data.products

import com.google.gson.annotations.SerializedName

data class Products(@SerializedName("count")
                    val productsCount: Int,
                    @SerializedName("result")
                    val productsList: List<Product>)