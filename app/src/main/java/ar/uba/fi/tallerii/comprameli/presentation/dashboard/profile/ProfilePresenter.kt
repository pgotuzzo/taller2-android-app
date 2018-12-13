package ar.uba.fi.tallerii.comprameli.presentation.dashboard.profile

import ar.uba.fi.tallerii.comprameli.domain.profile.Profile
import ar.uba.fi.tallerii.comprameli.domain.profile.ProfileService
import ar.uba.fi.tallerii.comprameli.presentation.base.BasePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class ProfilePresenter(private val mProfileService: ProfileService) :
        BasePresenter<ProfileContract.View>(), ProfileContract.Presenter {

    private val mDisposables = CompositeDisposable()

    private var mCurrentProfile: Profile? = null
    private var mNewProfile: Profile? = null
    private var mUpdateAvatarPending: Boolean = false
    private var mIsLocationChanged: Boolean = false
    private var mShowLocationError: Boolean = false

    override fun onViewDetached() {
        mDisposables.clear()
        super.onViewDetached()
    }

    override fun onInit() {
        val disposable = mProfileService
                .getProfile()
                .map {
                    mCurrentProfile = it
                    mNewProfile = it
                    fromProfile(it)
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            getView()?.refresh(it)
                            getView()?.showSaveButton(false)
                        },
                        { throwable ->
                            Timber.e(throwable, "Error al intentar obtener los datos del perfil de usuario")
                            getView()?.showError()
                        }
                )
        mDisposables.add(disposable)
    }

    override fun onNameChanged(name: String?) {
        mNewProfile = mNewProfile?.copy(name = if (name.isNullOrEmpty()) "" else name!!)
        validateInputs()
    }

    override fun onSurnameChanged(surname: String?) {
        mNewProfile = mNewProfile?.copy(surname = if (surname.isNullOrEmpty()) "" else surname!!)
        validateInputs()
    }

    override fun onAvatarChanged(uri: String) {
        mUpdateAvatarPending = true
        mNewProfile = mNewProfile?.copy(avatar = uri)
        validateInputs()
    }

    override fun onAddressChanged(latitude: Double?, longitude: Double?) {
        mShowLocationError = latitude == null || longitude == null
        mIsLocationChanged = !(mCurrentProfile!!.longitude == longitude && mCurrentProfile!!.latitude == latitude)
        if (mIsLocationChanged && !mShowLocationError) {
            mNewProfile = mNewProfile?.copy(latitude = latitude!!, longitude = longitude!!)
        }
        validateInputs()
    }

    override fun onConfirmClick() {
        getView()?.apply {
            showSaveButton(false)
            showLoader(true)
        }

        val singleProfile = mNewProfile?.let {
            if (mUpdateAvatarPending) {
                mProfileService.updateProfile(it, it.avatar!!)
            } else {
                mProfileService.updateProfile(it).toSingle { it }
            }
        }

        val disposable = singleProfile?.run {
            subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            { profile ->
                                mCurrentProfile = profile
                                mNewProfile = profile
                                mUpdateAvatarPending = false
                                getView()?.apply {
                                    showLoader(false)
                                    getView()?.refresh(fromProfile(profile))
                                    notifyProfileChanged()
                                }
                            },
                            { throwable ->
                                Timber.e(throwable)
                                getView()?.showError()
                            }
                    )
        }

        disposable?.also { mDisposables.add(it) }
    }

    private fun fromProfile(profile: Profile) =
            ProfileContract.UserInfo(
                    name = profile.name,
                    surname = profile.surname,
                    email = profile.email,
                    avatar = profile.avatar,
                    longitude = profile.longitude,
                    latitude = profile.latitude
            )

    private fun validateInputs() {
        mNewProfile?.let { profile ->
            getView()?.let { view ->
                view.enableNameError(profile.name.isEmpty())
                view.enableSurnameError(profile.surname.isEmpty())
                view.enableAddressError(mShowLocationError)
                view.showSaveButton(
                        profile.name.isNotEmpty() &&
                                profile.surname.isNotEmpty() &&
                                !mShowLocationError &&
                                profile != mCurrentProfile
                )
            }
        }
    }

}