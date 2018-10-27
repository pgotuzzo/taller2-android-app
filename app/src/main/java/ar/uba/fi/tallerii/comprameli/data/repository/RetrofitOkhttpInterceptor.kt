package ar.uba.fi.tallerii.comprameli.data.repository

import okhttp3.Interceptor
import okhttp3.Response
import okio.Buffer
import timber.log.Timber
import java.io.IOException

/**
 * Logs request to make it traceable and potentially auto-generates access tokens for header
 * authentication
 */
class RetrofitOkhttpInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        /* TODO - Decide if it is going to be necessary to add token handling for header authentication
        Snippet of an example: https://github.com/r7v/Tweetz/blob/master/app/src/main/java/com/rahulrv/tweetz/api/RetrofitInterceptor.java
        ///////////////////////////////////////////////////////////////////////////////////////////
            Token request!
                if (token == null) {
                    val body = chain.proceed(getToken()).body()

                    try {
                        val jsonObject = JSONObject(body.string())
                        token = "Bearer " + jsonObject.optString("access_token")
                    } catch (e: JSONException) {
                        e.printStackTrace()
                        Log.d(RetrofitInterceptor::class.java!!.getName(), "Error fetching token")
                    }

                }

                request = request.newBuilder()
                        .addHeader("Authorization", token)
                        .build()
        ///////////////////////////////////////////////////////////////////////////////////////////
        */

        val body = try {
            val copy = request.newBuilder().build()
            val buffer = Buffer()
            copy.body()?.writeTo(buffer)
            buffer.readUtf8()
        } catch (e: IOException) {
            "I/O exception"
        }

        Timber.d("\nRequest:\nurl: %s\nbody: %s\n",
                request.url().toString(),
                body)

        return chain.proceed(request)
    }

}