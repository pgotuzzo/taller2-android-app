package ar.uba.fi.tallerii.comprameli.data.products

import com.google.gson.annotations.SerializedName

data class Location(@SerializedName("x") val x: Double,
                    @SerializedName("y") val y: Double)