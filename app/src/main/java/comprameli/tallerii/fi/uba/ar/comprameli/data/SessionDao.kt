package comprameli.tallerii.fi.uba.ar.comprameli.data

import io.reactivex.Observable


interface SessionDao {

    /**
     * Run a test
     *
     * @return true if test was successful, false otherwise
     */
    fun test(): Observable<Boolean>

}