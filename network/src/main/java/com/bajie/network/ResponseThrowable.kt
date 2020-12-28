package com.bajie.network

import java.lang.Exception


/**

 * bajie on 2020/12/23 22:31

 */
class ResponseThrowable: Exception {
    var code: Int = 0;
    var errMsg: String = "";

    constructor(message: String, throwable: Throwable, code: Int): super(message, throwable) {
        this.code = code;
    }

    constructor(message: String ,code:Int): super(message) {
        this.code = code;
    }


}