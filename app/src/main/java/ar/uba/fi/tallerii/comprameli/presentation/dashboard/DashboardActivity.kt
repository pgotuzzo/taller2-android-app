package ar.uba.fi.tallerii.comprameli.presentation.dashboard

import android.content.Intent
import android.os.Bundle
import ar.uba.fi.tallerii.comprameli.R
import ar.uba.fi.tallerii.comprameli.presentation.auth.AuthActivity
import ar.uba.fi.tallerii.comprameli.presentation.base.BaseActivity
import ar.uba.fi.tallerii.comprameli.presentation.dashboard.di.DashboardModule
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

        navMenu.setNavigationItemSelectedListener { menuItem ->
            drawer.closeDrawers()

            when (menuItem.itemId) {
                R.id.home -> mPresenter.onNavigationHomeClick()
                R.id.myAccount -> mPresenter.onNavigationAccountSettingsClick()
                R.id.closeSession -> mPresenter.closeSession()
            }

            true
        }
    }

    override fun showHome() {
    }

    override fun showMyAccount() {
    }

    override fun goAuth() {
        startActivity(Intent(this, AuthActivity::class.java))
        finish()
    }


}