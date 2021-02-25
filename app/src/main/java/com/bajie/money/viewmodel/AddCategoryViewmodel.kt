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
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers

/**

 * bajie on 2021/1/4 16:04

 */
class AddCategoryViewmodel constructor(val local: CategoryDao) : ViewModel() {
    val title = MutableLiveData<String>("支出列别");
    val category = MutableLiveData<Category>();

    init {
        category.value = Category();
    }

    fun setTitle(parentId: Int) {
        title.value = if(parentId == -1) "添加支出大类" else "添加支出小类";
        category.value?.parentId = parentId;
    }

    fun addCategory(name: String): Completable {
        category.value?.name = name;
        return local.add(category.value!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    }
}

class ViewModelFactory(private val application: Application): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val local = AppDatabase.getInstance(application).categoryDao();
        return modelClass.getConstructor(CategoryDao::class.java).newInstance(local);
    }

}