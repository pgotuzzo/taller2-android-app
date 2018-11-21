package ar.uba.fi.tallerii.comprameli.presentation.auth.register

import ar.uba.fi.tallerii.comprameli.data.session.FirebaseCredentials
import ar.uba.fi.tallerii.comprameli.presentation.MvpPresenter
import ar.uba.fi.tallerii.comprameli.domain.profile.Profile

interface RegisterContract {
    interface View {
        fun notifyUserSigned()
        fun showLoginErrorMessage()
        fun showRegisterErrorMessage()
        fun doLogin()
    }

    interface Presenter : MvpPresenter<View> {
        fun registerUser(profile: Profile)
        fun doLogin(user: String, pass: String)
        fun doLogin(credentials: FirebaseCredentials)
    }
}