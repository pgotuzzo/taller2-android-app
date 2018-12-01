package ar.uba.fi.tallerii.comprameli.data.orders

import com.google.gson.annotations.SerializedName

data class OrderTracking(@SerializedName("order_tracking_number") val trackingNumber: String)