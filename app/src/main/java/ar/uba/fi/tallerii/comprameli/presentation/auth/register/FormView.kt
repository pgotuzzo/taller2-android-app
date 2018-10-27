package ar.uba.fi.tallerii.comprameli.presentation.auth.register

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.view.View
import ar.uba.fi.tallerii.comprameli.R
import kotlinx.android.synthetic.main.auth_register_mail_form.view.*

class FormView : ConstraintLayout {

    private val mAttrs: AttributeSet?

    constructor(context: Context?) : super(context) {
        mAttrs = null
    }

    constructor(context: Context?, attrs: AttributeSet) : super(context, attrs) {
        mAttrs = attrs
    }

    constructor(context: Context?, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        mAttrs = attrs
    }

    init {
        View.inflate(context, R.layout.auth_register_mail_form, this)
    }

    fun getContent(): Form {
        return Form(
                userInputEdit.text.toString(),
                passInputEdit.text.toString(),
                passConfirmInputEdit.text.toString(),
                nameInputEdit.text.toString(),
                lastNameInputEdit.text.toString()
        )
    }

}