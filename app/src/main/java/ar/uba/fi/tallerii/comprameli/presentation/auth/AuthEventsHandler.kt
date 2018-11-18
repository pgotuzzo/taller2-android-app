package ar.uba.fi.tallerii.comprameli.presentation.auth

import ar.uba.fi.tallerii.comprameli.data.session.FirebaseCredentials

interface AuthEventsHandler {
    fun onCreateAccountClick()
    fun onAuthComplete()
    fun registerFromFacebookLogin(credentials: FirebaseCredentials)
}