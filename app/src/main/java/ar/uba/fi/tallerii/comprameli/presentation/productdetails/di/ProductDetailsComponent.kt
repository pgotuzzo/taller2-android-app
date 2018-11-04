package ar.uba.fi.tallerii.comprameli.presentation.productdetails.di

import ar.uba.fi.tallerii.comprameli.di.scope.PerActivity
import ar.uba.fi.tallerii.comprameli.presentation.productdetails.ProductDetailsActivity
import dagger.Subcomponent


@PerActivity
@Subcomponent(modules = [ProductDetailsModule::class])
interface ProductDetailsComponent {
    fun inject(activity: ProductDetailsActivity)
}