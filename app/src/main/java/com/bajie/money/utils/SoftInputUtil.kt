package com.bajie.money.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

/**

 * bajie on 2021/3/24 12:05

 */
class SoftInputUtil {
    companion object {
        fun showSoftInput(view: View) {
            val inputMethodManager = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager;
            inputMethodManager.showSoftInput(view, 0);
        }
        fun hideSoftInput(view: View) {
            val inputMethodManager = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager;
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0);
        }
    }
}