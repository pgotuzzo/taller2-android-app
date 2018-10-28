package ar.uba.fi.tallerii.comprameli.presentation.search.di


import ar.uba.fi.tallerii.comprameli.di.scope.PerActivity
import ar.uba.fi.tallerii.comprameli.presentation.search.SearchContract
import ar.uba.fi.tallerii.comprameli.presentation.search.SearchPresenter
import dagger.Module
import dagger.Provides

@Module
class SearchModule {

    @Provides
    @PerActivity
    fun providePresenter(): SearchContract.Presenter = SearchPresenter()

}