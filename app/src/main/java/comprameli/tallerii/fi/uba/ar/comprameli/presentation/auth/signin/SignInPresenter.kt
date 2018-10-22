package comprameli.tallerii.fi.uba.ar.comprameli.presentation.auth.signin

import comprameli.tallerii.fi.uba.ar.comprameli.presentation.base.BasePresenter

class SignInPresenter : BasePresenter<SignInContract.View>(), SignInContract.Presenter {

    private var mUser: String? = null
    private var mPass: String? = null

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

}