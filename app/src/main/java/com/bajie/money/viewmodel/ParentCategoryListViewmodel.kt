package com.bajie.money.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bajie.money.model.dao.CategoryDao
import com.bajie.money.model.data.Category
import com.bajie.money.utils.Canstant
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**

 * bajie on 2021/1/4 16:04

 */
class ParentCategoryListViewmodel constructor(val local: CategoryDao) : ViewModel() {
    var title = "支出列表管理";
    var type = 0;   // 0:支出，1：收入

    val category = MutableLiveData<Category>();

    init {
        category.value = Category();
    }
    fun init(type: Int) {
        this.type = type;
        title = if(isOutType()) "支出类别管理" else "收入类别管理"
    }

    fun isOutType(): Boolean {
        return this.type == Canstant.OUT_TYPE;
    }

    fun getOutParentList(): Single<MutableList<Category>> {
        return local.getParentList()
            .map { t->
                val add = Category();
                add.name = "添加大类";
                val t1 = t.toMutableList();
                t1.add(add);
                t1;
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun getInList(): Single<MutableList<Category>> {
        return local.getInTypeList()
            .map { t ->
                val add = Category();
                add.name = "添加类别";
                val t1 = t.toMutableList();
                t1.add(add);
                t1;
            } .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun getList(): Single<MutableList<Category>> {
        return if (isOutType()) getOutParentList() else getInList();
    }

    fun addCategory(name: String): Completable {
        category.value?.name = name;
        return local.add(category.value!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    }
}