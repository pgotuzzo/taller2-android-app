package ar.uba.fi.tallerii.comprameli.data

import com.google.gson.annotations.SerializedName

data class Location(@SerializedName("x") val longitude: Double,
                    @SerializedName("y") val latitude: Double)