package com.bajie.money.model.remote.loader

import com.bajie.money.model.data.MovieList
import com.bajie.network.ObjectLoader
import com.bajie.network.RetrofitServiceManager
import io.reactivex.Single
import retrofit2.http.*

/**

 * bajie on 2020/12/28 17:10

 */
class MovieLoader: ObjectLoader {



    private val moviceService: MovieService;
    constructor() {
        moviceService = RetrofitServiceManager.getInstance().create(MovieService::class.java);
    }

    public fun getMovies(): Single<MovieList>? {
        return observer(moviceService.getMovies("A", "B"))
//            ?.map(object : Function<MovieList, ArrayList<MovieSubject>> {
//                override fun apply(t: MovieList): ArrayList<MovieSubject> {
//                    return t.subject_collection_items;
//                }
//            })
//            ?.flatMap(object : Function<MovieList, Single<MovieSubject>> {
//                override fun apply(t: MovieList): Single<MovieSubject> {
//                    // return
//                    return Single.fromObservable(Observable.fromIterable(t.subject_collection_items));
//                }
//
//            })
    }


    interface MovieService {
        @GET("subject_collection/movie_showing/items")
        fun getMovies(@Query("start") start: String, @Query("count") count: String): Single<MovieList>;
    }
}