package ar.uba.fi.tallerii.comprameli.presentation.auth.signin

import android.content.Intent
import ar.uba.fi.tallerii.comprameli.presentation.MvpPresenter
import com.facebook.AccessToken
import com.facebook.login.widget.LoginButton
import kotlin.reflect.KFunction0

interface SignInContract {
    interface View {
        fun showNextButton(show: Boolean)
        fun notifyUserSigned()
        fun showInvalidCredentialsAlert()
        fun showFacebookAuthenticateFailed()
    }

    interface Presenter : MvpPresenter<View> {
        fun onUserChanged(user: String?)
        fun onPassChanged(pass: String?)
        fun onNextButtonClick()
        fun onLoginFacebookButtonClick(token: AccessToken)
        fun setFacebookLoginBtnBehavior(loginBtn: LoginButton)
        fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    }
}