package ar.uba.fi.tallerii.comprameli.presentation.base

import android.support.v7.app.AppCompatActivity
import ar.uba.fi.tallerii.comprameli.presentation.ComprameliApp

open class BaseActivity : AppCompatActivity() {

    val app: ComprameliApp
        get() = application as ComprameliApp


}