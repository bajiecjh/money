package com.bajie.money.viewmodel

import android.app.Application
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import com.bajie.money.model.dao.MovieDao
import com.bajie.money.model.data.MovieList
import com.bajie.money.model.data.MovieSubject
import com.bajie.money.model.loacal.AppDatabase
import com.bajie.money.model.remote.loader.MovieLoader
import com.bajie.money.model.repository.RemoteRepo
import io.reactivex.Single
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer


/**

 * bajie on 2021/1/8 15:54

 */
//constructor(private val repo: RemoteRepo)
class RemoteViewModel constructor(application: Application)  : AndroidViewModel(application) {
//    lateinit var application: Application;
//    constructor(application: Application)  {
//        super(application);
//    }

    val info = ObservableField<String>("click me");
    val loading = ObservableBoolean(false);

    private val repo:RemoteRepo;
    private val remote: MovieLoader;
    private val local: MovieDao;
    init {
        remote = MovieLoader();
        local = AppDatabase.getInstance(application).movieDao();
        repo = RemoteRepo(remote, local);
    }

    fun click() {
//        TestUtils.getInstance()!!.getMovies();
        MovieLoader().getMovies()
            ?.doOnSubscribe { loading.set(true) }           // 开始请求数据
            ?.doAfterTerminate { loading.set(false) }       // 请求完成
            ?.subscribe({t: MovieList ->
                    run {
                        info.set(t.toString());
                    }
                }, { t: Throwable ->
                    run {
                        info.set(t.message);
                    }
                });
    }

    fun loadMovies(): Single<MovieList> = repo.getList().doOnSuccess {  }

//    fun loadRemote1(): Single<MovieList> =
//        MovieLoader().getMovies()!!
//            .doOnSubscribe{loading.set(true)}
//            .doAfterTerminate { loading.set(false) }
//            .doOnSuccess { t: MovieList ->
//                t?.let {
////                    System.out.println(t.subject_collection_items.get(0));
//                    t.subject_collection_items.forEach { movie: MovieSubject ->
//                        info.set(info.get()+movie.title+",");
//                    }
//                }
//            }

    fun loadRemote(): Single<MovieList> = repo.getList()
        .doOnSubscribe{
            loading.set(true)
        }
        .doAfterTerminate(object : Action {
            override fun run() {
                loading.set(false);
            }

        })
        .doOnSuccess(object : Consumer<MovieList> {
            override fun accept(t: MovieList?) {
                t?.let {
                    t.subject_collection_items.forEach{movie: MovieSubject? ->
                        info.set(info.get() + movie!!.title + ".");
                    }
                }
            }

        })


}


