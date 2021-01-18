package com.bajie.money.model.data

import androidx.room.Entity
import androidx.room.PrimaryKey


/**

 * bajie on 2020/12/28 17:19

 */
@Entity(tableName = "movies")
class MovieSubject {
    @PrimaryKey
    var id: String = "";
    var title: String = "";

//    override fun toString(): String {
//        return "MovieSubject{count:"+count+
////                ",subject_collection:"+subject_collection.toString()+"" +
//                ",subject_collection_items:"+subject_collection_items.get(0)+
//                ",subject_collection_items:"+subject_collection_items.get(1)+
//                ",total:"+total+
//                ",start:"+start+"}";
//    }
}