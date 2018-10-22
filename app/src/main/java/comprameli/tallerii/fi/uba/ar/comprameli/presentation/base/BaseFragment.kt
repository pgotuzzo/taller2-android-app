package comprameli.tallerii.fi.uba.ar.comprameli.presentation.base

import android.support.v4.app.Fragment
import comprameli.tallerii.fi.uba.ar.comprameli.presentation.ComprameliApp

open class BaseFragment : Fragment() {

    fun app(): ComprameliApp? = activity?.application as ComprameliApp

}