package com.bajie.money.viewmodel

import androidx.lifecycle.MutableLiveData
import com.bajie.money.model.data.TestData

/**

 * bajie on 2021/1/8 11:30

 */
class TestDataViewModel {
    var testData: MutableLiveData<TestData> = MutableLiveData();
    init {
        testData.value = TestData("bajie", 0);
    }
}