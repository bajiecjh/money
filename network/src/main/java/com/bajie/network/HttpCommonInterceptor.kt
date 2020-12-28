package com.bajie.network

import okhttp3.Interceptor
import okhttp3.Response

/**

 * bajie on 2020/12/16 23:47
 * 拦截器

 */
class HttpCommonInterceptor: Interceptor  {
    private var headerMap = HashMap<String, String>();
    private var urlParams = HashMap<String, String>();


    override fun intercept(chain: Interceptor.Chain): Response {
        val oldRequest = chain.request();

        // 添加新的参数到url中
        val url = oldRequest.url();
        val newUrlBuilder = url.newBuilder();
        for((key, value) in urlParams) {
            newUrlBuilder.addEncodedQueryParameter(key, value);
        }
        val newUrl = newUrlBuilder.build();

        val requestBuilder = oldRequest.newBuilder()
            .url(newUrl)
            .method(oldRequest.method(), oldRequest.body());

        // 添加新的参数到header中
        if(headerMap.size > 0) {
            for ((key, value) in headerMap) {
                requestBuilder.addHeader(key, value);
            }
        }
        val newRequest = requestBuilder.build();
        return chain.proceed(newRequest);
    }

    class Builder {
        private var mHttpCommonInterceptor: HttpCommonInterceptor;

        constructor() {
            mHttpCommonInterceptor = HttpCommonInterceptor();
        }

        public fun addHeaderParams(key: String, value: String): Builder {
            this.mHttpCommonInterceptor.headerMap[key] = value;
            return this;
        }
        public fun addUrlParams(key: String, value: String): Builder {
            this.mHttpCommonInterceptor.urlParams[key] = value;
            return this;
        }
        public fun build(): HttpCommonInterceptor {
            return mHttpCommonInterceptor;
        }
    }


}