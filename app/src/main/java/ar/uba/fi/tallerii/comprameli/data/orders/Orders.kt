package ar.uba.fi.tallerii.comprameli.data.orders

import com.google.gson.annotations.SerializedName

class Orders(@SerializedName("orders") val orders: List<Order>)