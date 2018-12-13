package ar.uba.fi.tallerii.comprameli.presentation.checkout.delivery

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.view.View
import ar.uba.fi.tallerii.comprameli.R
import kotlinx.android.synthetic.main.check_out_delivery.view.*
import java.text.NumberFormat

class DeliveryView(context: Context, attrs: AttributeSet?) : ConstraintLayout(context, attrs) {

    init {
        // Inflate view
        View.inflate(context, R.layout.check_out_delivery, this)
    }

    fun setPrice(value: Float) {
        price.text = NumberFormat.getCurrencyInstance().format(value)
    }

}