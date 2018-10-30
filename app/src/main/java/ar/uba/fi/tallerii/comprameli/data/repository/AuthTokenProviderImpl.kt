package ar.uba.fi.tallerii.comprameli.data.repository

import ar.uba.fi.tallerii.comprameli.data.repository.PreferencesMap.Companion.AUTH
import io.reactivex.Completable
import io.reactivex.Single

class AuthTokenProviderImpl(private val mPreferencesMap: PreferencesMap) : AuthTokenProvider {

    companion object {
        const val KEY_AUTH_TOKEN = "auth_token"
    }

    override fun setAuthToken(token: String): Completable =
            mPreferencesMap.store(AUTH, KEY_AUTH_TOKEN, token)

    override fun getAuthToken(): Single<String> =
            mPreferencesMap.getString(AUTH, KEY_AUTH_TOKEN)

    override fun invalidateToken(): Completable =
            mPreferencesMap.clear(AUTH, KEY_AUTH_TOKEN)

    override fun existAuthToken(): Single<Boolean> =
            mPreferencesMap.getString(AUTH, KEY_AUTH_TOKEN).map { true }.onErrorReturn { false }

}