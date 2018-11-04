package ar.uba.fi.tallerii.comprameli.presentation.dashboard.home

import ar.uba.fi.tallerii.comprameli.presentation.MvpPresenter

interface HomeContract {
    interface View
    interface Presenter : MvpPresenter<View>
}