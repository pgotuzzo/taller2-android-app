package ar.uba.fi.tallerii.comprameli.presentation.checkout

import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import ar.uba.fi.tallerii.comprameli.R
import ar.uba.fi.tallerii.comprameli.domain.products.Product
import ar.uba.fi.tallerii.comprameli.presentation.GlideApp
import ar.uba.fi.tallerii.comprameli.presentation.base.BaseActivity
import ar.uba.fi.tallerii.comprameli.presentation.checkout.CheckOutContract.Companion.CARD
import ar.uba.fi.tallerii.comprameli.presentation.checkout.CheckOutContract.Companion.CASH
import ar.uba.fi.tallerii.comprameli.presentation.checkout.di.CheckOutModule
import kotlinx.android.synthetic.main.check_out_activity.*
import timber.log.Timber
import java.text.NumberFormat
import javax.inject.Inject

class CheckOutActivity : BaseActivity(), CheckOutContract.View {

    companion object {
        fun createBundle(product: Product): Bundle {
            val bundle = Bundle()
            bundle.putParcelable(BundleExtras.PRODUCT, product)
            return bundle
        }
    }

    private object BundleExtras {
        const val PRODUCT = "product"
    }

    @Inject
    lateinit var mPresenter: CheckOutContract.Presenter

    private val mComponent by lazy { app.component.plus(CheckOutModule()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.check_out_activity)
        mComponent.inject(this)
        mPresenter.attachView(this)

        if (!intent.hasExtra(BundleExtras.PRODUCT) ||
                intent.getParcelableExtra<Product>(BundleExtras.PRODUCT) == null) {
            Timber.w("No se paso el producto que se quiere comprar.")
            finish()
        } else {
            val product = intent.getParcelableExtra<Product>(BundleExtras.PRODUCT)

            productTitle.text = product.title
            GlideApp.with(this)
                    .load(product.images[0])
                    .placeholder(R.drawable.search_product_placeholder)
                    .circleCrop()
                    .into(productImg)
            productPrice.text = getString(
                    R.string.check_out_price,
                    NumberFormat.getCurrencyInstance().format(product.price)
            )

            cashBtn.setOnClickListener {
                cashBtn.isSelected = true
                cardBtn.isSelected = false
                mPresenter.onPaymentTypeSelected(CASH)
            }
            cardBtn.setOnClickListener {
                cashBtn.isSelected = false
                cardBtn.isSelected = true
                mPresenter.onPaymentTypeSelected(CARD)
            }
            nextBtn.setOnClickListener {
                mPresenter.onNextButtonClick()
            }
            unitsInput.setMin(1)

            mPresenter.onInit(product)
        }
    }

    override fun onDestroy() {
        mPresenter.detachView()
        super.onDestroy()
    }

    override fun setMaxUnits(max: Int) {
        unitsInput.setMax(max)
    }

    override fun enablePayment(paymentType: Int, enable: Boolean) {
        when (paymentType) {
            CASH -> cashBtn
            CARD -> cardBtn
            else -> null
        }?.apply {
            this.visibility = if (enable) VISIBLE else GONE
        }
    }

    override fun showSinglePaymentMessage(paymentType: Int) {
        paymentLabel.text = getString(R.string.check_out_payment_selection_unique)
        when (paymentType) {
            CASH -> {
                cashBtn.visibility = VISIBLE
                cashBtn.isSelected = true
                cardBtn.visibility = GONE
            }
            CARD -> {
                cardBtn.visibility = VISIBLE
                cardBtn.isSelected = true
                cashBtn.visibility = GONE
            }
        }
    }

    override fun showNextBtn() {
        nextBtn.show()
    }
}