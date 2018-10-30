package ar.uba.fi.tallerii.comprameli.di.module

import ar.uba.fi.tallerii.comprameli.data.products.ProductsDao
import ar.uba.fi.tallerii.comprameli.data.repository.AuthTokenProvider
import ar.uba.fi.tallerii.comprameli.data.session.SessionDao
import ar.uba.fi.tallerii.comprameli.di.scope.PerApplication
import ar.uba.fi.tallerii.comprameli.domain.SessionService
import ar.uba.fi.tallerii.comprameli.domain.SessionServiceImpl
import ar.uba.fi.tallerii.comprameli.domain.products.ProductsService
import ar.uba.fi.tallerii.comprameli.domain.products.ProductsServiceImpl
import dagger.Module
import dagger.Provides

@Module
class DomainModule {

    @Provides
    @PerApplication
    fun provideSessionService(sessionDao: SessionDao,
                              authTokenProvider: AuthTokenProvider): SessionService =
            SessionServiceImpl(sessionDao, authTokenProvider)

    @Provides
    @PerApplication
    fun provideProductsService(productsDao: ProductsDao): ProductsService =
            ProductsServiceImpl(productsDao)


}