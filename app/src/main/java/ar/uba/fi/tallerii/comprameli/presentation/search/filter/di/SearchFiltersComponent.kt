package ar.uba.fi.tallerii.comprameli.presentation.search.filter.di

import ar.uba.fi.tallerii.comprameli.di.scope.PerFragment
import ar.uba.fi.tallerii.comprameli.presentation.search.filter.SearchFiltersFragment
import dagger.Subcomponent


@PerFragment
@Subcomponent(modules = [SearchFiltersModule::class])
interface SearchFiltersComponent {
    fun inject(fragment: SearchFiltersFragment)
}