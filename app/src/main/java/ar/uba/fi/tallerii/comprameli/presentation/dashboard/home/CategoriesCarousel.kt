package ar.uba.fi.tallerii.comprameli.presentation.dashboard.home

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT

class CategoriesCarousel : ViewPager {

    private val mAttrs: AttributeSet?

    private var mOnCategoryClickListener: (String) -> Unit = {}

    constructor(context: Context) : super(context) {
        mAttrs = null
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        mAttrs = attrs
    }


    fun setUp(categories: List<HomeContract.Category>, onCategoryClickListener: (String) -> Unit) {
        adapter = Adapter(categories)
        mOnCategoryClickListener = onCategoryClickListener
    }

    inner class Adapter(private val categories: List<HomeContract.Category>) : PagerAdapter() {

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val category = categories[position]
            val params = ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT)
            val categoryView = CategoryView(container.context)
            categoryView.setUp(category.image, category.name) { label -> onCategoryClick(label) }
            container.addView(categoryView, params)
            return categoryView
        }

        override fun isViewFromObject(view: View, any: Any): Boolean = view == any

        override fun getCount(): Int = categories.size

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {}

    }

    private fun onCategoryClick(label: String) {
        mOnCategoryClickListener(label)
    }

}