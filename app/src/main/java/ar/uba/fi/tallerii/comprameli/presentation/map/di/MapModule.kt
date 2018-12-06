package ar.uba.fi.tallerii.comprameli.presentation.map.di

import ar.uba.fi.tallerii.comprameli.di.scope.PerActivity
import ar.uba.fi.tallerii.comprameli.domain.products.ProductsService
import ar.uba.fi.tallerii.comprameli.presentation.map.MapContract
import ar.uba.fi.tallerii.comprameli.presentation.map.MapPresenter
import dagger.Module
import dagger.Provides


@Module
class MapModule {

    @Provides
    @PerActivity
    fun providePresenter(productsService: ProductsService): MapContract.Presenter =
            MapPresenter(productsService)

}