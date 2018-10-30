package ar.uba.fi.tallerii.comprameli.data.repository

import android.support.annotation.StringDef
import io.reactivex.Completable
import io.reactivex.Single

interface PreferencesMap {

    companion object {

        @StringDef(SESSION)
        @Retention(AnnotationRetention.SOURCE)
        annotation class OwnerId

        const val AUTH = "Auth"
        const val SESSION = "Session"
    }

    fun store(@OwnerId ownerId: String, key: String, value: String): Completable

    fun getString(@OwnerId ownerId: String, key: String): Single<String>

    fun store(@OwnerId ownerId: String, key: String, value: Int): Completable

    fun getInt(@OwnerId ownerId: String, key: String): Single<Int>

    fun store(@OwnerId ownerId: String, key: String, value: Long): Completable

    fun getLong(@OwnerId ownerId: String, key: String): Single<Long>

    fun store(@OwnerId ownerId: String, key: String, value: Float): Completable

    fun getFloat(@OwnerId ownerId: String, key: String): Single<Float>

    fun store(@OwnerId ownerId: String, key: String, value: Boolean): Completable

    fun getBoolean(@OwnerId ownerId: String, key: String): Single<Boolean>

    fun clear(@OwnerId ownerId: String, key: String): Completable

}