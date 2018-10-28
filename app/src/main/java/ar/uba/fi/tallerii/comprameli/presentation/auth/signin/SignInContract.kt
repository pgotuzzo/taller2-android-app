package ar.uba.fi.tallerii.comprameli.presentation.auth.signin

import ar.uba.fi.tallerii.comprameli.presentation.MvpPresenter

interface SignInContract {
    interface View {
        fun showNextButton(show: Boolean)
        fun notifyUserSigned()
        fun showInvalidCredentialsAlert()
    }

    interface Presenter : MvpPresenter<View> {
        fun onUserChanged(user: String?)
        fun onPassChanged(pass: String?)
        fun onNextButtonClick()
    }
}