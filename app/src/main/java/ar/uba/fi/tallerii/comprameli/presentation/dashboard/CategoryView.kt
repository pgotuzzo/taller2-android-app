package ar.uba.fi.tallerii.comprameli.presentation.dashboard

import android.content.Context
import android.support.annotation.DrawableRes
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.view.View
import ar.uba.fi.tallerii.comprameli.R
import ar.uba.fi.tallerii.comprameli.presentation.GlideApp
import kotlinx.android.synthetic.main.dashboad_category.view.*

class CategoryView : ConstraintLayout {

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
        View.inflate(context, R.layout.dashboad_category, this)
    }

    fun setUp(@DrawableRes drawable: Int, label: String, listener: (String) -> Unit) {
        GlideApp.with(context).load(drawable).centerCrop().into(categoryBg)
        categoryLabel.apply {
            text = label
            setOnClickListener { listener(label) }
        }
    }

}