package ar.uba.fi.tallerii.comprameli.presentation.auth.signin

import android.content.Intent
import ar.uba.fi.tallerii.comprameli.domain.session.SessionService
import ar.uba.fi.tallerii.comprameli.presentation.base.BasePresenter
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.firebase.auth.FacebookAuthProvider
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class SignInPresenter(private val mSessionService: SessionService) :
        BasePresenter<SignInContract.View>(), SignInContract.Presenter {
    private val mCompositeDisposable = CompositeDisposable()
    private var mUser: String? = null
    private var mPass: String? = null
    private lateinit var mCallbackManager: CallbackManager

    override fun onViewDetached() {
        mCompositeDisposable.clear()
        super.onViewDetached()
    }

    override fun onUserChanged(user: String?) {
        mUser = user
        validateCredentials()
    }

    override fun onPassChanged(pass: String?) {
        mPass = pass
        validateCredentials()
    }

    private fun validateCredentials() {
        val valid = !mUser.isNullOrEmpty() && !mPass.isNullOrEmpty()
        getView()?.showNextButton(valid)
    }

    override fun onNextButtonClick() {
        val disposable =
                mSessionService
                        .logIn(mUser!!, mPass!!)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                { getView()?.notifyUserSigned() },
                                {
                                    Timber.e(it)
                                    getView()?.showInvalidCredentialsAlert()
                                }
                        )

        mCompositeDisposable.add(disposable)
    }

    override fun setFacebookLoginBtnBehavior(loginBtn: LoginButton) {

        loginBtn.setReadPermissions("email", "public_profile")

        mCallbackManager = CallbackManager.Factory.create()

        loginBtn.registerCallback(mCallbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                onLoginFacebookButtonClick(loginResult.accessToken)
            }

            override fun onCancel() {
                getView()?.showFacebookAuthenticateFailed()
            }

            override fun onError(error: FacebookException) {
                getView()?.showFacebookAuthenticateFailed()
            }
        })
    }

    override fun onLoginFacebookButtonClick(token: AccessToken) {
        val credential = FacebookAuthProvider.getCredential(token.token)
        val disposable =
                mSessionService
                        .logInWithFacebook(credential)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                { getView()?.notifyUserSigned() },
                                { getView()?.showFacebookAuthenticateFailed() }
                        )

        mCompositeDisposable.add(disposable)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        mCallbackManager.onActivityResult(requestCode, resultCode, data)
    }

}