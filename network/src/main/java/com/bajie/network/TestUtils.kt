package com.bajie.network

import com.bajie.network.loader.DataInfoLoader

/**

 * bajie on 2020/12/15 23:47

 */
class TestUtils {
    companion object {
        private var instance: TestUtils? = null;
        private val BASE_URL = "http://gameweb-zhizun-rd.efunfun.com";
        fun getInstance(): TestUtils? {
            if(instance == null) {
                instance = TestUtils();
            }
            return instance;
        }
    }

    public fun request1() {
        val loader = DataInfoLoader();
        loader.getDataInfoURL()!!.subscribe{t: DataInfoSubject? ->
            println(t);
        };
    }

    public fun request() {
//        val retrofit: Retrofit = Retrofit.Builder()
//            .baseUrl(BASE_URL)
//            .addConverterFactory(GsonConverterFactory.create())
//            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//            .addConverterFactory(GsonConverterFactory.create())
//            .build();
//        val movieService: MovieService = retrofit.create(MovieService::class.java);
        val movieService: MovieService = RetrofitServiceManager.getInstance().create(MovieService::class.java);


//        val subscription = movieService.getDataInfoURL("android")
//            .subscribeOn(AndroidSchedulers.mainThread())
//            .subscribe(object: DisposableObserver<MovieSubject>() {
//                override fun onComplete() {
//                }
//
//                override fun onNext(t: MovieSubject) {
//                    println(t.CdnDomain);
//                    println(t.DataInfoUrl);
//                }
//
//                override fun onError(e: Throwable) {
//                }
//
//            });

        // 没加入Rxjava之前的写法
//        call.enqueue(object: Callback<MovieSubject> {
//            override fun onFailure(call: Call<MovieSubject>, t: Throwable) {
//                t.printStackTrace();
//            }
//
//            override fun onResponse(call: Call<MovieSubject>, response: Response<MovieSubject>) {
//                println(response.body()!!.CdnDomain);
//                println(response.body()!!.DataInfoUrl);
//            }
//
//        } );
    }
}


