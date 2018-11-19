package ar.uba.fi.tallerii.comprameli.presentation.dashboard.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ar.uba.fi.tallerii.comprameli.R
import ar.uba.fi.tallerii.comprameli.presentation.base.BaseFragment
import ar.uba.fi.tallerii.comprameli.presentation.dashboard.home.di.HomeModule
import kotlinx.android.synthetic.main.dashboard_home_fragment.*
import timber.log.Timber
import javax.inject.Inject

class HomeFragment : BaseFragment(), HomeContract.View {

    companion object {
        fun getInstance(): HomeFragment {
            return HomeFragment()
        }
    }

    @Inject
    lateinit var mPresenter: HomeContract.Presenter

    private val mComponent by lazy { app()!!.component.plus(HomeModule()) }

    private var mHomeEventHandler: HomeEventHandler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mComponent.inject(this)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try {
            mHomeEventHandler = context as HomeEventHandler
        } catch (e: Exception) {
            Timber.e(e, "Activity must implement Home Event Handler interface")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dashboard_home_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mPresenter.attachView(this)

        // Categories
        categoriesCarousel.setUp(
                listOf(
                        CategoriesCarousel.Category("Tecnologia", R.drawable.category_technology),
                        CategoriesCarousel.Category("Auto", R.drawable.category_cars),
                        CategoriesCarousel.Category("Ropa", R.drawable.category_clothes)
                )
        ) { mHomeEventHandler!!.onCategorySelected(it) }

        // Publish Button
        publishBtn.setOnClickListener { mHomeEventHandler!!.onPublishProduct() }
    }

    override fun onDestroyView() {
        mPresenter.detachView()
        super.onDestroyView()
    }

}