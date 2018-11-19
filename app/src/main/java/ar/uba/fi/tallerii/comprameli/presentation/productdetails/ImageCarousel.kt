package ar.uba.fi.tallerii.comprameli.presentation.productdetails

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.ImageView
import ar.uba.fi.tallerii.comprameli.R
import ar.uba.fi.tallerii.comprameli.presentation.GlideApp

class ImageCarousel : ViewPager {

    private val mAttrs: AttributeSet?

    constructor(context: Context) : super(context) {
        mAttrs = null
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        mAttrs = attrs
    }

    init {
        if (isInEditMode) {
            adapter = Adapter(listOf(""), true)
        }
    }

    fun setUp(images: List<String>) {
        adapter = Adapter(images, false)
    }

    class Adapter(private val imageUrls: List<String>,
                  private val isEditMode: Boolean) : PagerAdapter() {

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val url = imageUrls[position]
            val params = ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT)
            val imageView = ImageView(container.context)
            imageView.adjustViewBounds = true
            container.addView(imageView, params)
            if (isEditMode) {
                imageView.setImageDrawable(ContextCompat.getDrawable(container.context, R.drawable.search_product_placeholder))
            } else {
                GlideApp.with(container).load(url).centerInside().into(imageView)
            }
            return imageView
        }

        override fun isViewFromObject(view: View, any: Any): Boolean = view == any

        override fun getCount(): Int = imageUrls.size

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {}

    }

}