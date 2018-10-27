package ar.uba.fi.tallerii.comprameli.presentation

interface MvpPresenter<V> {

    fun attachView(view: V)

    fun detachView()

}