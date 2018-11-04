package ar.uba.fi.tallerii.comprameli.presentation.dashboard.mall.di

import ar.uba.fi.tallerii.comprameli.di.scope.PerFragment
import ar.uba.fi.tallerii.comprameli.presentation.dashboard.mall.MallFragment
import dagger.Subcomponent


@PerFragment
@Subcomponent(modules = [MallModule::class])
interface MallComponent {
    fun inject(fragment: MallFragment)
}