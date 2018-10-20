package comprameli.tallerii.fi.uba.ar.comprameli.presentation

interface MvpPresenter<V> {

    fun attachView(view: V)

    fun detachView()

}