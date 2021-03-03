package com.bajie.money.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bajie.money.R
import com.bajie.money.model.dao.CategoryDao
import com.bajie.money.model.data.BottomTabData
import com.bajie.money.model.data.Category
import com.bajie.money.model.loacal.AppDatabase
import com.bajie.money.view.EditCategoryActivity
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers

/**

 * bajie on 2021/1/4 16:04

 */
class EditCategoryViewmodel constructor(val local: CategoryDao) : ViewModel() {
    companion object {
        const val TYPE_CREATE_CHILD: Int = 0;
        const val TYPE_CREATE_PARENT = 1;
        const val TYPE_EDIT_PARENT = 3;
    }
    var type = 0;
    val title = MutableLiveData<String>("支出列别");
    val category = MutableLiveData<Category>();
    val childList = ArrayList<Category>();

    init {
        category.value = Category();
    }

    fun initData(params: HashMap<String, Int>): Completable {
        type = params[EditCategoryActivity.TYPE]!!;
        when(type) {
            TYPE_CREATE_CHILD -> title.value = "添加支出小类";
            TYPE_CREATE_PARENT -> title.value = "添加支出大类";
            TYPE_EDIT_PARENT -> title.value = "编辑支出大类";
        }
        params[EditCategoryActivity.PARENT_ID]?.run { category.value?.parentId =this; }
        params[EditCategoryActivity.ID]?.run { category.value?.id = this}
        // 如果是编辑支出大类，需要获取大类信息，和小类列表
        if(type == TYPE_EDIT_PARENT) {
            return Completable.create {
                local.getCategoryById(category.value!!.id).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe { item, error ->
                        item?.run {
                            category.value = item;
                            getChildList().subscribe { list: List<Category>?, err: Throwable? ->
                                err?.run {
                                    it.onError(Throwable("查找小类列表失败"))
                                }
                                list?.run {
                                    it.onComplete();
                                }
                            }
                        }
                        error?.run {
                            it.onError(Throwable("大类不存在"))
                        }
                    }
            }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        }
        return Completable.complete();
    }

    fun getChildList(): Single<List<Category>> {
        return local.getChildList(getCategoryId()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .map {
                childList.clear();
                childList.addAll(it);
                val category = Category();
                category.name = "添加小类";
                childList.add(category);
                childList;
            };
    }


    fun getNameLength(): Int {
        return category.value!!.name.length;
    }

    fun addCategory(name: String): Completable {
        category.value?.name = name;
        return local.add(category.value!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    }
    fun getCategoryId(): Int {
        return category.value!!.id;
    }

    fun isEditParent(): Boolean {
        return type == TYPE_EDIT_PARENT;
    }
}

class ViewModelFactory(private val application: Application): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val local = AppDatabase.getInstance(application).categoryDao();
        return modelClass.getConstructor(CategoryDao::class.java).newInstance(local);
    }

}