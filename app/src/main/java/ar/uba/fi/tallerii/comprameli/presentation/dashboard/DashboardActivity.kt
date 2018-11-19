package ar.uba.fi.tallerii.comprameli.presentation.dashboard

import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.view.GravityCompat
import android.view.MenuItem
import ar.uba.fi.tallerii.comprameli.R
import ar.uba.fi.tallerii.comprameli.presentation.GlideApp
import ar.uba.fi.tallerii.comprameli.presentation.auth.AuthActivity
import ar.uba.fi.tallerii.comprameli.presentation.base.BaseActivity
import ar.uba.fi.tallerii.comprameli.presentation.dashboard.di.DashboardModule
import ar.uba.fi.tallerii.comprameli.presentation.dashboard.home.HomeEventHandler
import ar.uba.fi.tallerii.comprameli.presentation.dashboard.home.HomeFragment
import ar.uba.fi.tallerii.comprameli.presentation.dashboard.mall.MallFragment
import ar.uba.fi.tallerii.comprameli.presentation.dashboard.profile.ProfileEventsHandler
import ar.uba.fi.tallerii.comprameli.presentation.dashboard.profile.ProfileFragment
import ar.uba.fi.tallerii.comprameli.presentation.publish.PublishActivity
import ar.uba.fi.tallerii.comprameli.presentation.search.SearchActivity
import kotlinx.android.synthetic.main.dashboad_activity.*
import kotlinx.android.synthetic.main.dashboard_nav_header.view.*
import javax.inject.Inject


class DashboardActivity : BaseActivity(), DashboardContract.View, HomeEventHandler,
        ProfileEventsHandler {

    companion object {
        const val TAG_FRAGMENT_HOME = "FragmentHome"
        const val TAG_FRAGMENT_MALL = "FragmentMall"
        const val TAG_FRAGMENT_PROFILE = "FragmentProfile"
    }

    private val mComponent by lazy { app.component.plus((DashboardModule())) }

    @Inject
    lateinit var mPresenter: DashboardContract.Presenter

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
                R.id.myProducts -> mPresenter.onNavigationMyProductsClick()
                R.id.search -> mPresenter.onNavigationSearchClick()
                R.id.closeSession -> mPresenter.onNavigationCloseSessionClick()
            }
            true
        }

        mPresenter.onInit()
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

    override fun refreshNavMenuHeader(navMenuHeader: DashboardContract.NavMenuHeader) {
        with(navMenu.getHeaderView(0)) {
            GlideApp.with(this)
                    .load(navMenuHeader.avatar)
                    .placeholder(R.drawable.ic_user)
                    .circleCrop()
                    .into(avatar)
            name.text = navMenuHeader.name
            email.text = navMenuHeader.email
        }
    }

    override fun showHome() {
        if (supportFragmentManager.findFragmentByTag(TAG_FRAGMENT_HOME) == null) {
            supportFragmentManager
                    .beginTransaction()
                    .replace(mainContainer.id, HomeFragment.getInstance(), TAG_FRAGMENT_HOME)
                    .commit()
        }
    }

    override fun showMyAccount() {
        if (supportFragmentManager.findFragmentByTag(TAG_FRAGMENT_PROFILE) == null) {
            supportFragmentManager
                    .beginTransaction()
                    .replace(mainContainer.id, ProfileFragment.getInstance(), TAG_FRAGMENT_PROFILE)
                    .commit()
        }
    }

    override fun showMyProducts() {
        if (supportFragmentManager.findFragmentByTag(TAG_FRAGMENT_MALL) == null) {
            supportFragmentManager
                    .beginTransaction()
                    .replace(mainContainer.id, MallFragment.getInstance(), TAG_FRAGMENT_MALL)
                    .commit()
        }
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

    override fun showError() {
    }

    override fun onCategorySelected(category: String) {
        goSearchCategory(category.toLowerCase())
    }

    override fun onPublishProduct() {
        startActivity(Intent(this, PublishActivity::class.java))
    }

    override fun onProfileChanged() {
        mPresenter.onProfileChanged()
    }

}