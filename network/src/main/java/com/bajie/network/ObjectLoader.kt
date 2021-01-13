package com.bajie.network

import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**

 * bajie on 2020/12/17 23:29

 */
public open class ObjectLoader {
    constructor() {}

    fun <T> observer(observable: Single<T>): Single<T>? {
        return observable
            .subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .onErrorResumeNext(HttpErrorHandler());
    }
}