package ar.uba.fi.tallerii.comprameli.presentation.productdetails

import android.app.AlertDialog
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import ar.uba.fi.tallerii.comprameli.R
import ar.uba.fi.tallerii.comprameli.presentation.base.BaseActivity
import ar.uba.fi.tallerii.comprameli.presentation.productdetails.di.ProductDetailsModule
import kotlinx.android.synthetic.main.product_details_activity.*
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

    override fun showError() {
        AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage("Hubo un error...chequea los logsɵ")
                .setOnDismissListener { finish() }
                .create()
                .show()
    }

}