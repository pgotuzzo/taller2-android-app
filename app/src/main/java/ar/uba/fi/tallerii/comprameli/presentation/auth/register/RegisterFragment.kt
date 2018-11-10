package ar.uba.fi.tallerii.comprameli.presentation.auth.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import ar.uba.fi.tallerii.comprameli.R
import ar.uba.fi.tallerii.comprameli.presentation.auth.register.di.RegisterModule
import ar.uba.fi.tallerii.comprameli.presentation.auth.signin.di.SignInModule
import ar.uba.fi.tallerii.comprameli.presentation.base.BaseFragment
import kotlinx.android.synthetic.main.auth_register_fragment.*
import javax.inject.Inject

class RegisterFragment : BaseFragment(), RegisterContract.View {

    @Inject
    lateinit var mPresenter: RegisterContract.Presenter

    private val mComponent by lazy { app()!!.component.plus(RegisterModule()) }

    companion object {
        fun getInstance(): RegisterFragment {
            return RegisterFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.auth_register_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mPresenter.attachView(this)
        mailBtn.setOnClickListener {
            mailFormScroll.visibility = VISIBLE
        }
        facebookBtn.setOnClickListener {
            mailFormScroll.visibility = GONE
        }
    }

    override fun onDestroyView() {
        mPresenter.detachView()
        super.onDestroyView()
    }

}