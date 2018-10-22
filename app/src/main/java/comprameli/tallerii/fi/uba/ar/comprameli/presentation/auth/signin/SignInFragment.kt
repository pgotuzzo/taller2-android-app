package comprameli.tallerii.fi.uba.ar.comprameli.presentation.auth.signin

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import comprameli.tallerii.fi.uba.ar.comprameli.R
import comprameli.tallerii.fi.uba.ar.comprameli.presentation.auth.AuthEventsHandler
import comprameli.tallerii.fi.uba.ar.comprameli.presentation.auth.signin.di.SignInModule
import comprameli.tallerii.fi.uba.ar.comprameli.presentation.base.BaseFragment
import comprameli.tallerii.fi.uba.ar.comprameli.presentation.utils.SimpleTextWatcher
import kotlinx.android.synthetic.main.auth_sign_in_fragment.*
import timber.log.Timber
import javax.inject.Inject

class SignInFragment : BaseFragment(), SignInContract.View {

    @Inject
    lateinit var mPresenter: SignInContract.Presenter

    private val mComponent by lazy { app()!!.component.plus(SignInModule()) }
    private var mAuthEventsHandler: AuthEventsHandler? = null

    companion object {
        fun getInstance(): SignInFragment {
            return SignInFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mComponent.inject(this)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try {
            mAuthEventsHandler = context as AuthEventsHandler
        } catch (e: Exception) {
            Timber.e(e, "Activity must implement AuthEventsHandler interface")
        }
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
    }

    override fun onDestroyView() {
        mPresenter.detachView()
        super.onDestroyView()
    }

    override fun showNextButton(show: Boolean) {
        nextBtn.visibility = if (show) View.VISIBLE else View.GONE
    }

}