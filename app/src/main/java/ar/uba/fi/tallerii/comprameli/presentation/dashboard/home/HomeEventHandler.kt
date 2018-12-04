package ar.uba.fi.tallerii.comprameli.presentation.dashboard.home

import ar.uba.fi.tallerii.comprameli.presentation.dashboard.PublishEventHandler

interface HomeEventHandler : PublishEventHandler {
    fun onCategorySelected(category: String)
}