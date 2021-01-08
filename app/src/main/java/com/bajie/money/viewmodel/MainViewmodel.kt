package com.bajie.money.viewmodel

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bajie.money.model.TestData

/**

 * bajie on 2021/1/4 16:04

 */
class MainViewmodel : ViewModel() {
//    var testNum = 0;

    val testData: MutableLiveData<TestData> = MutableLiveData();
    init {
        testData.value = TestData("bajie", 0);
    }

    val info = ObservableField<String>("${testData.value?.name} 点击了 ${testData.value?.count} 次");

    fun click() {
        testData.value!!.count ++;
        info.set("${testData.value?.name} 点击了 ${testData.value?.count} 次")
    }

    val testNum: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>();
    }
}