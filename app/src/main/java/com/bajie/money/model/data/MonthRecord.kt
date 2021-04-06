package com.bajie.money.model.data

import java.util.ArrayList

/**

 * bajie on 2021/4/6 12:20

 */
class MonthRecord {
    var year: Int = 0;
    var month: Int = 0;
    var income: Float = 0.0f;
    var outlay: Float = 0.0f;
    var records: ArrayList<Record> = ArrayList();
}