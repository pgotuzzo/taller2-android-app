package ar.uba.fi.tallerii.comprameli.presentation.search.filter

import ar.uba.fi.tallerii.comprameli.presentation.MvpPresenter

interface SearchFiltersContract {
    interface View

    interface Presenter : MvpPresenter<View>
}