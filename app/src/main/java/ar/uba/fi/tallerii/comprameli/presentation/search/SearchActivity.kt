package ar.uba.fi.tallerii.comprameli.presentation.search

import android.app.AlertDialog
import android.os.Bundle
import android.support.v7.widget.StaggeredGridLayoutManager
import android.support.v7.widget.StaggeredGridLayoutManager.VERTICAL
import ar.uba.fi.tallerii.comprameli.R
import ar.uba.fi.tallerii.comprameli.presentation.base.BaseActivity
import ar.uba.fi.tallerii.comprameli.presentation.search.di.SearchModule
import kotlinx.android.synthetic.main.search_activity.*
import javax.inject.Inject

class SearchActivity : BaseActivity(), SearchContract.View {

    companion object {
        const val SPAN_COUNT = 2
    }

    @Inject
    lateinit var mPresenter: SearchContract.Presenter

    private val mComponent by lazy { app.component.plus(SearchModule()) }
    private val listAdapter = SearchListAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.search_activity)
        mComponent.inject(this)

        mPresenter.attachView(this)

        itemList.layoutManager = StaggeredGridLayoutManager(SPAN_COUNT, VERTICAL)
        itemList.adapter = listAdapter
        itemList.setHasFixedSize(true)

        mPresenter.onInit()
    }

    override fun refreshList(items: List<SearchContract.SearchItem>) {
        listAdapter.setItems(items)
    }

    override fun showError(error: Int) {
        AlertDialog.Builder(this)
                .setTitle(R.string.search_error_products_fetch_title)
                .setMessage(R.string.search_error_products_fetch_message)
                .create()
                .show()
    }

}