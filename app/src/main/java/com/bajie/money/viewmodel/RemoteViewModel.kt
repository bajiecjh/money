package com.bajie.money.viewmodel

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.bajie.network.bean.MovieSubject
import com.bajie.network.loader.MovieLoader
import io.reactivex.Single


/**

 * bajie on 2021/1/8 15:54

 */
class RemoteViewModel : ViewModel() {
    val info = ObservableField<String>("click me");
    val loading = ObservableBoolean(false);
    fun click() {
//        TestUtils.getInstance()!!.getMovies();
        MovieLoader().getMovies()
            ?.doOnSubscribe { loading.set(true) }           // 开始请求数据
            ?.doAfterTerminate { loading.set(false) }       // 请求完成
            ?.subscribe({t: MovieSubject ->
                    run {
                        info.set(t.toString());
                    }
                }, { t: Throwable ->
                    run {
                        info.set(t.message);
                    }
                });
    }

    fun loadRemote(): Single<MovieSubject> =
        MovieLoader().getMovies()!!
            .doOnSubscribe{loading.set(true)}
            .doAfterTerminate { loading.set(false) }
            .doOnSuccess { t:MovieSubject ->
                run {
                    info.set(t.toString());
                }
            }
}