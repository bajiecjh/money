package com.bajie.money.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bajie.money.model.dao.CategoryDao
import com.bajie.money.model.data.Category
import com.bajie.money.model.loacal.AppDatabase
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**

 * bajie on 2021/1/4 16:04

 */
class ParentCategoryListViewmodel constructor(val local: CategoryDao) : ViewModel() {
    val title = "支出列表管理";

    val category = MutableLiveData<Category>();

    init {
        category.value = Category();
    }

    fun getList(): Single<MutableList<Category>>? =
        local.getList()
            .map { t->
                val add = Category();
                add.name = "添加大类";
                val t1 = t.toMutableList();
                t1.add(add);
                t1;
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun addCategory(name: String): Completable {
        category.value?.name = name;
        return local.add(category.value!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    }
}