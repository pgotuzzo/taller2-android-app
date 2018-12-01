package ar.uba.fi.tallerii.comprameli.presentation.widget.dialog

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import ar.uba.fi.tallerii.comprameli.R
import kotlinx.android.synthetic.main.widget_card_date_picker.view.*
import java.util.*
import java.util.Calendar.MONTH
import java.util.Calendar.YEAR

class CardDatePickerDialog(context: Context) : AlertDialog(context) {

    var mListener: (date: String) -> Unit = {}
    var mMonthSelected: String = ""
    var mYearSelected: String = ""

    init {
        val view = layoutInflater.inflate(R.layout.widget_card_date_picker, null)
        setView(view)

        with(view) {
            val currentDate = Calendar.getInstance()
            val currentYear = currentDate.get(YEAR)
            val currentMonth = currentDate.get(MONTH) + 1

            yearPicker.minValue = currentYear
            yearPicker.maxValue = currentYear + 20
            yearPicker.wrapSelectorWheel = false
            yearPicker.setOnValueChangedListener { _, _, newVal ->
                monthPicker.minValue = if (newVal == currentYear) currentMonth else 1
                mYearSelected = newVal.toString()
            }
            mYearSelected = yearPicker.value.toString()

            monthPicker.minValue = currentMonth
            monthPicker.maxValue = 12
            monthPicker.value = currentMonth
            monthPicker.wrapSelectorWheel = false
            monthPicker.setOnValueChangedListener { _, _, newVal ->
                mMonthSelected = newVal.toString()
            }
            mMonthSelected = monthPicker.value.toString()
        }

        setButton(
                DialogInterface.BUTTON_POSITIVE,
                context.resources.getString(R.string.widget_card_date_picker_confirm)) { _, _ ->
            onConfirm()
        }
    }

    fun setOnConfirmListener(listener: (date: String) -> Unit) {
        mListener = listener
    }

    private fun onConfirm() {
        mListener.invoke("$mMonthSelected/$mYearSelected")
    }

}