package com.example.ecographics.utils

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

class Common {
    fun getStyledDrawableId(context: Context, attribute: Int): Int {
        val a = context.getTheme().obtainStyledAttributes(intArrayOf(attribute))
        return a.getResourceId(0, -1)
    }

    fun hideKeyboard (context:Context, view:View) {
        val inputMethodManager = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }
}