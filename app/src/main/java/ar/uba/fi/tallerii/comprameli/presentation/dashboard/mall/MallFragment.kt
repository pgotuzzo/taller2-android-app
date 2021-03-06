package ar.uba.fi.tallerii.comprameli.presentation.dashboard.mall

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ar.uba.fi.tallerii.comprameli.R
import ar.uba.fi.tallerii.comprameli.model.ProductFilter
import ar.uba.fi.tallerii.comprameli.presentation.base.BaseFragment
import ar.uba.fi.tallerii.comprameli.presentation.dashboard.PublishEventHandler
import ar.uba.fi.tallerii.comprameli.presentation.dashboard.mall.di.MallModule
import ar.uba.fi.tallerii.comprameli.presentation.productdetails.ProductDetailsActivity
import ar.uba.fi.tallerii.comprameli.presentation.search.SearchContract
import ar.uba.fi.tallerii.comprameli.presentation.search.SearchListAdapter
import kotlinx.android.synthetic.main.dashboard_mall_fragment.*
import timber.log.Timber
import javax.inject.Inject

class MallFragment : BaseFragment(), SearchContract.View {

    companion object {
        fun getInstance(): MallFragment {
            return MallFragment()
        }
    }

    @Inject
    lateinit var mPresenter: SearchContract.Presenter
    private lateinit var mPublishEventHandler: PublishEventHandler

    private val mComponent by lazy { app()!!.component.plus(MallModule()) }
    private val mListAdapter = SearchListAdapter()

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try {
            mPublishEventHandler = context as PublishEventHandler
        } catch (e: Exception) {
            Timber.e(e, "Activity must implement Publish Event Handler interface")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dashboard_mall_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mPresenter.attachView(this)

        // List - Recycler
        itemList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        itemList.adapter = mListAdapter
        itemList.setHasFixedSize(true)

        // Add Button
        addBtn.setOnClickListener { mPublishEventHandler.onPublishProduct() }

        mPresenter.onInit(true)
    }

    override fun onDestroyView() {
        mPresenter.detachView()
        super.onDestroyView()
    }

    override fun refreshList(items: List<SearchContract.SearchItem>) {
        showListOfProducts(true)
        mListAdapter.setItems(items) { mPresenter.onItemClicked(it) }
    }

    override fun showEmptyListMessage() {
        showListOfProducts(false)
    }

    override fun goProductDetails(productId: String) {
        val intent = Intent(context, ProductDetailsActivity::class.java)
        intent.putExtra(ProductDetailsActivity.INTENT_BUNDLE_EXTRA_PRODUCT_ID, productId)
        startActivity(intent)
    }

    override fun goFilters(productFilter: ProductFilter) {
        // No op yet
    }

    override fun showLoader(show: Boolean) {
        progressOverlay.enable(show)
    }

    override fun showError(error: Int) {
        AlertDialog.Builder(context)
                .setTitle(R.string.search_error_products_fetch_title)
                .setMessage(R.string.search_error_products_fetch_message)
                .create()
                .show()
    }

    private fun showListOfProducts(show: Boolean) {
        notFoundImg.visibility = if (show) View.GONE else View.VISIBLE
        notFoundLabel.visibility = if (show) View.GONE else View.VISIBLE
        itemList.visibility = if (show) View.VISIBLE else View.GONE
    }

}