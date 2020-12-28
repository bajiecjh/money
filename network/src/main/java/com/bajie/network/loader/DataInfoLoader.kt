package com.bajie.network.loader


import com.bajie.network.*
import com.bajie.network.DataInfoLoad
import com.bajie.network.DataInfoSubject
import io.reactivex.Observable
import io.reactivex.functions.Function
import retrofit2.http.*

/**

 * bajie on 2020/12/17 23:27
 * 请求逻辑

 */
class DataInfoLoader: ObjectLoader {
    private val dataInfoService: DataInfoService;
    constructor() {
        dataInfoService = RetrofitServiceManager.getInstance().create(DataInfoService::class.java);
    }
    public fun getDataInfoURL(): Observable<DataInfoSubject?>? {
        val load = DataInfoLoad<DataInfoSubject>();
        return observer(dataInfoService.getDataInfoURL("android"))!!.map(load);

//        return observer(dataInfoService.getDataInfoURL("android"))!!.map<DataInfoSubject?>(object :
//            Function<BaseResponse<DataInfoSubject>, DataInfoSubject> {
//            override fun apply(t: BaseResponse<DataInfoSubject>): DataInfoSubject {
//                if(!t.isSuccess()) {
////                    throw Fault(t.status, t.message);
//                }
//                return  t.data;
//            }
//        });
    }

    interface DataInfoService {

        @FormUrlEncoded
        @POST("AppAjaxs/GetDataInfoURL.ashx")
        fun getDataInfoURL(@Field("platform") platform: String): Observable<BaseResponse<DataInfoSubject>>;
    }
}

