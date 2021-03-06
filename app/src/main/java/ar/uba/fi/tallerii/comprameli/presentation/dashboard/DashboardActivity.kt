package ar.uba.fi.tallerii.comprameli.presentation.dashboard

import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.view.GravityCompat
import android.view.MenuItem
import ar.uba.fi.tallerii.comprameli.R
import ar.uba.fi.tallerii.comprameli.presentation.GlideApp
import ar.uba.fi.tallerii.comprameli.presentation.scanner.IntentIntegrator
import ar.uba.fi.tallerii.comprameli.presentation.auth.AuthActivity
import ar.uba.fi.tallerii.comprameli.presentation.base.BaseActivity
import ar.uba.fi.tallerii.comprameli.presentation.dashboard.chat.ChatFragment
import ar.uba.fi.tallerii.comprameli.presentation.dashboard.di.DashboardModule
import ar.uba.fi.tallerii.comprameli.presentation.dashboard.home.HomeEventHandler
import ar.uba.fi.tallerii.comprameli.presentation.dashboard.home.HomeFragment
import ar.uba.fi.tallerii.comprameli.presentation.dashboard.mall.MallFragment
import ar.uba.fi.tallerii.comprameli.presentation.dashboard.profile.ProfileEventsHandler
import ar.uba.fi.tallerii.comprameli.presentation.dashboard.profile.ProfileFragment
import ar.uba.fi.tallerii.comprameli.presentation.dashboard.purchases.PurchasesFragment
import ar.uba.fi.tallerii.comprameli.presentation.dashboard.sales.SalesFragment
import ar.uba.fi.tallerii.comprameli.presentation.map.MapActivity
import ar.uba.fi.tallerii.comprameli.presentation.productdetails.ProductDetailsActivity
import ar.uba.fi.tallerii.comprameli.presentation.publish.PublishActivity
import ar.uba.fi.tallerii.comprameli.presentation.search.SearchActivity
import kotlinx.android.synthetic.main.dashboad_activity.*
import kotlinx.android.synthetic.main.dashboard_nav_header.view.*
import javax.inject.Inject


class DashboardActivity : BaseActivity(), DashboardContract.View, HomeEventHandler,
        ProfileEventsHandler, ChatsEventHandler {

    object FragmentTag {
        const val HOME = "FragmentHome"
        const val MALL = "FragmentMall"
        const val PROFILE = "FragmentProfile"
        const val SALES = "FragmentSales"
        const val PURCHASES = "FragmentPurchases"
        const val CHAT = "FragmentChat"
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
                R.id.search -> mPresenter.onNavigationSearchClick()
                R.id.map -> mPresenter.onNavigationMapClick()
                R.id.qr -> mPresenter.onNavigationQRClick()
                R.id.mall -> mPresenter.onNavigationMallClick()
                R.id.sales -> mPresenter.onNavigationSalesClick()
                R.id.purchases -> mPresenter.onNavigationPurchasesClick()
                R.id.closeSession -> mPresenter.onNavigationCloseSessionClick()
            }
            true
        }

        mPresenter.onInit()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent)
        if (scanResult != null) {
            data?.extras?.getString("SCAN_RESULT")?.also {
                mPresenter.onProductIdScanned(it)
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
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
        if (supportFragmentManager.findFragmentByTag(FragmentTag.HOME) == null) {
            supportFragmentManager
                    .beginTransaction()
                    .replace(mainContainer.id, HomeFragment.getInstance(), FragmentTag.HOME)
                    .commit()
        }
    }

    override fun showMyAccount() {
        if (supportFragmentManager.findFragmentByTag(FragmentTag.PROFILE) == null) {
            supportFragmentManager
                    .beginTransaction()
                    .replace(mainContainer.id, ProfileFragment.getInstance(), FragmentTag.PROFILE)
                    .commit()
        }
    }

    override fun showMall() {
        if (supportFragmentManager.findFragmentByTag(FragmentTag.MALL) == null) {
            supportFragmentManager
                    .beginTransaction()
                    .replace(mainContainer.id, MallFragment.getInstance(), FragmentTag.MALL)
                    .commit()
        }
    }

    override fun showSales() {
        if (supportFragmentManager.findFragmentByTag(FragmentTag.SALES) == null) {
            supportFragmentManager
                    .beginTransaction()
                    .replace(mainContainer.id, SalesFragment.getInstance(), FragmentTag.SALES)
                    .commit()
        }
    }

    override fun showPurchases() {
        if (supportFragmentManager.findFragmentByTag(FragmentTag.PURCHASES) == null) {
            supportFragmentManager
                    .beginTransaction()
                    .replace(mainContainer.id, PurchasesFragment.getInstance(), FragmentTag.PURCHASES)
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

    override fun goMap() {
        startActivity(Intent(this, MapActivity::class.java))
    }

    override fun goSearchCategory(category: String) {
        val intent = Intent(this, SearchActivity::class.java)
        intent.putExtra(SearchActivity.INTENT_EXTRA_CATEGORY, category)
        startActivity(intent)
    }

    override fun goScan() {
        val integrator = IntentIntegrator(this)
        integrator.initiateScan()
    }

    override fun goProductDetails(productId: String) {
        val intent = Intent(this, ProductDetailsActivity::class.java)
        intent.putExtra(ProductDetailsActivity.INTENT_BUNDLE_EXTRA_PRODUCT_ID, productId)
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

    override fun onChatSelected(transactionId: String, isCurrentUserOwner: Boolean) {
        if (supportFragmentManager.findFragmentByTag(FragmentTag.CHAT) == null) {
            supportFragmentManager
                    .beginTransaction()
                    .add(mainContainer.id, ChatFragment.getInstance(transactionId, isCurrentUserOwner), FragmentTag.CHAT)
                    .addToBackStack(null)
                    .commit()
        }
    }

}