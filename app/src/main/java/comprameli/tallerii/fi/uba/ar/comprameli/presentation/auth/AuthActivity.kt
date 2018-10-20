package comprameli.tallerii.fi.uba.ar.comprameli.presentation.auth

import android.os.Bundle
import comprameli.tallerii.fi.uba.ar.comprameli.R
import comprameli.tallerii.fi.uba.ar.comprameli.presentation.auth.di.AuthModule
import comprameli.tallerii.fi.uba.ar.comprameli.presentation.base.BaseActivity
import javax.inject.Inject

class AuthActivity : BaseActivity(), AuthContract.View {
    @Inject
    lateinit var mPresenter: AuthContract.Presenter

    private val mComponent by lazy { app.component.plus(AuthModule()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.auth_activity)
        app.component.plus(AuthModule()).inject(this)
    }

}