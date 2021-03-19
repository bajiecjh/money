package com.bajie.money.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bajie.money.model.dao.CategoryDao
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