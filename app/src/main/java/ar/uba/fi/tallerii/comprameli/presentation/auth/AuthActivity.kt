package ar.uba.fi.tallerii.comprameli.presentation.auth

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import ar.uba.fi.tallerii.comprameli.R
import ar.uba.fi.tallerii.comprameli.presentation.auth.di.AuthModule
import ar.uba.fi.tallerii.comprameli.presentation.auth.register.RegisterFragment
import ar.uba.fi.tallerii.comprameli.presentation.auth.signin.SignInFragment
import ar.uba.fi.tallerii.comprameli.presentation.base.BaseActivity
import ar.uba.fi.tallerii.comprameli.presentation.base.BaseFragment
import ar.uba.fi.tallerii.comprameli.presentation.dashboard.DashboardActivity
import kotlinx.android.synthetic.main.auth_activity.*
import javax.inject.Inject

class AuthActivity : BaseActivity(), AuthContract.View, AuthEventsHandler {

    @Inject
    lateinit var mPresenter: AuthContract.Presenter

    private val mComponent by lazy { app.component.plus(AuthModule()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.auth_activity)
        mComponent.inject(this)
        mPresenter.attachView(this)

        if (savedInstanceState == null) {
            // Sign In
            supportFragmentManager
                    .beginTransaction()
                    .add(root.id, SignInFragment.getInstance())
                    .commit()
        }
    }

    override fun onCreateAccountClick() {
        supportFragmentManager
                .beginTransaction()
                .setCustomAnimations(
                        R.animator.frg_transaction_horizontal_enter,
                        R.animator.frg_transaction_horizontal_exit,
                        R.animator.frg_transaction_horizontal_enter_pop,
                        R.animator.frg_transaction_horizontal_exit_pop
                )
                .replace(root.id, RegisterFragment.getInstance())
                .addToBackStack("From Sign in to Register")
                .commit()
    }

    override fun onAuthComplete() {
        startActivity(Intent(this, DashboardActivity::class.java))
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val fragment : MutableList<Fragment> = supportFragmentManager.fragments
        fragment[0].onActivityResult(requestCode, resultCode, data)
    }

}