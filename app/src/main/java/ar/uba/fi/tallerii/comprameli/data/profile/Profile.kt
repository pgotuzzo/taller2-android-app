package ar.uba.fi.tallerii.comprameli.data.profile

import ar.uba.fi.tallerii.comprameli.data.Location
import com.google.gson.annotations.SerializedName

data class Profile(@SerializedName("uid")
                   val id: String,
                   @SerializedName("email")
                   val email: String,
                   @SerializedName("name")
                   val name: String,
                   @SerializedName("surname")
                   val surname: String,
                   @SerializedName("location")
                   val location: Location,
                   @SerializedName("facebook")
                   val facebook: String?,
                   @SerializedName("google")
                   val google: String?,
                   @SerializedName("photo")
                   val avatar: String?)
