package ar.uba.fi.tallerii.comprameli.presentation.dashboard.purchases

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import ar.uba.fi.tallerii.comprameli.R
import ar.uba.fi.tallerii.comprameli.presentation.dashboard.purchases.PurchasesContract.Companion.NEGATIVE
import ar.uba.fi.tallerii.comprameli.presentation.dashboard.purchases.PurchasesContract.Companion.NEUTRAL
import ar.uba.fi.tallerii.comprameli.presentation.dashboard.purchases.PurchasesContract.Companion.POSITIVE
import kotlinx.android.synthetic.main.dashboard_purchases_rate.view.*

class RateView(context: Context, attrs: AttributeSet?) : ConstraintLayout(context, attrs) {

    private var mListener: (String) -> Unit = {}

    init {
        inflate(context, R.layout.dashboard_purchases_rate, this)
        positive.setOnClickListener { mListener.invoke(POSITIVE) }
        neutral.setOnClickListener { mListener.invoke(NEUTRAL) }
        negative.setOnClickListener { mListener.invoke(NEGATIVE) }
    }

    fun setOnClickListener(listener: (String) -> Unit) {
        mListener = listener
    }

}