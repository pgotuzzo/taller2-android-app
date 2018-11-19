package ar.uba.fi.tallerii.comprameli.domain.products

data class Product(val productId: String? = null,
                   val title: String,
                   val description: String,
                   val images: List<String>,
                   val price: Float,
                   val units: Int,
                   val categories: List<String>,
                   val paymentMethods: List<String>,
                   val question: List<Question> = ArrayList(),
                   val seller: String? = null)
