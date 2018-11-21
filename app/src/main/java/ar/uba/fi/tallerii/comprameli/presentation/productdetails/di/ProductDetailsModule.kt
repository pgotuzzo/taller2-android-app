package ar.uba.fi.tallerii.comprameli.presentation.productdetails.di

import ar.uba.fi.tallerii.comprameli.di.scope.PerActivity
import ar.uba.fi.tallerii.comprameli.domain.products.ProductsService
import ar.uba.fi.tallerii.comprameli.domain.profile.ProfileService
import ar.uba.fi.tallerii.comprameli.presentation.productdetails.ProductDetailsContract
import ar.uba.fi.tallerii.comprameli.presentation.productdetails.ProductDetailsPresenter
import dagger.Module
import dagger.Provides


@Module
class ProductDetailsModule {

    @Provides
    @PerActivity
    fun providePresenter(productsService: ProductsService,
                         profileService: ProfileService): ProductDetailsContract.Presenter =
            ProductDetailsPresenter(productsService, profileService)

}