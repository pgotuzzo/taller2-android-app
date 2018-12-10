package ar.uba.fi.tallerii.comprameli.presentation.productdetails

import android.app.AlertDialog
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import ar.uba.fi.tallerii.comprameli.R
import ar.uba.fi.tallerii.comprameli.domain.products.Product
import ar.uba.fi.tallerii.comprameli.presentation.base.BaseActivity
import ar.uba.fi.tallerii.comprameli.presentation.checkout.CheckOutActivity
import ar.uba.fi.tallerii.comprameli.presentation.productdetails.di.ProductDetailsModule
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
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
            buyBtn.setOnClickListener { mPresenter.onBuyButtonClick() }
            qrBtn.setOnClickListener { mPresenter.onQrButtonClick() }

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

    override fun showQR(encode: String) {
        val bitmap = encodeAsBitmap(encode)
        val imageView = ImageView(this)
        imageView.setImageBitmap(bitmap)
        AlertDialog.Builder(this)
                .setView(imageView)
                .create()
                .show()
    }

    override fun showError() {
        AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage("Hubo un error...chequea los logs")
                .setOnDismissListener { finish() }
                .create()
                .show()
    }

    override fun goCheckOut(product: Product) {
        val intent = Intent(this, CheckOutActivity::class.java)
        intent.putExtras(CheckOutActivity.createBundle(product))
        startActivity(intent)
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

    private fun encodeAsBitmap(str: String): Bitmap? {
        val size = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 250f, resources.displayMetrics).toInt()
        val result: BitMatrix
        try {
            result = MultiFormatWriter().encode(str, BarcodeFormat.QR_CODE, size, size, null)
        } catch (iae: IllegalArgumentException) {
            // Unsupported format
            return null
        }

        val w = result.width
        val h = result.height
        val pixels = IntArray(w * h)
        for (y in 0 until h) {
            val offset = y * w
            for (x in 0 until w) {
                pixels[offset + x] = if (result.get(x, y)) Color.BLACK else Color.WHITE
            }
        }
        val bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        bitmap.setPixels(pixels, 0, size, 0, 0, w, h)
        return bitmap
    }

}