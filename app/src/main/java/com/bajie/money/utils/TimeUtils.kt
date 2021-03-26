package com.bajie.money.utils

import java.text.SimpleDateFormat
import java.util.*

/**

 * bajie on 2021/3/26 10:38

 */
class TimeUtils {

    companion object {
        // 获取当前时间
        fun getNowTime(pattern: String): String {
            val simpleDateFormat = SimpleDateFormat(pattern);
            val date = Date(System.currentTimeMillis());
            return simpleDateFormat.format(date);
        }
        // 获取时间戳
        fun getTimeString(): String {
            val simpleDateFormat = SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
            val calendar = Calendar.getInstance();
            return simpleDateFormat.format(calendar.time);
        }
        // 时间转时间戳
        fun dateToStamp(time: String): String {
            val simpleDateFormat = SimpleDateFormat();
            val date = simpleDateFormat.parse(time);
            val ts = date.time;
            return ts.toString();
        }
        // 时间戳转字符串
        fun stampToDate(stamp: String, pattern: String): String {
            var dateFormat = SimpleDateFormat(pattern);
            val times = dateFormat.format(Date(stamp));
            return times;
        }
        fun stringToDate(str: String, pattern: String): Date {
            var dateFormat = SimpleDateFormat(pattern);
            val date = dateFormat.parse(str);
            return date;
        }
        // 获取年月日时分
        fun getFiveParams(dateStr: String): FiveParams<Int, Int, Int, Int, Int> {
            var dateFormat = SimpleDateFormat();
            val date = dateFormat.parse(dateStr);
            val calendar = Calendar.getInstance();
            calendar.time = date;
            return FiveParams(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
        }
    }
}