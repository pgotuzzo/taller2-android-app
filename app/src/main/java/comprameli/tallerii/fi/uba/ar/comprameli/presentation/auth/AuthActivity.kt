package comprameli.tallerii.fi.uba.ar.comprameli.presentation.auth

import android.os.Bundle
import comprameli.tallerii.fi.uba.ar.comprameli.R
import comprameli.tallerii.fi.uba.ar.comprameli.presentation.auth.di.AuthModule
import comprameli.tallerii.fi.uba.ar.comprameli.presentation.auth.register.RegisterFragment
import comprameli.tallerii.fi.uba.ar.comprameli.presentation.auth.signin.SignInFragment
import comprameli.tallerii.fi.uba.ar.comprameli.presentation.base.BaseActivity
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
        mPresenter

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

}