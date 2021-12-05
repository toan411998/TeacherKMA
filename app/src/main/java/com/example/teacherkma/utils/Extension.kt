package com.example.teacherkma.utils

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager

class Extension {
}

// extension function to hide soft keyboard programmatically
public fun Activity.hideSoftKeyboard(){
    (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).apply {
        hideSoftInputFromWindow(currentFocus?.windowToken, 0)
    }
}