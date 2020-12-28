package com.bajie.network

/**

 * bajie on 2020/12/21 23:33

 */
class BaseResponse<T> {
//    var status: Int = 0;
//    var message: String = "";
    var Data: T = TODO();
    fun isSuccess():Boolean {
        return true;
    }
}