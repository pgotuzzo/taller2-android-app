package comprameli.tallerii.fi.uba.ar.comprameli.presentation

import android.app.Application
import comprameli.tallerii.fi.uba.ar.comprameli.di.component.ApplicationComponent
import comprameli.tallerii.fi.uba.ar.comprameli.di.component.DaggerApplicationComponent
import timber.log.Timber

class ComprameliApp : Application() {

    val component: ApplicationComponent by lazy {
        DaggerApplicationComponent.builder().build()
    }

    override fun onCreate() {
        super.onCreate()
        // Initialize Timber
        Timber.plant(Timber.DebugTree())
    }

}