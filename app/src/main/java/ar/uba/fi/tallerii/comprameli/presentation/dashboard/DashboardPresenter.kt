package ar.uba.fi.tallerii.comprameli.presentation.dashboard

import ar.uba.fi.tallerii.comprameli.domain.SessionService
import ar.uba.fi.tallerii.comprameli.presentation.base.BasePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class DashboardPresenter(private val mSessionService: SessionService) :
        BasePresenter<DashboardContract.View>(), DashboardContract.Presenter {

    private val mCompositeDisposable = CompositeDisposable()

    override fun onViewDetached() {
        super.onViewDetached()
        mCompositeDisposable.clear()
    }

    override fun onNavigationHomeClick() {
        getView()?.showHome()
    }

    override fun onNavigationAccountSettingsClick() {
        getView()?.showMyAccount()
    }

    override fun onNavigationSearchClick() {
        getView()?.goSearch()
    }

    override fun onNavigationCloseSessionClick() {
        Timber.d("Cerrando session")
        val disposable = mSessionService
                .logOut()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    Timber.d("Redireccionando al flujo de Autenticacion")
                    getView()?.goAuth()
                }
        mCompositeDisposable.add(disposable)
    }

    override fun onCategorySelected(category: String) {
        getView()?.goSearchCategory(category.toLowerCase())
    }

}