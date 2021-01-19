package com.bajie.money.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
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
class RemoteViewModel constructor(val repo: RemoteRepo)  : ViewModel() {


//    val info = ObservableField<String>("click me");
    val info = MutableLiveData<String>("clickme");
    val loading = MutableLiveData<Boolean>(false);

    fun <T:Any> MutableLiveData<T>.set(value: T ?) = postValue(value);
    fun <T:Any> MutableLiveData<T>.get() = value;

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

    fun loadRemote1(): Single<MovieList> =
        MovieLoader().getMovies()!!
            .doOnSubscribe{loading.set(true)}
            .doAfterTerminate { loading.set(false) }
            .doOnSuccess { t: MovieList ->
                t?.let {
//                    System.out.println(t.subject_collection_items.get(0));
                    t.subject_collection_items.forEach { movie: MovieSubject ->
                        info.set(info.get()+movie.title+",");
                    }
                }
            }

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

    class RemoteViewModelFactory(private val application: Application): ViewModelProvider.Factory {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            val remote = MovieLoader();
            val local = AppDatabase.getInstance(application).movieDao();
            val repo = RemoteRepo(remote, local);
            return modelClass.getConstructor(RemoteRepo::class.java).newInstance(repo);
        }

    }
}


