package comprameli.tallerii.fi.uba.ar.comprameli.presentation.base

import comprameli.tallerii.fi.uba.ar.comprameli.presentation.MvpPresenter
import timber.log.Timber

open class BasePresenter<V> : MvpPresenter<V> {

    private var mView: V? = null

    init {
        Timber.tag(javaClass.simpleName).d("Created!")
    }

    override fun attachView(view: V) {
        Timber.tag(javaClass.simpleName).d("View attached")
        mView = view
    }

    override fun detachView() {
        mView = null
        onViewDetached()
        Timber.tag(javaClass.simpleName).d("View detached")
    }

    protected fun getView(): V? {
        return mView
    }

    protected open fun onViewDetached() {
        // no op by default
    }

}