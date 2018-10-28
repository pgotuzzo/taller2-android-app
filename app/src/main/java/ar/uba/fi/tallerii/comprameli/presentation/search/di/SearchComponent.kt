package ar.uba.fi.tallerii.comprameli.presentation.search.di


import ar.uba.fi.tallerii.comprameli.di.scope.PerActivity
import ar.uba.fi.tallerii.comprameli.presentation.search.SearchActivity
import dagger.Subcomponent

@PerActivity
@Subcomponent(modules = [SearchModule::class])
interface SearchComponent {
    fun inject(activity: SearchActivity)
}