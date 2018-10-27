package ar.uba.fi.tallerii.comprameli.presentation

import android.app.Application
import ar.uba.fi.tallerii.comprameli.di.component.ApplicationComponent
import ar.uba.fi.tallerii.comprameli.di.component.DaggerApplicationComponent
import ar.uba.fi.tallerii.comprameli.di.module.ApplicationModule
import timber.log.Timber

class ComprameliApp : Application() {

    val component: ApplicationComponent by lazy {
        DaggerApplicationComponent.builder()
                .applicationModule(ApplicationModule(this))
                .build()
    }

    override fun onCreate() {
        super.onCreate()
        // Initialize Timber
        Timber.plant(Timber.DebugTree())
    }

}