package com.bajie.money.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bajie.money.R

/**

 * bajie on 2021/3/26 11:41

 */
class TimePickerViewModel: ViewModel() {
    // top数据
    val title = MutableLiveData<String>("时间");
    val rightIcon = R.drawable.icon_finish;
}