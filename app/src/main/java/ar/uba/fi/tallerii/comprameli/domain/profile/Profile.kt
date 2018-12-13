package ar.uba.fi.tallerii.comprameli.domain.profile

data class Profile(val email: String,
                   val name: String,
                   val surname: String,
                   val userId: String,
                   val longitude: Double,
                   val latitude: Double,
                   val facebook: String?,
                   val google: String?,
                   val avatar: String?)