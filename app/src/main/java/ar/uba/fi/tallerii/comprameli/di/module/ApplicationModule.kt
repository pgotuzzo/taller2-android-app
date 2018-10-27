package ar.uba.fi.tallerii.comprameli.di.module

import android.content.Context
import ar.uba.fi.tallerii.comprameli.di.scope.PerApplication
import dagger.Module
import dagger.Provides

@Module
class ApplicationModule(private val mContext: Context) {

    @PerApplication
    @Provides
    fun provideApplicationContext(): Context = mContext

}