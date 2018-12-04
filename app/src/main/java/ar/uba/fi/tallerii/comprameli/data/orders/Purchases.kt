package ar.uba.fi.tallerii.comprameli.data.orders

import com.google.gson.annotations.SerializedName

class Purchases(@SerializedName("orders") val purchases: List<Purchase>)