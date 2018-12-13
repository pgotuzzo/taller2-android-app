package ar.uba.fi.tallerii.comprameli.presentation.dashboard.profile

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import ar.uba.fi.tallerii.comprameli.R
import ar.uba.fi.tallerii.comprameli.presentation.GlideApp
import ar.uba.fi.tallerii.comprameli.presentation.base.BaseFragment
import ar.uba.fi.tallerii.comprameli.presentation.dashboard.profile.di.ProfileModule
import ar.uba.fi.tallerii.comprameli.presentation.utils.SimpleTextWatcher
import kotlinx.android.synthetic.main.dashboard_profile_fragment.*
import timber.log.Timber
import javax.inject.Inject


class ProfileFragment : BaseFragment(), ProfileContract.View {

    private object RequestCode {
        const val GET_IMAGE = 1
    }

    companion object {
        fun getInstance() = ProfileFragment()
    }

    private val mComponent by lazy { app()!!.component.plus(ProfileModule()) }
    private val mGeoCoder by lazy { Geocoder(context) }
    private val mHandler by lazy { Handler() }
    private val mCheckAddressRunnable = Runnable {
        addressInputEdit.text?.apply {
            val address = decodeAddressFromInput(this.toString())
            mPresenter.onAddressChanged(address?.latitude, address?.longitude)
        }
    }

    @Inject
    lateinit var mPresenter: ProfileContract.Presenter
    private var mProfileEventsHandler: ProfileEventsHandler? = null


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try {
            mProfileEventsHandler = context as ProfileEventsHandler
        } catch (e: Exception) {
            Timber.e(e, "Activity must implement ProfileEventsHandler interface")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.dashboard_profile_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mPresenter.attachView(this)
        mPresenter.onInit()

        nameInputEdit.addTextChangedListener(object : SimpleTextWatcher() {
            override fun afterTextChanged(s: Editable?) {
                mPresenter.onNameChanged(s?.toString())
            }
        })
        surnameInputEdit.addTextChangedListener(object : SimpleTextWatcher() {
            override fun afterTextChanged(s: Editable?) {
                mPresenter.onSurnameChanged(s?.toString())
            }
        })
        addressInputEdit.addTextChangedListener(object : SimpleTextWatcher() {
            override fun afterTextChanged(s: Editable?) {
                mHandler.removeCallbacks(mCheckAddressRunnable)
                mHandler.postDelayed(mCheckAddressRunnable, 1000)
            }
        })
        confirmBtn.hide()
        confirmBtn.setOnClickListener {
            mPresenter.onConfirmClick()
        }
        editAvatarBtn.setOnClickListener {
            getImageFromDevice()
        }
    }

    override fun onDestroyView() {
        mPresenter.detachView()
        super.onDestroyView()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RequestCode.GET_IMAGE && resultCode == RESULT_OK) {
            data?.data.let { uri ->
                GlideApp.with(this).load(uri).circleCrop().into(avatar)
                mPresenter.onAvatarChanged(uri.toString())
            }
        }
    }

    override fun refresh(userInfo: ProfileContract.UserInfo) {
        GlideApp.with(this)
                .load(userInfo.avatar)
                .placeholder(R.drawable.ic_user)
                .circleCrop()
                .into(avatar)
        nameInputEdit.setText(userInfo.name)
        surnameInputEdit.setText(userInfo.surname)
        emailInputEdit.setText(userInfo.email)
        addressInputEdit.setText(
                formatAddress(
                        mGeoCoder.getFromLocation(userInfo.latitude, userInfo.longitude, 1)[0]
                )
        )
    }

    override fun showLoader(show: Boolean) {
        progressOverlay.enable(show)
    }

    override fun showSaveButton(show: Boolean) {
        if (show) confirmBtn.show() else confirmBtn.hide()
    }

    override fun enableNameError(enable: Boolean) {
        nameInput.error = if (enable) getString(R.string.dashboard_profile_name_error) else ""
    }

    override fun enableSurnameError(enable: Boolean) {
        surnameInput.error = if (enable) getString(R.string.dashboard_profile_surname_error) else ""
    }

    override fun enableAddressError(enable: Boolean) {
        addressInput.error = if (enable) getString(R.string.dashboard_profile_address_error) else ""
    }

    override fun notifyProfileChanged() {
        mProfileEventsHandler?.onProfileChanged()
    }

    override fun showError() {
        // TODO - Error handling
        Toast.makeText(context, "Hubo un problema!", Toast.LENGTH_SHORT).show()
    }

    private fun getImageFromDevice() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.type = "image/*"
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        startActivityForResult(intent, RequestCode.GET_IMAGE)
    }

    private fun decodeAddressFromInput(addressInput: String?): Address? {
        if (addressInput.isNullOrEmpty())
            return null

        val addresses: List<Address> = mGeoCoder.getFromLocationName(addressInput, 1)
        return if (addresses.isNotEmpty()) {
            val address = addresses[0]
            Timber.d("Latitude: %s, Longitude: %s, Direccion: %s", address.latitude, address.longitude, formatAddress(address))
            address
        } else null
    }

    private fun formatAddress(address: Address): String =
            address.featureName + ", " + address.thoroughfare + ", " + address.locality + ", " + address.adminArea + ", " + address.countryName

}