package ar.uba.fi.tallerii.comprameli.presentation.dashboard

import ar.uba.fi.tallerii.comprameli.presentation.MvpPresenter

interface DashboardContract {
    interface View {
        fun showHome()
        fun showMyAccount()
        fun goAuth()
        fun goSearch()
    }

    interface Presenter : MvpPresenter<View> {
        fun onNavigationHomeClick()
        fun onNavigationAccountSettingsClick()
        fun onNavigationSearchClick()
        fun onNavigationCloseSessionClick()
    }
}