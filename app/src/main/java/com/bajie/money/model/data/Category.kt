package com.bajie.money.model.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**

 * bajie on 2021/2/2 18:28

 */
@Entity(tableName = "categories")
class Category {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 1;
    var name: String = "";
    var parentId: Int = 0;
    var isCommonly: Boolean = false;
}