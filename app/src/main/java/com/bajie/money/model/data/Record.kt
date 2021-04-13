package com.bajie.money.model.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.ArrayList

/**

 * bajie on 2021/3/24 16:49

 */
@Entity(tableName = "record")
class Record {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0;
    var price: Float = 0.0f;
    var categoryId: Int = 0;
    var hint: String = "";
    var time: Long = 0;
    var categoryName: String = "";
    var parentCategoryName: String = "";
    var type: Int = 0;

    constructor(price:Float, categoryId:Int, hint:String, time: Long, categoryName: String, parentCategoryName: String, type: Int) {
        this.price = price;
        this.categoryId = categoryId;
        this.hint = hint;
        this.time = time;
        this.categoryName = categoryName;
        this.parentCategoryName = parentCategoryName
        this.type = type
    }
}

class MonthRecord {
    var year: Int = 0;
    var month: Int = 0;
    var income: Float = 0.0f;
    var outlay: Float = 0.0f;
    var recordSize: Int = 0;
    var dayRecords: HashMap<Int, DayRecord> = HashMap();
//    var records: ArrayList<Record> = ArrayList();
}

class DayRecord {
    var day: Int = 0;
    var week: String = "";
    var income: Float = 0.0f;
    var outlay: Float = 0.0f;
    var records: ArrayList<Record> = ArrayList();
}