package com.bajie.money.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**

 * bajie on 2021/1/4 16:04

 */
class MainViewmodel : ViewModel() {
//    var testNum = 0;

    val testNum: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>();
    }
}