package com.bajie.money.model.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**

 * bajie on 2021/2/2 18:28

 */
@Entity(tableName = "categories")
class Category {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0;
    var name: String = "";
    var parentId: Int = -1;
    var commonly: Int = 0;

    fun copy(): Category {
        val category = Category();
        category.id = id;
        category.name = name;
        category.parentId = parentId;
        category.commonly = commonly;
        return category;
    }
}