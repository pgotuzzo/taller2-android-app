package comprameli.tallerii.fi.uba.ar.comprameli.presentation.splash

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import comprameli.tallerii.fi.uba.ar.comprameli.R
import comprameli.tallerii.fi.uba.ar.comprameli.presentation.auth.AuthActivity
import comprameli.tallerii.fi.uba.ar.comprameli.presentation.base.BaseActivity
import comprameli.tallerii.fi.uba.ar.comprameli.presentation.splash.di.SplashModule
import kotlinx.android.synthetic.main.splash_activity.*
import timber.log.Timber
import javax.inject.Inject

class SplashActivity : BaseActivity(), SplashContract.View {

    @Inject
    lateinit var mPresenter: SplashContract.Presenter

    private val mComponent by lazy { app.component.plus(SplashModule()) }
    private val mRootLayoutListener: View.OnLayoutChangeListener =
            View.OnLayoutChangeListener { _, _, _, _, _, _, _, _, _ -> onRootLayoutChange() }

    override fun onCreate(savedInstanceState: Bundle?) {
        Timber.d("Created")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_activity)
        mComponent.inject(this)
        mPresenter.attachView(this)
        // Notify when layout is laid out
        root.addOnLayoutChangeListener(mRootLayoutListener)
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
        Toast.makeText(this, "Not implemented yet!", Toast.LENGTH_SHORT).show()
    }

    private fun onRootLayoutChange() {
        if (root.width > 0 && root.height > 0) {
            root.removeOnLayoutChangeListener(mRootLayoutListener)
            mPresenter.onLayoutInit()
        }
    }

}