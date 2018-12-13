package ar.uba.fi.tallerii.comprameli.data.orders

import com.google.gson.annotations.SerializedName

data class DeliveryEstimation(@SerializedName("estimated_cost") val cost: Float)