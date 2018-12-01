package ar.uba.fi.tallerii.comprameli.presentation.checkout

data class CardDetails(val cardName: String,
                       val cardNumber: String,
                       val cardHolder: String,
                       val securityCode: String,
                       val expirationDate: String)