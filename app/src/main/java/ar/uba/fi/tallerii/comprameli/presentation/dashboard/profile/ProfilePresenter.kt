package ar.uba.fi.tallerii.comprameli.presentation.dashboard.profile

import ar.uba.fi.tallerii.comprameli.domain.profile.ProfileService
import ar.uba.fi.tallerii.comprameli.model.Profile
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
                    ProfileContract.UserInfo(
                            name = it.name,
                            surname = it.surname,
                            email = it.email,
                            avatar = it.avatar
                    )
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

    private fun validateInputs() {
        mNewProfile?.let { profile ->
            getView()?.let { view ->
                view.enableNameError(profile.name.isEmpty())
                view.enableSurnameError(profile.surname.isEmpty())
                view.showSaveButton(
                        profile.name.isNotEmpty() &&
                                profile.surname.isNotEmpty() &&
                                profile != mCurrentProfile
                )
            }
        }
    }

}