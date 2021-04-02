package com.bajie.money.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bajie.money.model.dao.RecordDao
import com.bajie.money.model.data.Record
import com.bajie.money.utils.Canstant
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
    val monthIn = MutableLiveData<Float>(0.0f);
    val balance = MutableLiveData<Float>(0.0f);

    val monthData by lazy {
        TimeUtils.getFirstLastDayOfMonth()
    }


    fun getMonthSpending() {
        local.getSumOutByTimeRange(monthData.a, monthData.b)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {t1, _ ->
                t1?.let{ it ->
                    monthSpending.value = it
                    setBalance()
//                    monthSpending.value = it.sumByDouble { it.price.toDouble() }.toFloat()
                }
            }
    }

    fun getMonthIn() {
        local.getSumInByTimeRange(monthData.a, monthData.b)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { t1, _ ->
                t1.let {it ->
                    monthIn.value = it;
                    setBalance()
                }
            }
    }

    private fun setBalance() {
        balance.value = monthIn.value!! - monthSpending.value!!;
    }

    fun getFiveRecords(): Single<MutableList<Record>> {
        return local.getFiveRecords().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { it ->
                fiveRecords.value = it.toMutableList();
                fiveRecords.value!!;
            }

    }

    fun addNewRecord(record: Record): Single<MutableList<Record>> {
        return Single.create { emitter ->
            if(record.time >= monthData.a && record.time < monthData.b) {
                if (record.type == Canstant.OUT_TYPE) {
                    monthSpending.value = monthSpending.value!! + record.price;
                } else {
                    monthIn.value = monthIn.value!! + record.price;
                }
                setBalance();
            }

            getFiveRecords().subscribe { _, _ -> emitter.onSuccess(fiveRecords.value!!) }

        }
    }
}