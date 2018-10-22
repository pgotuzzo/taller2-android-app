package comprameli.tallerii.fi.uba.ar.comprameli.presentation.auth.signin

import comprameli.tallerii.fi.uba.ar.comprameli.presentation.MvpPresenter

interface SignInContract {
    interface View {
        fun showNextButton(show: Boolean)
    }

    interface Presenter : MvpPresenter<View> {
        fun onUserChanged(user: String?)
        fun onPassChanged(pass: String?)
    }
}