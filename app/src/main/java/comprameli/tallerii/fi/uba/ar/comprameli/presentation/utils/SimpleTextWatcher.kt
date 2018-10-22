package comprameli.tallerii.fi.uba.ar.comprameli.presentation.utils

import android.text.TextWatcher

abstract class SimpleTextWatcher : TextWatcher {

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        // No op
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        // No op
    }

}