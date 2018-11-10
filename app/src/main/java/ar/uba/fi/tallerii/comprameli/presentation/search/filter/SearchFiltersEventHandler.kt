package ar.uba.fi.tallerii.comprameli.presentation.search.filter

import ar.uba.fi.tallerii.comprameli.model.ProductFilter

interface SearchFiltersEventHandler {
    fun onFilterChanged(filter: ProductFilter)
}