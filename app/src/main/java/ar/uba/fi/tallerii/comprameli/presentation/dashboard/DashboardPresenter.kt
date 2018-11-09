package ar.uba.fi.tallerii.comprameli.presentation.dashboard

import ar.uba.fi.tallerii.comprameli.domain.profile.ProfileService
import ar.uba.fi.tallerii.comprameli.domain.session.SessionService
import ar.uba.fi.tallerii.comprameli.presentation.base.BasePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class DashboardPresenter(private val mSessionService: SessionService,
                         private val mProfileService: ProfileService) :
        BasePresenter<DashboardContract.View>(), DashboardContract.Presenter {

    private val mCompositeDisposable = CompositeDisposable()

    override fun onViewDetached() {
        super.onViewDetached()
        mCompositeDisposable.clear()
    }

    override fun onInit() {
        val disposable = mProfileService.getProfile()
                .map {
                    DashboardContract.NavMenuHeader(
                            name = String.format("%s %s", it.name, it.surname),
                            email = it.email,
                            avatar = it.avatar
                    )
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            getView()?.apply {
                                refreshNavMenuHeader(it)
                                showHome()
                            }
                        },
                        { getView()?.showError() }
                )
        mCompositeDisposable.add(disposable)
    }

    override fun onNavigationHomeClick() {
        getView()?.showHome()
    }

    override fun onNavigationAccountSettingsClick() {
        getView()?.showMyAccount()
    }

    override fun onNavigationMyProductsClick() {
        getView()?.showMyProducts()
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

    override fun onProfileChanged() {
        val disposable = mProfileService.getProfile()
                .map {
                    DashboardContract.NavMenuHeader(
                            name = String.format("%s %s", it.name, it.surname),
                            email = it.email,
                            avatar = it.avatar
                    )
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { getView()?.apply { refreshNavMenuHeader(it) } },
                        { getView()?.showError() }
                )
        mCompositeDisposable.add(disposable)
    }

}