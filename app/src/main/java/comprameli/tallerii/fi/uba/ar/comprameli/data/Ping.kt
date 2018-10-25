package comprameli.tallerii.fi.uba.ar.comprameli.data

import com.google.gson.annotations.SerializedName

data class Ping(
        @SerializedName("ping")
        private val ping: Int
)