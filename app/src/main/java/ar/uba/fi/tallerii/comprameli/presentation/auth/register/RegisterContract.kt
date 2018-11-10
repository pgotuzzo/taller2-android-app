package ar.uba.fi.tallerii.comprameli.presentation.auth.register

import ar.uba.fi.tallerii.comprameli.presentation.MvpPresenter

interface RegisterContract {
    interface View

    interface Presenter : MvpPresenter<View>
}