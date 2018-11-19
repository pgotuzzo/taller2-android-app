package ar.uba.fi.tallerii.comprameli.presentation.publish.di

import ar.uba.fi.tallerii.comprameli.di.scope.PerActivity
import ar.uba.fi.tallerii.comprameli.domain.products.ProductsService
import ar.uba.fi.tallerii.comprameli.presentation.publish.PublishContract
import ar.uba.fi.tallerii.comprameli.presentation.publish.PublishPresenter
import dagger.Module
import dagger.Provides


@Module
class PublishModule {

    @Provides
    @PerActivity
    fun providePresenter(productsService: ProductsService): PublishContract.Presenter =
            PublishPresenter(productsService)

}