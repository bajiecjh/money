package com.bajie.network.bean

import com.google.gson.JsonArray
import com.google.gson.JsonObject

/**

 * bajie on 2020/12/28 17:19

 */
class MovieSubject {
    var count = 0;
//    lateinit var subject_collection:JsonObject;
//    lateinit var subject_collection_items:JsonArray;
    var total = 0;
    var start = 0;

    override fun toString(): String {
        return "MovieSubject{count:"+count+
//                ",subject_collection:"+subject_collection.toString()+"" +
//                ",subject_collection_items:"+subject_collection_items.toString()+
                ",total:"+total+
                ",start:"+start+"}";
    }
}