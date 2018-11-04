package ar.uba.fi.tallerii.comprameli.presentation.dashboard.home.di

import ar.uba.fi.tallerii.comprameli.di.scope.PerFragment
import ar.uba.fi.tallerii.comprameli.presentation.dashboard.home.HomeContract
import ar.uba.fi.tallerii.comprameli.presentation.dashboard.home.HomePresenter
import dagger.Module
import dagger.Provides


@Module
class HomeModule {

    @Provides
    @PerFragment
    fun providePresenter(): HomeContract.Presenter = HomePresenter()

}