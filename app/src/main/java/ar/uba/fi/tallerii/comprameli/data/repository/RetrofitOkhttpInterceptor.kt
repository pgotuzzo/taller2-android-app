package ar.uba.fi.tallerii.comprameli.data.repository

import io.reactivex.Single
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody
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
        logRequest(request)

        return logResponse(chain.proceed(request))
    }


    private fun logRequest(request: Request) {
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
    }

    private fun logResponse(response: Response): Response =
            if (!response.isSuccessful) {
                // Error --> Log first
                val responseBody = response.body()
                val responseBodyString = responseBody?.string()

                Timber.d("\nResponse:\ncode: %s\nbody: %s\n",
                        response.code().toString(),
                        responseBodyString
                )
                /*
                 Now we have extracted the response body but in the process we have consumed the original
                 response and can't read it again so we need to build a new one to return from this method
                 */

                val newResponseBody =
                        if (responseBodyString != null)
                            ResponseBody.create(responseBody.contentType(), responseBodyString)
                        else
                            ResponseBody.create(responseBody?.contentType(), "")

                response.newBuilder().body(newResponseBody).build()
            } else {
                response
            }
}