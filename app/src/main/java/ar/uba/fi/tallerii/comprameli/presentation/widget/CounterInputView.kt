package ar.uba.fi.tallerii.comprameli.presentation.widget

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.text.Editable
import android.util.AttributeSet
import android.view.View
import ar.uba.fi.tallerii.comprameli.R
import ar.uba.fi.tallerii.comprameli.presentation.utils.SimpleTextWatcher
import kotlinx.android.synthetic.main.widget_counter_input.view.*

class CounterInputView(context: Context, attrs: AttributeSet) : ConstraintLayout(context, attrs) {

    private var mMin: Int? = null
    private var mMax: Int? = null

    init {
        // Inflate view
        View.inflate(context, R.layout.widget_counter_input, this)
        // Style attributes
        var labelText: String? = null
        // Attributes set up
        context.theme.obtainStyledAttributes(
                attrs,
                R.styleable.CounterInputView,
                0,
                0
        ).apply {
            try {
                labelText = getString(R.styleable.CounterInputView_text)
                mMin = getInt(R.styleable.CounterInputView_min, 0)
            } finally {
                recycle()
            }
        }
        // Customize using attributes
        label.text = labelText
        mMin?.apply { input.setText(this.toString()) }
        input.addTextChangedListener(object : SimpleTextWatcher() {
            override fun afterTextChanged(s: Editable?) {
                val current = s?.toString()?.toIntOrNull()
                // Null check
                current?.apply {
                    if (this <= 0) {
                        // Min check
                        input.setText("1")
                    } else if (mMax != null && mMax!! < this) {
                        // Max check
                        input.setText(mMax!!.toString())
                    }
                }
            }
        })
        plusBtn.setOnClickListener {
            val currentValue = input.text?.toString()?.toIntOrNull()
            val updateValue: Int = when {
                currentValue == null -> 1
                mMax == null || mMax!! > currentValue -> currentValue + 1
                else -> currentValue
            }
            input.setText(updateValue.toString())
        }
        minusBtn.setOnClickListener {
            val currentValue = input.text?.toString()?.toIntOrNull()
            val updateValue: Int = when {
                currentValue == null -> 0
                mMin == null || mMin!! < currentValue -> currentValue - 1
                else -> currentValue
            }
            input.setText(updateValue.toString())
        }
    }

    fun setMax(maxLimit: Int?) {
        mMax = maxLimit
        input.text?.toString()?.toIntOrNull()?.apply {
            if (mMax != null && this > mMax!!) {
                input.setText(mMax!!.toString())
            }
        }
    }

    fun setMin(minLimit: Int?) {
        mMin = minLimit
        input.text?.toString()?.toIntOrNull()?.apply {
            if (mMin != null && this < mMin!!) {
                input.setText(mMin!!.toString())
            }
        }
    }

}