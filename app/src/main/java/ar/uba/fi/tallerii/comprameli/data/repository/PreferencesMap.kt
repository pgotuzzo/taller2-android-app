package ar.uba.fi.tallerii.comprameli.data.repository

import android.support.annotation.StringDef
import io.reactivex.Completable
import io.reactivex.Observable

interface PreferencesMap {

    companion object {

        @StringDef(SESSION)
        @Retention(AnnotationRetention.SOURCE)
        annotation class OwnerId

        const val SESSION = "Session"
    }

    fun store(@OwnerId ownerId: String, key: String, value: String): Completable

    fun getString(@OwnerId ownerId: String, key: String): Observable<String>

    fun store(@OwnerId ownerId: String, key: String, value: Int): Completable

    fun getInt(@OwnerId ownerId: String, key: String): Observable<Int>

    fun store(@OwnerId ownerId: String, key: String, value: Long): Completable

    fun getLong(@OwnerId ownerId: String, key: String): Observable<Long>

    fun store(@OwnerId ownerId: String, key: String, value: Float): Completable

    fun getFloat(@OwnerId ownerId: String, key: String): Observable<Float>

    fun store(@OwnerId ownerId: String, key: String, value: Boolean): Completable

    fun getBoolean(@OwnerId ownerId: String, key: String): Observable<Boolean>

    fun clear(@OwnerId ownerId: String, key: String): Completable

}