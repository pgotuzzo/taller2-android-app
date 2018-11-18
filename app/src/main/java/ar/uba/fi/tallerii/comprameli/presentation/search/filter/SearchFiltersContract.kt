package ar.uba.fi.tallerii.comprameli.presentation.search.filter

import ar.uba.fi.tallerii.comprameli.model.ProductFilter
import ar.uba.fi.tallerii.comprameli.presentation.MvpPresenter

interface SearchFiltersContract {

    data class SelectableItem(val label: String,
                              val selected: Boolean)

    data class FiltersFormData(val minPrice: String?,
                               val maxPrice: String?,
                               val minUnits: String?,
                               val categoriesSelected: List<String>,
                               val paymentMethodsSelected: List<String>)

    interface View {
        fun setPrice(min: Float?, max: Float?)
        fun setUnits(min: Int?)
        fun setCategories(categories: List<SelectableItem>)
        fun setPaymentMethods(paymentMethods: List<SelectableItem>)
        fun notifyFilter(filter: ProductFilter)
    }

    interface Presenter : MvpPresenter<View> {
        fun onInit(productFilter: ProductFilter?)
        fun onSubmitClick(filtersFormData: FiltersFormData)
    }
}