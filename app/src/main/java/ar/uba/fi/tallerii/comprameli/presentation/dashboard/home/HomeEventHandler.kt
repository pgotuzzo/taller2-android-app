package ar.uba.fi.tallerii.comprameli.presentation.dashboard.home

interface HomeEventHandler {
    fun onCategorySelected(category: String)
    fun onPublishProduct()
}