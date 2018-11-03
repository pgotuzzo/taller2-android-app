package ar.uba.fi.tallerii.comprameli.presentation.search.filter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ar.uba.fi.tallerii.comprameli.R
import ar.uba.fi.tallerii.comprameli.presentation.base.BaseFragment
import ar.uba.fi.tallerii.comprameli.presentation.search.filter.di.SearchFiltersModule
import javax.inject.Inject

class SearchFiltersFragment : BaseFragment(), SearchFiltersContract.View {

    companion object {
        fun getInstance(): SearchFiltersFragment {
            return SearchFiltersFragment()
        }
    }

    @Inject
    lateinit var mPresenter: SearchFiltersContract.Presenter

    private val mComponent by lazy { app()!!.component.plus(SearchFiltersModule()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.search_filters_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mPresenter.attachView(this)
    }

    override fun onDestroyView() {
        mPresenter.detachView()
        super.onDestroyView()
    }

}