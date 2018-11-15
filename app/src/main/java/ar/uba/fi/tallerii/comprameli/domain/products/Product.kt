package ar.uba.fi.tallerii.comprameli.domain.products

data class Product(val productId: String,
                   val title: String,
                   val description: String,
                   val images: List<String>,
                   val price: Float,
                   val units: Int,
                   val question: List<Question>,
                   val seller: String)
