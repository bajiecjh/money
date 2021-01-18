package com.bajie.money.model.repository

import com.bajie.money.model.dao.MovieDao
import com.bajie.money.model.data.MovieList
import com.bajie.money.model.data.MovieSubject
import com.bajie.money.model.remote.loader.MovieLoader
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers

/**

 * bajie on 2021/1/15 15:51

 */
class RemoteRepo constructor(private val remote: MovieLoader, private val local: MovieDao) {
    fun getList(): Single<MovieList> =
        local.getList()
            .flatMap(object : Function<List<MovieSubject>, Single<MovieList>> {
                override fun apply(t: List<MovieSubject>): Single<MovieList>? {
                    return if (t.isEmpty()) {
                        remote.getMovies()
                            ?.flatMap(object : Function<MovieList, Single<MovieList>> {
                                override fun apply(t: MovieList): Single<MovieList> {
                                    local.insertAll(t.subject_collection_items);
                                    return Single.just(t);
                                }

                            });
                    } else {
                        return Single.just(MovieList(t));
                    }
                }

            })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())



//            .flatMap(object: Function<List<MovieSubject>, Single<MovieList>> {
//                override fun apply(t: List<MovieSubject>): Single<MovieList>? {
//                    return if(t.isEmpty()) {
//                        remote.getMovies()
//                            ?.doOnNext(object : Consumer<MovieList> {
//                                override fun accept(t: MovieList?) {
//                                    local.insertAll(t!!.subject_collection_items);
//                                }
//                            })
//                            ?.subscribeOn(Schedulers.io())
//                    } else {
//                        Observable.fromArray(MovieList(t);
//                    }
//                }
//
//            })


//        .flatMap(object :  Function<List<MovieSubject>, Single<MovieList>> {
//            override fun apply(t: List<MovieSubject>): Single<MovieList> {
//                return Observable.just( MovieList(t));
////                return Single.fromObservable(Observable.just( MovieList(t)));
//            }
//        })
//
//        .onErrorResumeNext {
//            remote.getMovies()?.doOnSuccess{t: MovieList? ->
//                t?.let {
//                    local.insertAll(t.subject_collection_items);
//                }
//            }
//        }
//        .subscribeOn(Schedulers.io());

//    fun getList(): Single<MovieList> = local.getCount()
//        .flatMap(object : Function<Int, Single<MovieList>> {
//            override fun apply(t: Int): Single<MovieList> {
//                return if (t <= 0) {
//                    remote.getMovies()!!.doOnSuccess(object : Consumer<MovieList> {
//                        override fun accept(t: MovieList?) {
//                            local.insertAll(t!!.subject_collection_items);
//                        }
//
//                    })
//                } else {
//                    Single.fromObservable(Observable.empty<MovieList>());
//                }
//                Observable.empty<MovieList>().switchIfEmpty()
//            }
//
//        })




//    fun getList(): Single<MovieList> = local.getCount()
//        .map(object : Function<Int,MovieList> {
//            override fun apply(count: Int): MovieList? {
//                return remote.getMovies()
//
//
//        })



            //                if(count <= 0) {
//                    return remote.getMovies()
////                    !!.doOnSuccess { t: MovieList? ->  local.insertAll(t!!.subject_collection_items) }
//                } else {
////                    local.getList()
////                        .map(object : Function<List<MovieSubject>, MovieList> {
////                            override fun apply(t: List<MovieSubject>): MovieList {
////                                return MovieList(t);
////                            }
////                        })
//                }
//                return null;
//            }

//        .doOnSuccess(
//            object : Consumer<Int>, io.reactivex.functions.Consumer<Int> {
//                override fun accept(t: Int) {
//                    if (t<=0) {
//                        remote.getMovies()!!.doOnSuccess {t: MovieList? ->
//                            t?.let {
//                                local.insertAll(t.subject_collection_items);
//                            }
//                        }
//                    }
//                }
//            }
//        )
}


