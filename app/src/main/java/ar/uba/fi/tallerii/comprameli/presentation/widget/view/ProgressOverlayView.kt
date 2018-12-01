package ar.uba.fi.tallerii.comprameli.presentation.widget.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import ar.uba.fi.tallerii.comprameli.R
import kotlinx.android.synthetic.main.widget_progress_overlay.view.*

class ProgressOverlayView : FrameLayout {

    private val mAttrs: AttributeSet?

    constructor(context: Context) : super(context) {
        mAttrs = null
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        mAttrs = attrs
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        mAttrs = attrs
    }

    init {
        View.inflate(context, R.layout.widget_progress_overlay, this)
        isClickable = true
    }

    fun enable(enable: Boolean) {
        visibility = if (enable) View.VISIBLE else View.GONE
        progressBar.apply { if (enable) show() else hide() }
    }

}