package ar.uba.fi.tallerii.comprameli.presentation.auth.signin

import ar.uba.fi.tallerii.comprameli.domain.SessionService
import ar.uba.fi.tallerii.comprameli.presentation.base.BasePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class SignInPresenter(private val mSessionService: SessionService) :
        BasePresenter<SignInContract.View>(), SignInContract.Presenter {
    private val mCompositeDisposable = CompositeDisposable()
    private var mUser: String? = null
    private var mPass: String? = null

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

}