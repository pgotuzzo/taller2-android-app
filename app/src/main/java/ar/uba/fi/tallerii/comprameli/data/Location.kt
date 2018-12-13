package ar.uba.fi.tallerii.comprameli.data

import com.google.gson.annotations.SerializedName

data class Location(@SerializedName("coordinates") val coordinates: List<Double>)