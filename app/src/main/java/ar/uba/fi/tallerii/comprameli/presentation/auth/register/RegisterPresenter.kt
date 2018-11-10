package ar.uba.fi.tallerii.comprameli.presentation.auth.register

import ar.uba.fi.tallerii.comprameli.domain.session.SessionService
import ar.uba.fi.tallerii.comprameli.presentation.base.BasePresenter

class RegisterPresenter(private val mSessionService: SessionService) :
        BasePresenter<RegisterContract.View>(), RegisterContract.Presenter  {

}