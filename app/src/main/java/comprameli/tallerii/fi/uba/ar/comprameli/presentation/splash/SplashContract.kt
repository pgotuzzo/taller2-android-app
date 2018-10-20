package comprameli.tallerii.fi.uba.ar.comprameli.presentation.splash

import comprameli.tallerii.fi.uba.ar.comprameli.presentation.MvpPresenter

interface SplashContract {

    interface View {

        fun animateLogo()

        fun goRegistration()

        fun goDashboard()

    }

    interface Presenter : MvpPresenter<View> {

        fun onLayoutInit()

        fun onLogoAnimated()

    }

}