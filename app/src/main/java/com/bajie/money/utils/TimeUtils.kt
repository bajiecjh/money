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
            val simpleDateFormat = SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
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
    }
}