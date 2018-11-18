package ar.uba.fi.tallerii.comprameli.presentation.auth.register

import ar.uba.fi.tallerii.comprameli.data.session.FirebaseCredentials
import ar.uba.fi.tallerii.comprameli.domain.profile.ProfileService
import ar.uba.fi.tallerii.comprameli.domain.session.SessionService
import ar.uba.fi.tallerii.comprameli.presentation.base.BasePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import ar.uba.fi.tallerii.comprameli.model.Profile
import timber.log.Timber

class RegisterPresenter(private val mProfileService: ProfileService, private val mSessionService: SessionService) :
        BasePresenter<RegisterContract.View>(), RegisterContract.Presenter  {

    private val mCompositeDisposable = CompositeDisposable()

    override fun onViewDetached() {
        mCompositeDisposable.clear()
        super.onViewDetached()
    }

    override fun registerUser(profile: Profile) {
        val disposable =
                mProfileService
                        .registerUser(profile)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                {
                                    getView()?.doLogin()
                                },
                                {
                                    Timber.e(it)
                                    getView()?.showLoginErrorMessage()
                                }
                        )

        mCompositeDisposable.add(disposable)
    }

    override fun doLogin(user: String, pass: String) {
        val disposable =
                mSessionService
                        .logIn(user, pass)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                { getView()?.notifyUserSigned() },
                                {
                                    Timber.e(it)
                                    getView()?.showLoginErrorMessage()
                                }
                        )

        mCompositeDisposable.add(disposable)
    }

    override fun doLogin(credentials: FirebaseCredentials) {
            val disposable = mSessionService
                    .logIn(credentials)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            { getView()?.notifyUserSigned() },
                            {
                                Timber.e(it)
                                getView()?.showLoginErrorMessage()
                            }
                    )

            mCompositeDisposable.add(disposable)
    }

}