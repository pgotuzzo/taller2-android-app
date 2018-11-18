package ar.uba.fi.tallerii.comprameli.presentation.auth.signin

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ar.uba.fi.tallerii.comprameli.R
import ar.uba.fi.tallerii.comprameli.presentation.auth.AuthEventsHandler
import ar.uba.fi.tallerii.comprameli.presentation.auth.signin.di.SignInModule
import ar.uba.fi.tallerii.comprameli.presentation.base.BaseFragment
import ar.uba.fi.tallerii.comprameli.presentation.utils.SimpleTextWatcher
import kotlinx.android.synthetic.main.auth_sign_in_fragment.*
import timber.log.Timber
import javax.inject.Inject
import android.content.Intent
import ar.uba.fi.tallerii.comprameli.data.session.FirebaseCredentials


class SignInFragment : BaseFragment(), SignInContract.View {

    companion object {
        fun getInstance(): SignInFragment {
            return SignInFragment()
        }
    }

    @Inject
    lateinit var mPresenter: SignInContract.Presenter

    private val mComponent by lazy { app()!!.component.plus(SignInModule()) }
    private var mAuthEventsHandler: AuthEventsHandler? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try {
            mAuthEventsHandler = context as AuthEventsHandler
        } catch (e: Exception) {
            Timber.e(e, "SignInFragment must implement AuthEventsHandler interface")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.auth_sign_in_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mPresenter.attachView(this)
        createBtn.setOnClickListener { mAuthEventsHandler?.onCreateAccountClick() }
        userInputEdit.addTextChangedListener(object : SimpleTextWatcher() {
            override fun afterTextChanged(s: Editable?) {
                mPresenter.onUserChanged(s?.toString())
            }
        })
        passInputEdit.addTextChangedListener(object : SimpleTextWatcher() {
            override fun afterTextChanged(s: Editable?) {
                mPresenter.onPassChanged(s?.toString())
            }
        })
        nextBtn.setOnClickListener {
            mPresenter.onNextButtonClick()
        }

        mPresenter.setFacebookLoginBtnBehavior(facebookLoginBtn)

    }

    override fun onDestroyView() {
        mPresenter.detachView()
        super.onDestroyView()
    }

    override fun showNextButton(show: Boolean) {
        nextBtn.visibility = if (show) View.VISIBLE else View.GONE
    }

    override fun showInvalidCredentialsAlert() {
        AlertDialog.Builder(context)
                .setTitle(R.string.auth_sign_in_error_title)
                .setMessage(R.string.auth_sign_in_error_message)
                .create()
                .show()
    }

    override fun showFacebookAuthenticateFailed() {
        AlertDialog.Builder(context)
                .setTitle(R.string.auth_sign_in_error_facebook_title)
                .setMessage(R.string.auth_sign_in_error_facebook_message)
                .create()
                .show()
    }

    override fun notifyUserSigned() {
        mAuthEventsHandler?.onAuthComplete()
    }

    override fun showRegisterView(credentials: FirebaseCredentials) {
        mAuthEventsHandler?.registerFromFacebookLogin(credentials)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Pass the activity result back to the Facebook SDK
        mPresenter.onActivityResult(requestCode, resultCode, data)

    }



}