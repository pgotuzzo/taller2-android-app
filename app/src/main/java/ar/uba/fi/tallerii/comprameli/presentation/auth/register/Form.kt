package ar.uba.fi.tallerii.comprameli.presentation.auth.register

data class Form(val email: String,
                val pass: String,
                val confirmPass: String,
                val name: String,
                val lastName: String)
