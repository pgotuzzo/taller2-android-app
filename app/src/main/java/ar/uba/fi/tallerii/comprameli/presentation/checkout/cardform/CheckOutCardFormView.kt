package ar.uba.fi.tallerii.comprameli.presentation.checkout.cardform

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.LinearLayoutManager.HORIZONTAL
import android.text.Editable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import ar.uba.fi.tallerii.comprameli.R
import ar.uba.fi.tallerii.comprameli.presentation.checkout.CardDetails
import ar.uba.fi.tallerii.comprameli.presentation.checkout.CheckOutContract
import ar.uba.fi.tallerii.comprameli.presentation.utils.RegexUtils
import ar.uba.fi.tallerii.comprameli.presentation.utils.SimpleTextWatcher
import ar.uba.fi.tallerii.comprameli.presentation.widget.dialog.CardDatePickerDialog
import kotlinx.android.synthetic.main.check_out_card_form.view.*
import java.util.regex.Pattern


class CheckOutCardFormView(context: Context, attrs: AttributeSet?, cards: List<CheckOutContract.Card>) :
        ConstraintLayout(context, attrs) {

    private val mCardListAdapter = CardsListAdapter(cards)
    private val mCardDatePickerDialog: CardDatePickerDialog = CardDatePickerDialog(context)
    private var mCardNumber: String = ""
    private var mCardHolder: String = ""
    private var mSecurityCode: String = ""
    private var mExpirationDate: String = ""

    init {
        // Inflate view
        View.inflate(context, R.layout.check_out_card_form, this)
        val paddingPx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20f, resources.displayMetrics).toInt()
        setPadding(paddingPx, paddingPx, paddingPx, paddingPx)
        // Card Type
        cardList.layoutManager = LinearLayoutManager(getContext(), HORIZONTAL, false)
        cardList.adapter = mCardListAdapter
        // Card Number
        val expectedPattern: Pattern = Pattern.compile(RegexUtils.CARD_NUMBER)
        cardNumberInputEdit.addTextChangedListener(object : SimpleTextWatcher() {
            override fun afterTextChanged(s: Editable?) {
                s?.apply {
                    val matcher = expectedPattern.matcher(this)
                    if (matcher.find()) {
                        mCardNumber = toString().replace(" ", "")
                    } else {
                        mCardNumber = ""
                        // disable filters temporally
                        val filters = s.filters
                        s.filters = arrayOf()
                        // reformat input
                        val current = toString()
                        val expected = reformatCardNumber(current)
                        if (expected != current) {
                            s.clear()
                            s.append(expected)
                        }
                        // enable filters
                        s.filters = filters
                    }
                }
                validateInputs()
            }
        })
        // Card Holder
        nameInputEdit.addTextChangedListener(object : SimpleTextWatcher() {
            override fun afterTextChanged(s: Editable?) {
                s?.apply { mCardHolder = toString() }
                validateInputs()
            }
        })
        // Security Code - CVV
        cvvInputEdit.addTextChangedListener(object : SimpleTextWatcher() {
            override fun afterTextChanged(s: Editable?) {
                s?.apply {
                    mSecurityCode = if (this.length >= 3) {
                        delete(3, length)
                        substring(0..2)
                    } else {
                        ""
                    }
                }
                validateInputs()
            }
        })
        // Expiration Date
        mCardDatePickerDialog.setOnConfirmListener { date ->
            mExpirationDate = date
            expirationDateInputEdit.setText(mExpirationDate)
            validateInputs()
        }
        mCardDatePickerDialog.setOnCancelListener {
            mExpirationDate = ""
            validateInputs()
        }
        expirationDateInputEdit.setOnTouchListener { _, _ ->
            if (!mCardDatePickerDialog.isShowing) {
                mCardDatePickerDialog.show()
            }
            true
        }
        // Confirm Button
        confirmBtn.visibility = View.INVISIBLE
    }

    fun setListener(listener: (form: CardDetails) -> Unit) {
        confirmBtn.setOnClickListener {
            listener.invoke(
                    CardDetails(
                            cardName = mCardListAdapter.getSelection(),
                            cardNumber = mCardNumber,
                            cardHolder = mCardHolder,
                            securityCode = mSecurityCode,
                            expirationDate = mExpirationDate
                    )
            )
        }
    }

    private fun validateInputs() {
        confirmBtn.visibility =
                if (!mCardNumber.isEmpty() && !mCardHolder.isEmpty() && !mSecurityCode.isEmpty() && !mExpirationDate.isEmpty())
                    View.VISIBLE
                else
                    View.GONE
    }

    fun reformatCardNumber(current: String): String {
        val currentNoSpans = current.replace(" ", "")
        val spans = currentNoSpans.length / 4
        var formatted = ""
        for (i in 1..spans) {
            val bottom = (i - 1) * 4
            val top = bottom + 4
            formatted += currentNoSpans.substring(bottom, top)
            if (top != currentNoSpans.length)
                formatted += " "
        }
        formatted += currentNoSpans.substring(currentNoSpans.length - currentNoSpans.length % 4, currentNoSpans.length)
        return if (formatted.length > 19) formatted.substring(0, 19) else formatted
    }

}