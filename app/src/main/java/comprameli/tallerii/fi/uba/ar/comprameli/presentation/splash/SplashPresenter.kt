package comprameli.tallerii.fi.uba.ar.comprameli.presentation.splash

import comprameli.tallerii.fi.uba.ar.comprameli.domain.SessionService
import comprameli.tallerii.fi.uba.ar.comprameli.presentation.base.BasePresenter
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
        // TODO - Remove test with: "Check if user is signed" logic - BEGIN
        val subscription = mSessionService
                .test()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            getView()?.goRegistration()
                        },
                        {
                            Timber.e(it, "Fallo el test del server")
                        }
                )
        mDisposables.add(subscription)
        // TODO - Remove test with: "Check if user is signed" logic - END
    }

}