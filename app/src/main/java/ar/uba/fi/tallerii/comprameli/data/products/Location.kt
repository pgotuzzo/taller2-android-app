package ar.uba.fi.tallerii.comprameli.data.products

import com.google.gson.annotations.SerializedName

data class Location(@SerializedName("longitude") val x: Double,
                    @SerializedName("latitude") val y: Double)