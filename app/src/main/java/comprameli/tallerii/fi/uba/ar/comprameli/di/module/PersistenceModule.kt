package comprameli.tallerii.fi.uba.ar.comprameli.di.module

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import com.pgotuzzo.llavero.di.scope.PerApplication
import comprameli.tallerii.fi.uba.ar.comprameli.BuildConfig
import comprameli.tallerii.fi.uba.ar.comprameli.data.SessionDao
import comprameli.tallerii.fi.uba.ar.comprameli.data.SessionDaoImpl
import comprameli.tallerii.fi.uba.ar.comprameli.data.repository.AppServerRestApi
import comprameli.tallerii.fi.uba.ar.comprameli.data.repository.RetrofitOkhttpInterceptor
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

    @Provides
    @PerApplication
    fun provideSessionDao(): SessionDao = SessionDaoImpl(mAppServerRestApi)
}
