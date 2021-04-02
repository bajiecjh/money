package com.bajie.money.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bajie.money.model.data.Record

/**

 * bajie on 2021/4/2 15:36

 */
class SharedViewModel: ViewModel() {

    var addedRecord = MutableLiveData<Record>();

    fun setAddedCategory(record: Record) {
        addedRecord.value = record;
    }

}