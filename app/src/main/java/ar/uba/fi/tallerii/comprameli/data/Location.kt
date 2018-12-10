package ar.uba.fi.tallerii.comprameli.data

import com.google.gson.annotations.SerializedName

data class Location(@SerializedName("longitude") val longitude: Double,
                    @SerializedName("latitude") val latitude: Double)