package ar.uba.fi.tallerii.comprameli.presentation.splash

import android.support.annotation.IntDef
import ar.uba.fi.tallerii.comprameli.presentation.MvpPresenter

interface SplashContract {

    companion object {

        @IntDef(SESSION_CHECK)
        @Retention(AnnotationRetention.SOURCE)
        annotation class Error

        const val SESSION_CHECK = 0
    }

    interface View {

        fun animateLogo()

        fun goRegistration()

        fun goDashboard()

        fun showError(@Error error: Int)

        fun dismiss()

    }

    interface Presenter : MvpPresenter<View> {

        fun onLayoutInit()

        fun onLogoAnimated()

        fun onErrorDismissed(@Error error: Int)

    }

}