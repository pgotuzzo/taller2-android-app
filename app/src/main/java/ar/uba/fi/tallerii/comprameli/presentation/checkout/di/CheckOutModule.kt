package ar.uba.fi.tallerii.comprameli.presentation.checkout.di

import ar.uba.fi.tallerii.comprameli.di.scope.PerActivity
import ar.uba.fi.tallerii.comprameli.domain.orders.OrdersService
import ar.uba.fi.tallerii.comprameli.presentation.checkout.CheckOutContract
import ar.uba.fi.tallerii.comprameli.presentation.checkout.CheckOutPresenter
import dagger.Module
import dagger.Provides


@Module
class CheckOutModule {

    @Provides
    @PerActivity
    fun providePresenter(ordersService: OrdersService): CheckOutContract.Presenter =
            CheckOutPresenter(ordersService)

}