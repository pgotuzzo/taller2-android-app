package ar.uba.fi.tallerii.comprameli.presentation.dashboard.purchases.di

import ar.uba.fi.tallerii.comprameli.di.scope.PerFragment
import ar.uba.fi.tallerii.comprameli.domain.orders.OrdersService
import ar.uba.fi.tallerii.comprameli.presentation.dashboard.purchases.PurchasesContract
import ar.uba.fi.tallerii.comprameli.presentation.dashboard.purchases.PurchasesPresenter
import dagger.Module
import dagger.Provides

@Module
class PurchasesModule {
    @PerFragment
    @Provides
    fun providePresenter(ordersService: OrdersService): PurchasesContract.Presenter =
            PurchasesPresenter(ordersService)
}