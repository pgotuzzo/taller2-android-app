package ar.uba.fi.tallerii.comprameli.data.session

import com.google.gson.annotations.SerializedName

data class Token(
        @SerializedName("token")
        val token: String
)