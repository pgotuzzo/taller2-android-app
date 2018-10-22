package comprameli.tallerii.fi.uba.ar.comprameli.presentation.auth.register

data class Form(val user: String,
                val pass: String,
                val confirmPass: String,
                val name: String,
                val lastName: String)
