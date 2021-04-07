package com.bajie.money.utils

import android.view.View
import androidx.databinding.BindingAdapter

/**

 * bajie on 2021/4/7 16:41

 */
@BindingAdapter("android:layout_width")
public fun setRightMargin(view: View, width: Float) {
    val params = view.layoutParams;
    params.width = width.toInt()
    view.layoutParams = params
}