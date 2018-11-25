package ar.uba.fi.tallerii.comprameli.presentation.productdetails

import ar.uba.fi.tallerii.comprameli.domain.products.Product
import ar.uba.fi.tallerii.comprameli.presentation.MvpPresenter

interface ProductDetailsContract {

    data class Question(val id: String,
                        val question: String,
                        val answer: String?)

    data class ProductDetails(val title: String,
                              val description: String,
                              val price: Float,
                              val images: List<String>,
                              val units: Int,
                              val questions: List<Question>)

    interface View {
        fun showProductDetails(productDetails: ProductDetails)
        fun enableBuyButton(enable: Boolean)
        fun enableQuestionButton(alert: Boolean)
        fun disableQuestionButton()
        fun showAskQuestionDialog()
        fun showSelectQuestionToAnswerDialog(questions: List<Question>)
        fun showError()
        fun goCheckOut(product: Product)
    }

    interface Presenter : MvpPresenter<View> {
        fun onInit(productId: String)
        fun onQuestionButtonClick()
        fun onSendQuestionClick(question: String)
        fun onSendAnswerClick(questionId: String, answer: String)
        fun onBuyButtonClick()
    }
}