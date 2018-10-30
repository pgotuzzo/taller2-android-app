package ar.uba.fi.tallerii.comprameli.data.repository

import android.content.Context
import android.content.Context.MODE_PRIVATE
import ar.uba.fi.tallerii.comprameli.data.repository.exception.NonexistentException
import io.reactivex.Completable
import io.reactivex.Single

class AndroidPreferences(private val mContext: Context) : PreferencesMap {

    override fun store(ownerId: String, key: String, value: String): Completable =
            Completable.fromCallable {
                mContext.getSharedPreferences(ownerId, MODE_PRIVATE).edit().putString(key, value).apply()
            }

    override fun getString(ownerId: String, key: String): Single<String> =
            Single.fromCallable {
                val preferences = mContext.getSharedPreferences(ownerId, MODE_PRIVATE)
                if (preferences.contains(key)) {
                    preferences.getString(key, "")
                } else {
                    throw NonexistentException()
                }
            }

    override fun store(ownerId: String, key: String, value: Int): Completable =
            Completable.fromCallable {
                mContext.getSharedPreferences(ownerId, MODE_PRIVATE).edit().putInt(key, value).apply()
            }

    override fun getInt(ownerId: String, key: String): Single<Int> =
            Single.fromCallable {
                val preferences = mContext.getSharedPreferences(ownerId, MODE_PRIVATE)
                if (preferences.contains(key)) {
                    preferences.getInt(key, -1)
                } else {
                    throw NonexistentException()
                }
            }

    override fun store(ownerId: String, key: String, value: Long): Completable =
            Completable.fromCallable {
                mContext.getSharedPreferences(ownerId, MODE_PRIVATE).edit().putLong(key, value).apply()
            }

    override fun getLong(ownerId: String, key: String): Single<Long> =
            Single.fromCallable {
                val preferences = mContext.getSharedPreferences(ownerId, MODE_PRIVATE)
                if (preferences.contains(key)) {
                    preferences.getLong(key, -1L)
                } else {
                    throw NonexistentException()
                }
            }

    override fun store(ownerId: String, key: String, value: Float): Completable =
            Completable.fromCallable {
                mContext.getSharedPreferences(ownerId, MODE_PRIVATE).edit().putFloat(key, value).apply()
            }

    override fun getFloat(ownerId: String, key: String): Single<Float> =
            Single.fromCallable {
                val preferences = mContext.getSharedPreferences(ownerId, MODE_PRIVATE)
                if (preferences.contains(key)) {
                    preferences.getFloat(key, -1f)
                } else {
                    throw NonexistentException()
                }
            }

    override fun store(ownerId: String, key: String, value: Boolean): Completable =
            Completable.fromCallable {
                mContext.getSharedPreferences(ownerId, MODE_PRIVATE).edit().putBoolean(key, value).apply()
            }

    override fun getBoolean(ownerId: String, key: String): Single<Boolean> =
            Single.fromCallable {
                val preferences = mContext.getSharedPreferences(ownerId, MODE_PRIVATE)
                if (preferences.contains(key)) {
                    preferences.getBoolean(key, false)
                } else {
                    throw NonexistentException()
                }
            }

    override fun clear(ownerId: String, key: String): Completable =
            Completable.fromCallable {
                val preferences = mContext.getSharedPreferences(ownerId, MODE_PRIVATE)
                if (preferences.contains(key)) {
                    preferences.edit().remove(key).apply()
                } else {
                    throw NonexistentException()
                }
            }

}