package com.bajie.network

import io.reactivex.functions.Function;

/**

 * bajie on 2020/12/21 23:44

 */
class DataInfoLoad<T>: Function<BaseResponse<T>, T> {
    override fun apply(t: BaseResponse<T>): T {
        if(!t.isSuccess()) {

        }
        return t.Data;
    }
//    override fun apply(t: BaseResponse<T>): T {
//        if(!t.isSuccess()) {
//            throw Fault(t.status, t.message);
//        }
//        return t.data;
//    }
}