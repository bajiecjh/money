package com.bajie.money.model.data

import androidx.room.Entity
import androidx.room.PrimaryKey

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

    constructor(price:Float, categoryId:Int, hint:String, time: Long, categoryName: String, parentCategoryName: String) {
        this.price = price;
        this.categoryId = categoryId;
        this.hint = hint;
        this.time = time;
        this.categoryName = categoryName;
        this.parentCategoryName = parentCategoryName
    }
}