package ar.uba.fi.tallerii.comprameli.domain.products

data class ProductData(val title: String,
                       val description: String,
                       val images: List<String>,
                       val price: Float,
                       val units: Int,
                       val categories: List<String>,
                       val paymentMethods: List<String>,
                       val questions: List<Question> = ArrayList())
