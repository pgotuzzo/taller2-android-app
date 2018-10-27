package ar.uba.fi.tallerii.comprameli.presentation.base

import android.support.v4.app.Fragment
import ar.uba.fi.tallerii.comprameli.presentation.ComprameliApp

open class BaseFragment : Fragment() {

    fun app(): ComprameliApp? = activity?.application as ComprameliApp

}