package ar.uba.fi.tallerii.comprameli.presentation.checkout

import android.app.Activity
import android.app.AlertDialog
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import ar.uba.fi.tallerii.comprameli.R
import ar.uba.fi.tallerii.comprameli.domain.products.Product
import ar.uba.fi.tallerii.comprameli.presentation.GlideApp
import ar.uba.fi.tallerii.comprameli.presentation.base.BaseActivity
import ar.uba.fi.tallerii.comprameli.presentation.checkout.CheckOutContract.Companion.CARD
import ar.uba.fi.tallerii.comprameli.presentation.checkout.CheckOutContract.Companion.CASH
import ar.uba.fi.tallerii.comprameli.presentation.checkout.cardform.CheckOutCardFormView
import ar.uba.fi.tallerii.comprameli.presentation.checkout.delivery.DeliveryView
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
            unitsInput.setListener { units -> mPresenter.onUnitsChanged(units) }

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

    override fun showCardDetailsForm(cardsAvailable: List<CheckOutContract.Card>) {
        val dialogView = CheckOutCardFormView(this, null, cardsAvailable)
        val dialog = AlertDialog.Builder(this)
                .setTitle(R.string.check_out_card_dialog_title)
                .setView(dialogView)
                .create()
        dialog.show()
        dialogView.setListener { cardDetails ->
            dialog.dismiss()
            mPresenter.onCardDetailsInput(cardDetails)
        }
    }

    override fun showDelivery(deliveryCost: Float) {
        val dialogView = DeliveryView(this, null)
        dialogView.setPrice(deliveryCost)
        val dialog = AlertDialog.Builder(this)
                .setView(dialogView)
                .setTitle(R.string.check_out_delivery_title)
                .setPositiveButton(R.string.check_out_delivery_confirm) { _, _ -> mPresenter.onDeliveryConfirmed(true) }
                .setNegativeButton(R.string.check_out_delivery_cancel) { _, _ -> mPresenter.onDeliveryConfirmed(false) }
                .create()
        dialog.show()
    }

    override fun showConfirmationDialog() {
        AlertDialog.Builder(this)
                .setTitle(R.string.check_out_confirm_dialog_title)
                .setMessage(R.string.check_out_confirm_dialog_message)
                .setPositiveButton(R.string.check_out_confirm_dialog_positive) { _, _ -> mPresenter.onPaymentConfirmed() }
                .setNegativeButton(R.string.check_out_confirm_dialog_negative) { _, _ -> }
                .create()
                .show()
    }

    override fun showError() {
        AlertDialog.Builder(this)
                .setTitle("Algo salio mal")
                .create()
                .show()
    }

    override fun dismiss() {
        setResult(Activity.RESULT_OK)
        finish()
    }
}