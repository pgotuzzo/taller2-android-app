package ar.uba.fi.tallerii.comprameli.presentation.dashboard

import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.view.GravityCompat
import android.view.MenuItem
import ar.uba.fi.tallerii.comprameli.R
import ar.uba.fi.tallerii.comprameli.presentation.auth.AuthActivity
import ar.uba.fi.tallerii.comprameli.presentation.base.BaseActivity
import ar.uba.fi.tallerii.comprameli.presentation.dashboard.di.DashboardModule
import ar.uba.fi.tallerii.comprameli.presentation.search.SearchActivity
import kotlinx.android.synthetic.main.dashboad_activity.*
import javax.inject.Inject


class DashboardActivity : BaseActivity(), DashboardContract.View {

    @Inject
    lateinit var mPresenter: DashboardContract.Presenter

    private val mComponent by lazy { app.component.plus((DashboardModule())) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dashboad_activity)
        mComponent.inject(this)
        mPresenter.attachView(this)

        // Tool Bar & Action Bar
        toolbar.title = getString(R.string.app_name)
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white))
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_menu)
        }
        // Navigation Menu
        navMenu.setNavigationItemSelectedListener { menuItem ->
            drawer.closeDrawers()
            when (menuItem.itemId) {
                R.id.home -> mPresenter.onNavigationHomeClick()
                R.id.myAccount -> mPresenter.onNavigationAccountSettingsClick()
                R.id.search -> mPresenter.onNavigationSearchClick()
                R.id.closeSession -> mPresenter.onNavigationCloseSessionClick()
            }
            true
        }
        // Categories
        categoriesCarousel.setUp(
                listOf(
                        CategoriesCarousel.Category("Tecnologia", R.drawable.category_technology),
                        CategoriesCarousel.Category("Auto", R.drawable.category_cars),
                        CategoriesCarousel.Category("Ropa", R.drawable.category_clothes)
                )
        ) { mPresenter.onCategorySelected(it) }
    }

    override fun onDestroy() {
        mPresenter.detachView()
        super.onDestroy()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                drawer.openDrawer(GravityCompat.START)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun showHome() {
    }

    override fun showMyAccount() {
    }

    override fun goAuth() {
        startActivity(Intent(this, AuthActivity::class.java))
        finish()
    }

    override fun goSearch() {
        startActivity(Intent(this, SearchActivity::class.java))
    }

    override fun goSearchCategory(category: String) {
        val intent = Intent(this, SearchActivity::class.java)
        intent.putExtra(SearchActivity.INTENT_EXTRA_CATEGORY, category)
        startActivity(intent)
    }

}