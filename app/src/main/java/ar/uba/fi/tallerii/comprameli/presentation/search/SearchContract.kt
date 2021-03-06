package ar.uba.fi.tallerii.comprameli.presentation.search

import android.support.annotation.IntDef
import ar.uba.fi.tallerii.comprameli.model.ProductFilter
import ar.uba.fi.tallerii.comprameli.presentation.MvpPresenter

interface SearchContract {

    companion object {

        @IntDef(PRODUCTS_FETCH)
        @Retention(AnnotationRetention.SOURCE)
        annotation class Error

        const val PRODUCTS_FETCH = 0
    }

    data class SearchItem(val id: String,
                          val images: List<String>,
                          val title: String,
                          val price: Float)

    interface View {
        fun refreshList(items: List<SearchItem>)
        fun showEmptyListMessage()
        fun goProductDetails(productId: String)
        fun goFilters(productFilter: ProductFilter)
        fun showLoader(show: Boolean)
        fun showError(@Error error: Int)
    }

    interface Presenter : MvpPresenter<View> {
        fun onInit()
        fun onInit(category: String)
        fun onInit(showOnlyOwnerProducts: Boolean)
        fun onEmptySearch()
        fun onSearchSubmit(query: String?)
        fun onSearchSubmit(productFilter: ProductFilter)
        fun onItemClicked(productId: String)
        fun onFiltersClick()
    }
}