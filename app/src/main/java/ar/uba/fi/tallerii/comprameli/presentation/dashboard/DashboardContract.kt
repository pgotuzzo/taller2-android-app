package ar.uba.fi.tallerii.comprameli.presentation.dashboard

import ar.uba.fi.tallerii.comprameli.presentation.MvpPresenter

interface DashboardContract {

    data class NavMenuHeader(val avatar: String?,
                             val name: String,
                             val email: String)

    interface View {
        fun refreshNavMenuHeader(navMenuHeader: NavMenuHeader)
        fun showHome()
        fun showMyAccount()
        fun showMall()
        fun showSales()
        fun showPurchases()
        fun goAuth()
        fun goSearch()
        fun goSearchCategory(category: String)
        fun showError()
    }

    interface Presenter : MvpPresenter<View> {
        fun onInit()
        fun onNavigationHomeClick()
        fun onNavigationAccountSettingsClick()
        fun onNavigationMallClick()
        fun onNavigationSearchClick()
        fun onNavigationSalesClick()
        fun onNavigationPurchasesClick()
        fun onNavigationCloseSessionClick()
        fun onProfileChanged()
    }
}