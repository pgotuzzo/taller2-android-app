package ar.uba.fi.tallerii.comprameli.presentation.search.filter

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ar.uba.fi.tallerii.comprameli.R
import ar.uba.fi.tallerii.comprameli.model.ProductFilter
import ar.uba.fi.tallerii.comprameli.presentation.base.BaseFragment
import ar.uba.fi.tallerii.comprameli.presentation.search.filter.SearchFiltersFragment.IntentBundle.KEY_PRODUCT_FILTER
import ar.uba.fi.tallerii.comprameli.presentation.search.filter.di.SearchFiltersModule
import ar.uba.fi.tallerii.comprameli.presentation.widget.list.adapter.CategoryToggleListAdapter
import ar.uba.fi.tallerii.comprameli.presentation.widget.list.adapter.SelectableItem
import kotlinx.android.synthetic.main.search_filters_fragment.*
import timber.log.Timber
import javax.inject.Inject

class SearchFiltersFragment : BaseFragment(), SearchFiltersContract.View {

    companion object {
        fun getInstance(productFilter: ProductFilter): SearchFiltersFragment {
            val arguments = Bundle()
            arguments.putSerializable(KEY_PRODUCT_FILTER, productFilter)
            val fragment = SearchFiltersFragment()
            fragment.arguments = arguments
            return fragment
        }
    }

    object IntentBundle {
        const val KEY_PRODUCT_FILTER = "product_filter"
    }

    object Cons {
        const val SPAN_COUNT = 2
    }

    @Inject
    lateinit var mPresenter: SearchFiltersContract.Presenter
    private var mSearchFiltersEventHandler: SearchFiltersEventHandler? = null

    private val mComponent by lazy { app()!!.component.plus(SearchFiltersModule()) }
    private val mCategoriesAdapter = CategoryToggleListAdapter()
    private val mPaymentMethodsAdapter = CategoryToggleListAdapter()

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try {
            mSearchFiltersEventHandler = context as SearchFiltersEventHandler
        } catch (e: Exception) {
            Timber.e(e, "Activity must implement SearchFiltersEventHandler interface")
            throw e
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.search_filters_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        categoriesList.layoutManager = StaggeredGridLayoutManager(Cons.SPAN_COUNT, StaggeredGridLayoutManager.HORIZONTAL)
        categoriesList.adapter = mCategoriesAdapter
        categoriesList.setHasFixedSize(true)

        paymentMethodsList.layoutManager = StaggeredGridLayoutManager(Cons.SPAN_COUNT, StaggeredGridLayoutManager.HORIZONTAL)
        paymentMethodsList.adapter = mPaymentMethodsAdapter
        paymentMethodsList.setHasFixedSize(true)

        submitBtn.setOnClickListener {
            mPresenter.onSubmitClick(SearchFiltersContract.FiltersFormData(
                    minPrice = minPriceInputEdit.text.toString(),
                    maxPrice = maxPriceInputEdit.text.toString(),
                    minUnits = minUnitsInputEdit.text.toString(),
                    categoriesSelected = mCategoriesAdapter.getItemsSelected(),
                    paymentMethodsSelected = mPaymentMethodsAdapter.getItemsSelected()
            ))
        }

        val filter = arguments?.let {
            if (it.containsKey(KEY_PRODUCT_FILTER))
                it.getSerializable(KEY_PRODUCT_FILTER) as ProductFilter
            else
                null
        }

        mPresenter.attachView(this)
        mPresenter.onInit(filter)
    }

    override fun onDestroyView() {
        mPresenter.detachView()
        super.onDestroyView()
    }

    override fun setPrice(min: Float?, max: Float?) {
        minPriceInputEdit.setText(min?.toString())
        maxPriceInputEdit.setText(max?.toString())
    }

    override fun setUnits(min: Int?) {
        minUnitsInputEdit.setText(min?.toString())
    }

    override fun setCategories(categories: List<SelectableItem>) {
        mCategoriesAdapter.setItems(categories)
    }

    override fun setPaymentMethods(paymentMethods: List<SelectableItem>) {
        mPaymentMethodsAdapter.setItems(paymentMethods)
    }

    override fun notifyFilter(filter: ProductFilter) {
        mSearchFiltersEventHandler!!.onFilterChanged(filter)
    }

}