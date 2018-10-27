package ar.uba.fi.tallerii.comprameli.data.repository

import android.content.Context
import android.content.Context.MODE_PRIVATE
import ar.uba.fi.tallerii.comprameli.data.repository.exception.NonexistentException
import io.reactivex.Completable
import io.reactivex.Observable

class AndroidPreferences(private val mContext: Context) : PreferencesMap {

    override fun store(ownerId: String, key: String, value: String): Completable =
            Completable.fromCallable {
                mContext.getSharedPreferences(ownerId, MODE_PRIVATE).edit().putString(key, value).apply()
                Completable.complete()
            }

    override fun getString(ownerId: String, key: String): Observable<String> =
            Observable.fromCallable {
                val preferences = mContext.getSharedPreferences(ownerId, MODE_PRIVATE)
                if (preferences.contains(key))
                    preferences.getString(key, "")
                else
                    throw NonexistentException()
            }

    override fun store(ownerId: String, key: String, value: Int): Completable =
            Completable.fromCallable {
                mContext.getSharedPreferences(ownerId, MODE_PRIVATE).edit().putInt(key, value).apply()
                Completable.complete()
            }

    override fun getInt(ownerId: String, key: String): Observable<Int> =
            Observable.fromCallable {
                val preferences = mContext.getSharedPreferences(ownerId, MODE_PRIVATE)
                if (preferences.contains(key))
                    preferences.getInt(key, -1)
                else
                    throw NonexistentException()
            }

    override fun store(ownerId: String, key: String, value: Long): Completable =
            Completable.fromCallable {
                mContext.getSharedPreferences(ownerId, MODE_PRIVATE).edit().putLong(key, value).apply()
                Completable.complete()
            }

    override fun getLong(ownerId: String, key: String): Observable<Long> =
            Observable.fromCallable {
                val preferences = mContext.getSharedPreferences(ownerId, MODE_PRIVATE)
                if (preferences.contains(key))
                    preferences.getLong(key, -1L)
                else
                    throw NonexistentException()
            }

    override fun store(ownerId: String, key: String, value: Float): Completable =
            Completable.fromCallable {
                mContext.getSharedPreferences(ownerId, MODE_PRIVATE).edit().putFloat(key, value).apply()
                Completable.complete()
            }

    override fun getFloat(ownerId: String, key: String): Observable<Float> =
            Observable.fromCallable {
                val preferences = mContext.getSharedPreferences(ownerId, MODE_PRIVATE)
                if (preferences.contains(key))
                    preferences.getFloat(key, -1f)
                else
                    throw NonexistentException()
            }

    override fun store(ownerId: String, key: String, value: Boolean): Completable =
            Completable.fromCallable {
                mContext.getSharedPreferences(ownerId, MODE_PRIVATE).edit().putBoolean(key, value).apply()
                Completable.complete()
            }

    override fun getBoolean(ownerId: String, key: String): Observable<Boolean> =
            Observable.fromCallable {
                val preferences = mContext.getSharedPreferences(ownerId, MODE_PRIVATE)
                if (preferences.contains(key))
                    preferences.getBoolean(key, false)
                else
                    throw NonexistentException()
                null
            }

}