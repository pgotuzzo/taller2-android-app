package ar.uba.fi.tallerii.comprameli.services

import ar.uba.fi.tallerii.comprameli.di.component.ServiceComponent
import ar.uba.fi.tallerii.comprameli.domain.session.SessionService
import ar.uba.fi.tallerii.comprameli.presentation.ComprameliApp
import com.google.firebase.messaging.FirebaseMessagingService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber
import javax.inject.Inject

class CloudMessagingService : FirebaseMessagingService() {

    @Inject
    lateinit var mSessionService: SessionService

    val mComponent: ServiceComponent by lazy { (application as ComprameliApp).component.createService() }

    private val mDisposables = CompositeDisposable()

    override fun onCreate() {
        super.onCreate()
        mComponent.inject(this)
    }

    override fun onNewToken(token: String?) {
        Timber.d("Firebase Cloud Messaging token: $token")
        token?.also { refreshMessagingToken(it) }
    }

    override fun onDestroy() {
        mDisposables.clear()
        super.onDestroy()
    }

    private fun refreshMessagingToken(token: String) {
        val disposable = mSessionService
                .refreshMessagingToken(token)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { Timber.d("Messaging Token actualizado: %s", token) },
                        { throwable -> Timber.e(throwable, "Messaging token no pudo ser actualizado") }
                )
        mDisposables.add(disposable)
    }

}