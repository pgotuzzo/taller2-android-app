package ar.uba.fi.tallerii.comprameli.domain.orders

data class CardData(val cardNumber: String,
                    val cardHolderName: String,
                    val expirationDate: String,
                    val securityCode: String)