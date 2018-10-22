package comprameli.tallerii.fi.uba.ar.comprameli.presentation.auth

import comprameli.tallerii.fi.uba.ar.comprameli.presentation.MvpPresenter

interface AuthContract {
    interface View

    interface Presenter : MvpPresenter<View>
}