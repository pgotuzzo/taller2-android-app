package ar.uba.fi.tallerii.comprameli.presentation.dashboard.profile

import ar.uba.fi.tallerii.comprameli.presentation.MvpPresenter

interface ProfileContract {

    data class UserInfo(val name: String,
                        val surname: String,
                        val email: String,
                        val longitude: Double,
                        val latitude: Double,
                        val avatar: String?)

    interface View {
        fun refresh(userInfo: UserInfo)
        fun showLoader(show: Boolean)
        fun showSaveButton(show: Boolean)
        fun enableNameError(enable: Boolean)
        fun enableSurnameError(enable: Boolean)
        fun enableAddressError(enable: Boolean)
        fun notifyProfileChanged()
        fun showError()
    }

    interface Presenter : MvpPresenter<View> {
        fun onInit()
        fun onNameChanged(name: String?)
        fun onSurnameChanged(surname: String?)
        fun onAvatarChanged(uri: String)
        fun onAddressChanged(latitude: Double?, longitude: Double?)
        fun onConfirmClick()
    }
}