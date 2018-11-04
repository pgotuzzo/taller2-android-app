package ar.uba.fi.tallerii.comprameli.presentation.dashboard.home

import android.content.Context
import android.support.annotation.DrawableRes
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import ar.uba.fi.tallerii.comprameli.R

class CategoriesCarousel : ViewPager {

    data class Category(val label: String,
                        @DrawableRes
                        val drawable: Int)

    private val mAttrs: AttributeSet?

    private var mOnCategoryClickListener: (String) -> Unit = {}

    constructor(context: Context) : super(context) {
        mAttrs = null
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        mAttrs = attrs
    }

    init {
        if (isInEditMode) {
            adapter = Adapter(listOf(Category("Tecnologia", R.drawable.category_technology)))
        }
    }

    fun setUp(categories: List<Category>, onCategoryClickListener: (String) -> Unit) {
        adapter = Adapter(categories)
        mOnCategoryClickListener = onCategoryClickListener
    }

    inner class Adapter(private val categories: List<Category>) : PagerAdapter() {

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val category = categories[position]
            val params = ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT)
            val categoryView = CategoryView(container.context)
            categoryView.setUp(category.drawable, category.label, mOnCategoryClickListener)
            container.addView(categoryView, params)
            return categoryView
        }

        override fun isViewFromObject(view: View, any: Any): Boolean = view == any

        override fun getCount(): Int = categories.size

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {}

    }

}