package ar.uba.fi.tallerii.comprameli.presentation.productdetails

import ar.uba.fi.tallerii.comprameli.presentation.MvpPresenter

interface ProductDetailsContract {

    data class Question(val question: String,
                        val answer: String?)

    data class ProductDetails(val title: String,
                              val description: String,
                              val price: Float,
                              val images: List<String>,
                              val units: Int,
                              val questions: List<Question>)

    interface View {
        fun showProductDetails(productDetails: ProductDetails)
        fun showError()
    }

    interface Presenter : MvpPresenter<View> {
        fun onInit(productId: String)
    }
}