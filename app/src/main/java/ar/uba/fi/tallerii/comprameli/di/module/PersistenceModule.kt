package ar.uba.fi.tallerii.comprameli.di.module

import android.content.Context
import ar.uba.fi.tallerii.comprameli.BuildConfig
import ar.uba.fi.tallerii.comprameli.data.files.FilesDao
import ar.uba.fi.tallerii.comprameli.data.files.FilesDaoImpl
import ar.uba.fi.tallerii.comprameli.data.orders.OrdersDao
import ar.uba.fi.tallerii.comprameli.data.orders.OrdersDaoImpl
import ar.uba.fi.tallerii.comprameli.data.products.ProductsDao
import ar.uba.fi.tallerii.comprameli.data.products.ProductsDaoImpl
import ar.uba.fi.tallerii.comprameli.data.profile.ProfileDao
import ar.uba.fi.tallerii.comprameli.data.profile.ProfileDaoImpl
import ar.uba.fi.tallerii.comprameli.data.repository.*
import ar.uba.fi.tallerii.comprameli.data.session.SessionDao
import ar.uba.fi.tallerii.comprameli.data.session.SessionDaoImpl
import ar.uba.fi.tallerii.comprameli.di.scope.PerApplication
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


@Module
class PersistenceModule {

    @Provides
    @PerApplication
    fun providePreferencesMap(context: Context): PreferencesMap = AndroidPreferences(context)

    @Provides
    @PerApplication
    fun provideAuthTokenProvider(preferences: PreferencesMap): AuthTokenProvider =
            AuthTokenProviderImpl(preferences)

    @Provides
    @PerApplication
    fun provideAppServerRestApi(authTokenProvider: AuthTokenProvider): AppServerRestApi {
        val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(RetrofitOkhttpInterceptor(authTokenProvider))
                .build()
        return Retrofit.Builder()
                .baseUrl(BuildConfig.APP_SERVER_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(AppServerRestApi::class.java)
    }

    @Provides
    @PerApplication
    fun provideSessionDao(appServerRestApi: AppServerRestApi,
                          preferences: PreferencesMap): SessionDao =
            SessionDaoImpl(appServerRestApi, preferences)

    @Provides
    @PerApplication
    fun provideProductsDao(appServerRestApi: AppServerRestApi): ProductsDao =
            ProductsDaoImpl(appServerRestApi)

    @Provides
    @PerApplication
    fun provideProfileDao(appServerRestApi: AppServerRestApi): ProfileDao =
            ProfileDaoImpl(appServerRestApi)

    @Provides
    @PerApplication
    fun provideFilesDao(): FilesDao = FilesDaoImpl()

    @Provides
    @PerApplication
    fun provideOrdersDao(appServerRestApi: AppServerRestApi): OrdersDao =
            OrdersDaoImpl(appServerRestApi)


}
