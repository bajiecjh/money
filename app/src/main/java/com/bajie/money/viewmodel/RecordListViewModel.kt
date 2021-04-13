package com.bajie.money.viewmodel

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bajie.money.model.dao.RecordDao
import com.bajie.money.model.data.DayRecord
import com.bajie.money.model.data.MonthRecord
import com.bajie.money.utils.Canstant
import com.bajie.money.utils.FiveParams
import com.bajie.money.utils.TimeUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlin.collections.ArrayList

/**
 * bajie on 2021/4/6 10:35
 */
class RecordListViewModel(private val recordDao: RecordDao): ViewModel() {
    var title = "明细"
    val isShowLoading = MutableLiveData<Int>(View.GONE);
    val isLoadMonthRecordFinished = MutableLiveData<Boolean>(false);
    val monthRecords = ArrayList<MonthRecord>();
    lateinit var monthRecord: MonthRecord
    var maxPrice = 0.0f;   // 每个月支出/收入中最大一笔，用户条形报表百分比显示

    fun init() {
        isLoadMonthRecordFinished.value = false;
        isShowLoading.value = View.VISIBLE;
        recordDao.getEarliest().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { earliest, _ ->
                earliest?.let {
                    val date = TimeUtils.getFiveParams(TimeUtils.stampToDate(it, TimeUtils.TIME_PATTERN));
                    val nowDate = TimeUtils.getFiveParams(TimeUtils.getNowTime(TimeUtils.TIME_PATTERN));
                    getMonthRecord(nowDate.a, nowDate.b, date)
                }
            }
    }

    private fun getMonthRecord(year:Int, month:Int, endDate: FiveParams<Int, Int, Int, Int, Int>) {
        val monthData = TimeUtils.getFirstLastDayOfMonth(year, month);
        recordDao.getByTimeRange(monthData.a, monthData.b)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {recordList, _ ->
                monthRecord = MonthRecord();
                monthRecord.year = year;
                monthRecord.month = month;
                monthRecord.recordSize = recordList.size;
                for(record in recordList) {
                    if(record.type == Canstant.IN_TYPE) monthRecord.income += record.price;
                    else monthRecord.outlay += record.price

                    val dayAndWeek = TimeUtils.getDayAndWeek(record.time);
                    var dayRecords = monthRecord.dayRecords[dayAndWeek.a];
                    if(dayRecords == null) {
                        dayRecords = DayRecord();
                        dayRecords.day = dayAndWeek.a;
                        dayRecords.week = dayAndWeek.b;
                        dayRecords.records = ArrayList();
                        dayRecords.records.add(record);
                        monthRecord.dayRecords[dayAndWeek.a] = dayRecords;
                    } else {
                        dayRecords.records.add(0, record);
                    }
                    if(record.type == Canstant.IN_TYPE) dayRecords.income += record.price;
                    else dayRecords.outlay += record.price
                }
                if(monthRecord.income > maxPrice) maxPrice = monthRecord.income
                if(monthRecord.outlay > maxPrice) maxPrice = monthRecord.outlay
                monthRecords.add(monthRecord);
                if(year == endDate.a && month == endDate.b) {
                    isLoadMonthRecordFinished.value = true;
                    isShowLoading.value = View.GONE;
                    return@subscribe
                }
                if(month == 1) {
                    getMonthRecord(year-1, 12, endDate)
                } else {
                    getMonthRecord(year, month-1, endDate)
                }

            }
    }



}