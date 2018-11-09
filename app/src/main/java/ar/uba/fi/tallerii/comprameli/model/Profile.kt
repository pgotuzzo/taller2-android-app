package ar.uba.fi.tallerii.comprameli.model

data class Profile(val email: String,
                   val name: String,
                   val surname: String,
                   val userId: String,
                   val facebook: String?,
                   val google: String?,
                   val avatar: String?)