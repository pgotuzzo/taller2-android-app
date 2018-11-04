package ar.uba.fi.tallerii.comprameli.presentation.dashboard.mall.di

import ar.uba.fi.tallerii.comprameli.di.scope.PerFragment
import ar.uba.fi.tallerii.comprameli.domain.products.ProductsService
import ar.uba.fi.tallerii.comprameli.domain.profile.ProfileService
import ar.uba.fi.tallerii.comprameli.presentation.search.SearchContract
import ar.uba.fi.tallerii.comprameli.presentation.search.SearchPresenter
import dagger.Module
import dagger.Provides

@Module
class MallModule {

    @Provides
    @PerFragment
    fun providePresenter(productsService: ProductsService,
                         profileService: ProfileService): SearchContract.Presenter =
            SearchPresenter(productsService, profileService)

}