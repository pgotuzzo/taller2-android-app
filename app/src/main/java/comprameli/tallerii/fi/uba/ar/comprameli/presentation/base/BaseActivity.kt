package comprameli.tallerii.fi.uba.ar.comprameli.presentation.base

import android.support.v7.app.AppCompatActivity
import comprameli.tallerii.fi.uba.ar.comprameli.presentation.ComprameliApp

open class BaseActivity : AppCompatActivity() {

    val app: ComprameliApp
        get() = application as ComprameliApp


}