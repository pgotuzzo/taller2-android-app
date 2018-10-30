package ar.uba.fi.tallerii.comprameli.data.repository

import io.reactivex.Single
import okhttp3.Interceptor
import okhttp3.Response
import okio.Buffer
import timber.log.Timber
import java.io.IOException

/**
 * In charge of:
 * <ul>
 *     <li>Logging HTTP requests</li>
 *     <li>Adding Authentication headers to the requests</li>
 * </ul>
 */
class RetrofitOkhttpInterceptor(private val mTokenProvider: AuthTokenProvider) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()

        val singleRequest = mTokenProvider.existAuthToken().flatMap { exists: Boolean ->
            if (!exists) Single.just(request) else {
                mTokenProvider.getAuthToken().map {
                    request.newBuilder()
                            .addHeader("Authorization", "Bearer $it")
                            .build()
                }
            }
        }

        request = singleRequest.blockingGet()

        val body = try {
            val copy = request.newBuilder().build()
            val buffer = Buffer()
            copy.body()?.writeTo(buffer)
            buffer.readUtf8()
        } catch (e: IOException) {
            "I/O exception"
        }

        Timber.d("\nRequest:\nurl: %s\nheader: %s\nbody: %s\n",
                request.url().toString(),
                request.header("Authorization"),
                body
        )

        return chain.proceed(request)
    }

}