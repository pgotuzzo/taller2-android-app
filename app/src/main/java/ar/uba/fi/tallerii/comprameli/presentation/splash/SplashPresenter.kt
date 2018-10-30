package ar.uba.fi.tallerii.comprameli.presentation.splash

import ar.uba.fi.tallerii.comprameli.domain.SessionService
import ar.uba.fi.tallerii.comprameli.presentation.base.BasePresenter
import ar.uba.fi.tallerii.comprameli.presentation.splash.SplashContract.Companion.Error
import ar.uba.fi.tallerii.comprameli.presentation.splash.SplashContract.Companion.SESSION_CHECK
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class SplashPresenter(private val mSessionService: SessionService) :
        BasePresenter<SplashContract.View>(), SplashContract.Presenter {

    private val mDisposables: CompositeDisposable = CompositeDisposable()

    override fun onViewDetached() {
        Timber.d("Stop any in progress processing")
        mDisposables.clear()
    }

    override fun onLayoutInit() {
        getView()?.apply { animateLogo() }
    }

    override fun onLogoAnimated() {
        val subscription = mSessionService
                .isSessionActive()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { isSessionActive ->
                            getView()?.apply {
                                if (isSessionActive) {
                                    Timber.d("Session existente. No hace falta autenticar. Redireccionando al Dashboard")
                                    goDashboard()
                                } else {
                                    Timber.d("Session inexistente. Redireccionando al flujo de Autenticacion")
                                    goRegistration()
                                }
                            }
                        },
                        {
                            Timber.e(it, "Fallo el checkeo de la session")
                            getView()?.showError(SESSION_CHECK)
                        }
                )
        mDisposables.add(subscription)
    }

    override fun onErrorDismissed(@Error error: Int) {
        getView()?.dismiss()
    }

}