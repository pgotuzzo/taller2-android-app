package ar.uba.fi.tallerii.comprameli.data.orders

import com.google.gson.annotations.SerializedName

data class Sales(@SerializedName("orders") val sales: List<Sale>)