package ar.uba.fi.tallerii.comprameli.presentation.splash

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import ar.uba.fi.tallerii.comprameli.R
import ar.uba.fi.tallerii.comprameli.presentation.auth.AuthActivity
import ar.uba.fi.tallerii.comprameli.presentation.base.BaseActivity
import ar.uba.fi.tallerii.comprameli.presentation.dashboard.DashboardActivity
import ar.uba.fi.tallerii.comprameli.presentation.splash.SplashContract.Companion.Error
import ar.uba.fi.tallerii.comprameli.presentation.splash.SplashContract.Companion.SESSION_CHECK
import ar.uba.fi.tallerii.comprameli.presentation.splash.di.SplashModule
import kotlinx.android.synthetic.main.splash_activity.*
import timber.log.Timber
import javax.inject.Inject

class SplashActivity : BaseActivity(), SplashContract.View {

    @Inject
    lateinit var mPresenter: SplashContract.Presenter

    private val mComponent by lazy { app.component.plus(SplashModule()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        Timber.d("Created")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_activity)
        mComponent.inject(this)
        mPresenter.attachView(this)
        // Notify when layout is laid out
        root.addOnLayoutChangeListener(object : View.OnLayoutChangeListener {
            override fun onLayoutChange(v: View?, left: Int, top: Int, right: Int, bottom: Int, oldLeft: Int, oldTop: Int, oldRight: Int, oldBottom: Int) {
                if (root.width > 0 && root.height > 0) {
                    root.removeOnLayoutChangeListener(this)
                    mPresenter.onLayoutInit()
                }
            }
        })
    }

    override fun onDestroy() {
        mPresenter.detachView()
        super.onDestroy()
        Timber.d("Destroyed")
    }

    override fun animateLogo() {
        val animation = logo
                .animate()
                .rotationY(720f)
                .translationX((root.width / 2 - logo.width / 2).toFloat() - logo.x)
                .translationY((root.height / 2 - logo.height / 2).toFloat() - logo.y)
                .scaleX(2f)
                .scaleY(2f)
        animation.duration = 1000
        animation.withEndAction { mPresenter.onLogoAnimated() }
        animation.start()
    }

    override fun goRegistration() {
        startActivity(Intent(this, AuthActivity::class.java))
        finish()
    }

    override fun goDashboard() {
        startActivity(Intent(this, DashboardActivity::class.java))
        finish()
    }

    override fun showError(@Error error: Int) {
        AlertDialog.Builder(this)
                .setTitle(R.string.splash_error_session_check_title)
                .setMessage(R.string.splash_error_session_check_message)
                .setOnDismissListener { mPresenter.onErrorDismissed(SESSION_CHECK) }
                .create()
                .show()
    }

    override fun dismiss() {
        finish()
    }

}