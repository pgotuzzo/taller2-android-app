package ar.uba.fi.tallerii.comprameli.di.module

import android.content.Context
import ar.uba.fi.tallerii.comprameli.BuildConfig
import ar.uba.fi.tallerii.comprameli.data.repository.AndroidPreferences
import ar.uba.fi.tallerii.comprameli.data.repository.AppServerRestApi
import ar.uba.fi.tallerii.comprameli.data.repository.RetrofitOkhttpInterceptor
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

    private val mAppServerRestApi: AppServerRestApi
        get() {
            val okHttpClient = OkHttpClient.Builder()
                    .addInterceptor(RetrofitOkhttpInterceptor())
                    .build()
            return Retrofit.Builder()
                    .baseUrl(BuildConfig.APP_SERVER_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build()
                    .create(AppServerRestApi::class.java)
        }

    private fun getAndroidPreferences(context: Context) = AndroidPreferences(context)

    @Provides
    @PerApplication
    fun provideSessionDao(context: Context): SessionDao =
            SessionDaoImpl(mAppServerRestApi, getAndroidPreferences(context))

}
