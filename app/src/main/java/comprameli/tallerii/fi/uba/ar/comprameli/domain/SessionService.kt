package comprameli.tallerii.fi.uba.ar.comprameli.domain

import io.reactivex.Observable


interface SessionService {

    /**
     * Runs a test
     *
     * @return true if test was successful, false otherwise
     */
    fun test(): Observable<Boolean>

}