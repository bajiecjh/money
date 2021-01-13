package com.bajie.network.loader

import com.bajie.network.BaseResponse
import com.bajie.network.ObjectLoader
import com.bajie.network.RetrofitServiceManager
import com.bajie.network.bean.DataInfoSubject
import com.bajie.network.bean.MovieSubject
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.Function
import retrofit2.http.*

/**

 * bajie on 2020/12/28 17:10

 */
class MovieLoader: ObjectLoader {
    private val moviceService: MovieService;
    constructor() {
        moviceService = RetrofitServiceManager.getInstance().create(MovieService::class.java);
    }

    public fun getMovies(): Single<MovieSubject>? {
        return observer(moviceService.getMovies("A", "B"));
    }


    interface MovieService {
        @GET("subject_collection/movie_showing/items")
        fun getMovies(@Query("start") start: String, @Query("count") count: String): Single<MovieSubject>;
    }
}