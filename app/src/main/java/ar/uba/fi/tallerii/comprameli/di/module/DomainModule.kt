package ar.uba.fi.tallerii.comprameli.di.module

import ar.uba.fi.tallerii.comprameli.data.files.FilesDao
import ar.uba.fi.tallerii.comprameli.data.orders.OrdersDao
import ar.uba.fi.tallerii.comprameli.data.products.ProductsDao
import ar.uba.fi.tallerii.comprameli.data.profile.ProfileDao
import ar.uba.fi.tallerii.comprameli.data.repository.AuthTokenProvider
import ar.uba.fi.tallerii.comprameli.data.session.SessionDao
import ar.uba.fi.tallerii.comprameli.di.scope.PerApplication
import ar.uba.fi.tallerii.comprameli.domain.orders.OrdersService
import ar.uba.fi.tallerii.comprameli.domain.orders.OrdersServiceImpl
import ar.uba.fi.tallerii.comprameli.domain.products.ProductsService
import ar.uba.fi.tallerii.comprameli.domain.products.ProductsServiceImpl
import ar.uba.fi.tallerii.comprameli.domain.profile.ProfileService
import ar.uba.fi.tallerii.comprameli.domain.profile.ProfileServiceImpl
import ar.uba.fi.tallerii.comprameli.domain.session.SessionService
import ar.uba.fi.tallerii.comprameli.domain.session.SessionServiceImpl
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
    fun provideProductsService(productsDao: ProductsDao,
                               profileDao: ProfileDao,
                               filesDao: FilesDao): ProductsService =
            ProductsServiceImpl(productsDao, profileDao, filesDao)

    @Provides
    @PerApplication
    fun provideProfileService(profileDao: ProfileDao, filesDao: FilesDao): ProfileService =
            ProfileServiceImpl(profileDao, filesDao)

    @Provides
    @PerApplication
    fun provideOrdersService(ordersDao: OrdersDao, productsDao: ProductsDao): OrdersService =
            OrdersServiceImpl(ordersDao, productsDao)

}