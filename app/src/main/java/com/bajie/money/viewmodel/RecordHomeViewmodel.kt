package com.bajie.money.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bajie.money.model.dao.RecordDao
import com.bajie.money.model.data.Record
import com.bajie.money.utils.TimeUtils
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**

 * bajie on 2021/1/4 16:04

 */
class RecordHomeViewmodel(val local: RecordDao) : ViewModel() {

    val fiveRecords  = MutableLiveData<MutableList<Record>>();
    val monthSpending = MutableLiveData<Float>(0.0f);


    fun getMonthSpending() {
        var params = TimeUtils.getFirstLastDayOfMonth();
        local.getByTimeRange(params.a, params.b)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {t1, t2 ->
                t1?.let{ it ->
//                    monthSpending.value = it.stream().mapToDouble {it.price.toDouble()}.sum().toFloat();
                    monthSpending.value = it.sumByDouble { it.price.toDouble() }.toFloat()
                }
            }
    }

    fun getFiveRecords(): Single<MutableList<Record>> {
        return local.getFiveRecords().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { it ->
                fiveRecords.value = it.toMutableList();
                fiveRecords.value!!;
            }

    }
}