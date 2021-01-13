package com.bajie.network

import com.google.gson.JsonParseException
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.Function
import org.apache.http.conn.ConnectTimeoutException
import org.json.JSONException
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.text.ParseException
import java.util.*
import javax.net.ssl.SSLHandshakeException

/**

 * bajie on 2020/12/23 22:27

 */
class HttpErrorHandler<T>: Function<Throwable, Single<T>> {
    companion object {
        const val UNAUTHORIZED:Int = 401;
        const val FORBIDDEN =  403;
        const val NOT_FOUND = 404;
        const val REQUEST_TIMEOUT = 408;
        const val INTERNAL_SERVER_ERROR = 500;
        const val BAD_GATEWAY = 502;
        const val SERICE_UNAVAILABLE = 503;
        const val GATEWAY_TIMEOUT = 504;

        // 约定异常
        const val ERROR_UNKNOWN = 1000;     //未知错误
        const val ERROR_PARSE = 1001;       // 解析错误
        const val ERROR_NET = 1002;         // 网络错误
        const val ERROR_HTTP = 1003;        //  协议出错
        const val ERROR_SSL = 1005;         // 证书出错
        const val ERROR_TIMEOUT = 1006;     // 连接超时

        fun handleException(e: Throwable): ResponseThrowable? {
//        var ex: ResponseThrowable;
            if(e is HttpException) {
//            ex = ResponseThrowable("网络错误", e, ERROR_HTTP);
                when(e.code()) {
                    UNAUTHORIZED, FORBIDDEN, NOT_FOUND, REQUEST_TIMEOUT, INTERNAL_SERVER_ERROR, BAD_GATEWAY, SERICE_UNAVAILABLE, GATEWAY_TIMEOUT -> {
                        return ResponseThrowable("网络错误", e, ERROR_HTTP);
                    }

                }

            } else if(e is JsonParseException || e is JSONException || e is ParseException) {
                return ResponseThrowable("解析错误", e, ERROR_PARSE);
            } else if(e is ConnectException) {
                return ResponseThrowable("连接错误", e, ERROR_NET);
            } else if(e is SSLHandshakeException) {
                return ResponseThrowable("证书验证失败", e, ERROR_SSL);
            } else if(e is ConnectTimeoutException || e is SocketTimeoutException) {
                return ResponseThrowable("连接超时", e , ERROR_TIMEOUT);
            } else {
                return ResponseThrowable("未知错误", e, ERROR_UNKNOWN);
            }
            return null;
        }

    }
    override fun apply(t: Throwable): Single<T> {
        return Single.error(handleException(t));
    }


}