package ar.uba.fi.tallerii.comprameli.data.orders

import com.google.gson.annotations.SerializedName

data class PaymentData(@SerializedName("payment_method") val paymentMethodName: String,
                       @SerializedName("card_number") val cardNumber: String? = null,
                       @SerializedName("cardholder_name") val cardHolderName: String? = null,
                       @SerializedName("expiration_date") val expirationDate: String? = null,
                       @SerializedName("security_code") val securityCode: String? = null)
