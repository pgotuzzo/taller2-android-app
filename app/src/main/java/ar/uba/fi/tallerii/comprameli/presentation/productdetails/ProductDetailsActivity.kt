package ar.uba.fi.tallerii.comprameli.presentation.productdetails

import android.app.AlertDialog
import android.content.res.ColorStateList
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import ar.uba.fi.tallerii.comprameli.R
import ar.uba.fi.tallerii.comprameli.presentation.base.BaseActivity
import ar.uba.fi.tallerii.comprameli.presentation.productdetails.di.ProductDetailsModule
import kotlinx.android.synthetic.main.product_details_activity.*
import kotlinx.android.synthetic.main.product_details_answer_question_dialog.view.*
import kotlinx.android.synthetic.main.product_details_ask_question_dialog.view.*
import timber.log.Timber
import java.text.NumberFormat
import javax.inject.Inject

class ProductDetailsActivity : BaseActivity(), ProductDetailsContract.View {

    companion object {
        const val INTENT_BUNDLE_EXTRA_PRODUCT_ID = "productId"
    }

    @Inject
    lateinit var mPresenter: ProductDetailsContract.Presenter

    private val mComponent by lazy { app.component.plus(ProductDetailsModule()) }
    private val mQuestionsAdapter = QuestionsListAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.product_details_activity)
        mComponent.inject(this)
        mPresenter.attachView(this)

        if (!intent.hasExtra(INTENT_BUNDLE_EXTRA_PRODUCT_ID) ||
                intent.getStringExtra(INTENT_BUNDLE_EXTRA_PRODUCT_ID).isNullOrEmpty()) {
            Timber.w("No se paso el id del producto del que se quiere ver el detalle.")
            finish()
        } else {
            val productId = intent.getStringExtra(INTENT_BUNDLE_EXTRA_PRODUCT_ID)

            questionsList.adapter = mQuestionsAdapter
            questionsList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

            questionBtn.setOnClickListener { mPresenter.onQuestionButtonClick() }

            mPresenter.onInit(productId)
        }
    }

    override fun onDestroy() {
        mPresenter.detachView()
        super.onDestroy()
    }

    override fun showProductDetails(productDetails: ProductDetailsContract.ProductDetails) {
        productTitle.text = productDetails.title
        productDesc.text = productDetails.description
        price.text = NumberFormat.getCurrencyInstance().format(productDetails.price)
        units.text = getString(R.string.product_details_units, productDetails.units.toString())
        carousel.setUp(productDetails.images)
        mQuestionsAdapter.setItems(productDetails.questions)
    }

    override fun enableBuyButton(enable: Boolean) = if (enable) buyBtn.show() else buyBtn.hide()

    override fun enableQuestionButton(alert: Boolean) {
        questionBtn.backgroundTintList =
                ColorStateList.valueOf(
                        ContextCompat.getColor(this, if (alert) R.color.red_alert else R.color.colorPrimaryDark)
                )
        questionBtn.show()
    }

    override fun disableQuestionButton() = questionBtn.hide()

    override fun showAskQuestionDialog() {
        val dialogView: View = layoutInflater.inflate(R.layout.product_details_ask_question_dialog, null)
        AlertDialog.Builder(this)
                .setTitle(R.string.product_details_ask_question_title)
                .setView(dialogView)
                .setPositiveButton(R.string.product_details_ask_question_send) { _, _ ->
                    with(dialogView) {
                        questionInputEdit.text?.apply {
                            mPresenter.onSendQuestionClick(toString())
                        }
                    }
                }
                .setNegativeButton(R.string.product_details_ask_question_cancel, null)
                .create()
                .show()
    }

    override fun showSelectQuestionToAnswerDialog(questions: List<ProductDetailsContract.Question>) {
        AlertDialog.Builder(this)
                .setTitle(R.string.product_details_select_question_title)
                .setSingleChoiceItems(questions.map { q -> q.question }.toTypedArray(), -1) { dialog, i ->
                    showAnswerQuestionDialog(questions[i])
                    dialog.dismiss()
                }
                .create()
                .show()
    }

    override fun showError() {
        AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage("Hubo un error...chequea los logsɵ")
                .setOnDismissListener { finish() }
                .create()
                .show()
    }

    private fun showAnswerQuestionDialog(question: ProductDetailsContract.Question) {
        val dialogView: View = layoutInflater.inflate(R.layout.product_details_answer_question_dialog, null)
        AlertDialog.Builder(this)
                .setTitle(R.string.product_details_answer_question_title)
                .setView(dialogView)
                .setPositiveButton(R.string.product_details_answer_question_send) { _, _ ->
                    with(dialogView) {
                        answerInputEdit.text?.apply {
                            mPresenter.onSendAnswerClick(question.id, toString())
                        }
                    }
                }
                .create()
                .show()
    }

}