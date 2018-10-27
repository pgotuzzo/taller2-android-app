package ar.uba.fi.tallerii.comprameli.data.session

import com.google.gson.annotations.SerializedName

data class LogInBody(
        @SerializedName("idToken")
        private val idToken: String
)