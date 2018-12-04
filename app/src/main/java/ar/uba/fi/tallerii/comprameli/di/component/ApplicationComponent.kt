package ar.uba.fi.tallerii.comprameli.di.component

import ar.uba.fi.tallerii.comprameli.di.module.ApplicationModule
import ar.uba.fi.tallerii.comprameli.di.module.DomainModule
import ar.uba.fi.tallerii.comprameli.di.module.PersistenceModule
import ar.uba.fi.tallerii.comprameli.di.scope.PerApplication
import ar.uba.fi.tallerii.comprameli.presentation.auth.di.AuthComponent
import ar.uba.fi.tallerii.comprameli.presentation.auth.di.AuthModule
import ar.uba.fi.tallerii.comprameli.presentation.auth.register.di.RegisterComponent
import ar.uba.fi.tallerii.comprameli.presentation.auth.register.di.RegisterModule
import ar.uba.fi.tallerii.comprameli.presentation.auth.signin.di.SignInComponent
import ar.uba.fi.tallerii.comprameli.presentation.auth.signin.di.SignInModule
import ar.uba.fi.tallerii.comprameli.presentation.checkout.di.CheckOutComponent
import ar.uba.fi.tallerii.comprameli.presentation.checkout.di.CheckOutModule
import ar.uba.fi.tallerii.comprameli.presentation.dashboard.chat.di.ChatComponent
import ar.uba.fi.tallerii.comprameli.presentation.dashboard.chat.di.ChatModule
import ar.uba.fi.tallerii.comprameli.presentation.dashboard.di.DashboardComponent
import ar.uba.fi.tallerii.comprameli.presentation.dashboard.di.DashboardModule
import ar.uba.fi.tallerii.comprameli.presentation.dashboard.home.di.HomeComponent
import ar.uba.fi.tallerii.comprameli.presentation.dashboard.home.di.HomeModule
import ar.uba.fi.tallerii.comprameli.presentation.dashboard.mall.di.MallComponent
import ar.uba.fi.tallerii.comprameli.presentation.dashboard.mall.di.MallModule
import ar.uba.fi.tallerii.comprameli.presentation.dashboard.profile.di.ProfileComponent
import ar.uba.fi.tallerii.comprameli.presentation.dashboard.profile.di.ProfileModule
import ar.uba.fi.tallerii.comprameli.presentation.dashboard.purchases.di.PurchasesComponent
import ar.uba.fi.tallerii.comprameli.presentation.dashboard.purchases.di.PurchasesModule
import ar.uba.fi.tallerii.comprameli.presentation.dashboard.sales.di.SalesComponent
import ar.uba.fi.tallerii.comprameli.presentation.dashboard.sales.di.SalesModule
import ar.uba.fi.tallerii.comprameli.presentation.productdetails.di.ProductDetailsComponent
import ar.uba.fi.tallerii.comprameli.presentation.productdetails.di.ProductDetailsModule
import ar.uba.fi.tallerii.comprameli.presentation.publish.di.PublishComponent
import ar.uba.fi.tallerii.comprameli.presentation.publish.di.PublishModule
import ar.uba.fi.tallerii.comprameli.presentation.search.di.SearchComponent
import ar.uba.fi.tallerii.comprameli.presentation.search.di.SearchModule
import ar.uba.fi.tallerii.comprameli.presentation.search.filter.di.SearchFiltersComponent
import ar.uba.fi.tallerii.comprameli.presentation.search.filter.di.SearchFiltersModule
import ar.uba.fi.tallerii.comprameli.presentation.splash.di.SplashComponent
import ar.uba.fi.tallerii.comprameli.presentation.splash.di.SplashModule
import dagger.Component

@PerApplication
@Component(modules = [ApplicationModule::class, DomainModule::class, PersistenceModule::class])
interface ApplicationComponent {
    fun plus(module: SplashModule): SplashComponent
    fun plus(module: AuthModule): AuthComponent
    fun plus(module: SignInModule): SignInComponent
    fun plus(module: RegisterModule): RegisterComponent
    fun plus(module: DashboardModule): DashboardComponent
    fun plus(module: HomeModule): HomeComponent
    fun plus(module: MallModule): MallComponent
    fun plus(module: ProfileModule): ProfileComponent
    fun plus(module: SalesModule): SalesComponent
    fun plus(module: PurchasesModule): PurchasesComponent
    fun plus(module: SearchModule): SearchComponent
    fun plus(module: SearchFiltersModule): SearchFiltersComponent
    fun plus(module: ProductDetailsModule): ProductDetailsComponent
    fun plus(module: PublishModule): PublishComponent
    fun plus(module: CheckOutModule): CheckOutComponent
    fun plus(module: ChatModule): ChatComponent
}