package ar.uba.fi.tallerii.comprameli.presentation.search

import ar.uba.fi.tallerii.comprameli.presentation.MvpPresenter

interface SearchContract {

    data class SearchItem(val id: Int,
                          val images: List<String>,
                          val title: String,
                          val price: Float)

    interface View {
        fun refreshList(items: List<SearchItem>)
    }

    interface Presenter : MvpPresenter<View> {
        fun onInit()
    }
}