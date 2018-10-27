package ar.uba.fi.tallerii.comprameli.presentation.auth

import ar.uba.fi.tallerii.comprameli.presentation.MvpPresenter

interface AuthContract {
    interface View

    interface Presenter : MvpPresenter<View>
}