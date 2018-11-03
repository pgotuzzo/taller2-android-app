package ar.uba.fi.tallerii.comprameli.presentation.search.filter.di

import ar.uba.fi.tallerii.comprameli.di.scope.PerFragment
import ar.uba.fi.tallerii.comprameli.presentation.search.filter.SearchFiltersContract
import ar.uba.fi.tallerii.comprameli.presentation.search.filter.SearchFiltersPresenter
import dagger.Module
import dagger.Provides


@Module
class SearchFiltersModule {

    @Provides
    @PerFragment
    fun providePresenter(): SearchFiltersContract.Presenter = SearchFiltersPresenter()

}
