package ar.uba.fi.tallerii.comprameli.presentation.search

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.SearchView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.support.v7.widget.StaggeredGridLayoutManager.VERTICAL
import android.view.Menu
import android.view.View.GONE
import android.view.View.VISIBLE
import ar.uba.fi.tallerii.comprameli.R
import ar.uba.fi.tallerii.comprameli.presentation.base.BaseActivity
import ar.uba.fi.tallerii.comprameli.presentation.productdetails.ProductDetailsActivity
import ar.uba.fi.tallerii.comprameli.presentation.search.di.SearchModule
import ar.uba.fi.tallerii.comprameli.presentation.search.filter.SearchFiltersFragment
import kotlinx.android.synthetic.main.search_activity.*
import javax.inject.Inject


class SearchActivity : BaseActivity(), SearchContract.View {

    companion object {
        const val SPAN_COUNT = 2
        const val TAG_FRAGMENT_FILTERS = "FragmentFilters"
    }

    @Inject
    lateinit var mPresenter: SearchContract.Presenter

    private val mComponent by lazy { app.component.plus(SearchModule()) }
    private val listAdapter = SearchListAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.search_activity)
        mComponent.inject(this)

        // Toolbar
        toolbar.title = getString(R.string.app_name)
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white))
        setSupportActionBar(toolbar)
        // List - Recycler
        itemList.layoutManager = StaggeredGridLayoutManager(SPAN_COUNT, VERTICAL)
        itemList.adapter = listAdapter
        itemList.setHasFixedSize(true)

        mPresenter.attachView(this)
        mPresenter.onInit()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search, menu)
        // Search view
        val searchView: SearchView? = menu?.findItem(R.id.search)?.actionView as SearchView
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(text: String?): Boolean {
                if (text.isNullOrEmpty()) {
                    mPresenter.onEmptySearch()
                }
                return false
            }

            override fun onQueryTextSubmit(text: String?): Boolean {
                mPresenter.onSearchSubmit(text)
                return true
            }
        })
        // Filter
        menu.findItem(R.id.filter)?.setOnMenuItemClickListener {
            if (supportFragmentManager.findFragmentByTag(TAG_FRAGMENT_FILTERS) != null) {
                supportFragmentManager.popBackStack()
            } else {
                supportFragmentManager
                        .beginTransaction()
                        .setCustomAnimations(
                                R.animator.frg_transaction_vertical_enter, 0,
                                0, R.animator.frg_transaction_vertical_exit
                        )
                        .add(filterContainer.id, SearchFiltersFragment.getInstance(), TAG_FRAGMENT_FILTERS)
                        .addToBackStack("Show Filters")
                        .commit()
            }
            true
        }
        return true
    }

    override fun refreshList(items: List<SearchContract.SearchItem>) {
        showListOfProducts(true)
        listAdapter.setItems(items) { mPresenter.onItemClicked(it) }
    }

    override fun showEmptyListMessage() {
        showListOfProducts(false)
    }

    override fun goProductDetails(productId: String) {
        val intent = Intent(this, ProductDetailsActivity::class.java)
        intent.putExtra(ProductDetailsActivity.INTENT_BUNDLE_EXTRA_PRODUCT_ID, productId)
        startActivity(intent)
    }

    override fun showError(error: Int) {
        AlertDialog.Builder(this)
                .setTitle(R.string.search_error_products_fetch_title)
                .setMessage(R.string.search_error_products_fetch_message)
                .create()
                .show()
    }

    private fun showListOfProducts(show: Boolean) {
        notFoundImg.visibility = if (show) GONE else VISIBLE
        notFoundLabel.visibility = if (show) GONE else VISIBLE
        itemList.visibility = if (show) VISIBLE else GONE
    }

}