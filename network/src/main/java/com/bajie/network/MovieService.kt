package com.bajie.network

import io.reactivex.Observable
import retrofit2.Call
import retrofit2.http.*

/**

 * bajie on 2020/12/16 00:09

 */
interface MovieService {
    @GET("top250")
    fun getTop250(@Query("start") start: Int, @Query("count") count: Int): Call<MovieSubject>;

    @FormUrlEncoded
    @POST("AppAjaxs/GetDataInfoURL.ashx?platform=android&timestamp=1608130338739&productname=apk")
    fun getDataInfoURL(@Field("platform") platform: String): Observable<MovieSubject>;
}