package com.bajie.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**

 * bajie on 2020/12/16 23:27
 * 统一生成接口实例管理类

 */
class RetrofitServiceManager {
    private val defaultTimeOut = 5;
    private val defaultReadTimeOut = 10;
    private val baseUrl = "http://gameweb-zhizun-rd.efunfun.com";
    var mRetrofit: Retrofit;


    companion object {
        fun getInstance(): RetrofitServiceManager {
            return SingletonHolder.INSTANCE;
        }
    }

    /**
     * 获取对应的Service
     */
    public fun <T> create(service: Class<T>): T {
        return mRetrofit.create(service);
    }

    private constructor() {
        val okHttpClient = OkHttpClient.Builder();
        okHttpClient.connectTimeout(defaultTimeOut.toLong(), TimeUnit.SECONDS);  // 连接超时时间
        okHttpClient.writeTimeout(defaultReadTimeOut.toLong(), TimeUnit.SECONDS);   // 写操作 超时时间
        okHttpClient.readTimeout(defaultReadTimeOut.toLong(), TimeUnit.SECONDS);    // 读操作 超时时间


        // 添加公共参数拦截器
        val httpCommonInterceptor = HttpCommonInterceptor.Builder()
            .addUrlParams("platform", "android")
            .addUrlParams("timestamp", System.currentTimeMillis().toString())
            .addUrlParams("productname", "apk")
            .build();
        okHttpClient.addInterceptor(httpCommonInterceptor);

        // 创建Retrofit
        mRetrofit = Retrofit.Builder()
            .client(okHttpClient.build())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(baseUrl)
            .build();
    }
    private class SingletonHolder {
        companion object {
            public val INSTANCE: RetrofitServiceManager = RetrofitServiceManager();
        }
    }
}