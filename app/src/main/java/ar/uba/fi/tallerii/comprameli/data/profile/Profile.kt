package ar.uba.fi.tallerii.comprameli.data.profile

import com.google.gson.annotations.SerializedName

data class Profile(@SerializedName("_id")
                   val id: String,
                   @SerializedName("email")
                   val email: String,
                   @SerializedName("name")
                   val name: String,
                   @SerializedName("surname")
                   val surname: String,
                   @SerializedName("facebook")
                   val facebook: String?,
                   @SerializedName("google")
                   val google: String?,
                   @SerializedName("photo")
                   val avatar: String?)
