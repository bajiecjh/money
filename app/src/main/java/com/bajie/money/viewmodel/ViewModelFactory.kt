package com.bajie.money.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bajie.money.model.dao.CategoryDao
import com.bajie.money.model.dao.RecordDao
import com.bajie.money.model.loacal.AppDatabase

/**

 * bajie on 2021/3/5 18:06

 */
class ViewModelFactory(private val application: Application): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val local = AppDatabase.getInstance(application).categoryDao();
        return modelClass.getConstructor(CategoryDao::class.java).newInstance(local);
    }
}

class ViewModelFactoryWCategoryRecord(private val application: Application): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val category = AppDatabase.getInstance(application).categoryDao();
        val record = AppDatabase.getInstance(application).recordDao();
        return modelClass.getConstructor(CategoryDao::class.java, RecordDao::class.java).newInstance(category, record);
    }

}