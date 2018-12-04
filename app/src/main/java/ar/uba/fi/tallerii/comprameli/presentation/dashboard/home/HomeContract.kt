package ar.uba.fi.tallerii.comprameli.presentation.dashboard.home

import ar.uba.fi.tallerii.comprameli.presentation.MvpPresenter

interface HomeContract {

    data class Category(val name: String, val image: String)

    interface View {
        fun refreshCarousel(categories: List<Category>)
    }

    interface Presenter : MvpPresenter<View> {
        fun onInit()
    }
}