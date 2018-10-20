package comprameli.tallerii.fi.uba.ar.comprameli.presentation.splash

import comprameli.tallerii.fi.uba.ar.comprameli.presentation.base.BasePresenter
import timber.log.Timber

class SplashPresenter : BasePresenter<SplashContract.View>(), SplashContract.Presenter {

    override fun onViewDetached() {
        Timber.d("Stop any in progress processing")
    }

    override fun onLayoutInit() {
        getView()?.apply { animateLogo() }
    }

    override fun onLogoAnimated() {
        // Check if user is signed
        getView()?.apply { goRegistration() }
    }

}