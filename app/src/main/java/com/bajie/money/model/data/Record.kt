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
    var hint: String = ""

    constructor(price:Float, categoryId:Int, hint:String) {
        this.price = price;
        this.categoryId = categoryId;
        this.hint = hint;
    }
}