package com.bajie.money.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bajie.money.model.dao.CategoryDao
import com.bajie.money.model.loacal.AppDatabase
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*

/**

 * bajie on 2021/1/4 16:04

 */
class BookkeepingChildViewmodel constructor(val local: CategoryDao) : ViewModel() {
    // 0 支出，1收入
    var position = 0;

    companion object {
        const val POSITION = "position";
    }

    fun getDefaultCategory(): Single<String> {
        return Single.create { emitter ->
            getDefaultFood().subscribe { name, _ ->
                if(name != null) {
                    emitter.onSuccess(name)
                } else {
                    getFirstCommonly().subscribe { name, _ ->
                        if(name != null) {
                            emitter.onSuccess(name);
                        } else {
                            getFirstChild().subscribe{name, _ ->
                                if (name != null) {
                                    emitter.onSuccess(name);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun getFirstCommonly(): Single<String> {
        return local.getCommonlyList()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map {
                it[0].name;
            }

    }

    fun isCommonly() {

    }

    private fun getFirstChild(): Single<String> {
        return local.getChildList()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map {
                it[0].name;
            }
    }

    private fun getDefaultFood(): Single<String> {
        val calender = Calendar.getInstance();
        val hour = calender.get(Calendar.HOUR_OF_DAY);
        var default = ""
        if(hour == 0 || hour == 1 || hour == 2 || hour == 3 || hour == 4 || hour == 17 || hour == 18 || hour == 19 || hour == 20 || hour == 21 ) {
            default = "晚餐"
        } else if(hour == 12 || hour == 13 || hour == 14 || hour == 15 || hour == 16 ) {
            default = "午餐"
        } else if(hour == 5 || hour == 6 || hour == 7 || hour == 8 || hour == 9 || hour == 10 || hour == 11 )  {
            default = "早餐"
        } else {
            default = "宵夜"
        }

        return local.getCategoryByName(default)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map {
                it.name;
            }
    }

    class ViewModelFactory(private val context: Context): ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            val local = AppDatabase.getInstance(context).categoryDao();
            return modelClass.getConstructor(CategoryDao::class.java).newInstance(local);
        }
    }
}