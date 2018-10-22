package comprameli.tallerii.fi.uba.ar.comprameli.presentation.auth.register

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import comprameli.tallerii.fi.uba.ar.comprameli.R
import kotlinx.android.synthetic.main.auth_register_fragment.*

class RegisterFragment : Fragment() {

    companion object {
        fun getInstance(): RegisterFragment {
            return RegisterFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.auth_register_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mailBtn.setOnClickListener {
            mailFormScroll.visibility = VISIBLE
        }
        facebookBtn.setOnClickListener {
            mailFormScroll.visibility = GONE
        }
    }

}