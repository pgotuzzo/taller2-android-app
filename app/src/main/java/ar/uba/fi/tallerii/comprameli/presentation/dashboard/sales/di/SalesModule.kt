package ar.uba.fi.tallerii.comprameli.presentation.dashboard.sales.di

import ar.uba.fi.tallerii.comprameli.di.scope.PerFragment
import ar.uba.fi.tallerii.comprameli.domain.orders.OrdersService
import ar.uba.fi.tallerii.comprameli.presentation.dashboard.sales.SalesContract
import ar.uba.fi.tallerii.comprameli.presentation.dashboard.sales.SalesPresenter
import dagger.Module
import dagger.Provides


@Module
class SalesModule {

    @Provides
    @PerFragment
    fun providePresenter(ordersService: OrdersService): SalesContract.Presenter =
            SalesPresenter(ordersService)

}