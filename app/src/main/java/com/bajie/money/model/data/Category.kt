package com.bajie.money.model.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

/**

 * bajie on 2021/2/2 18:28

 */
@Entity(tableName = "categories")
class Category: Serializable {
    companion object {
        const val OUT_PARENT_ID = -2;
    }

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0;
    var name: String = "";
    var parentId: Int = -1; // -1表示支出大类，-2表示收入小类
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