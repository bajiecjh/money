package com.bajie.money.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bajie.money.model.dao.CategoryDao
import com.bajie.money.model.loacal.AppDatabase

/**

 * bajie on 2021/1/4 16:04

 */
class BookkeepingChildViewmodel constructor(val local: CategoryDao) : ViewModel() {
    // 0 支出，1收入
    var position = 0;

    companion object {
        const val POSITION = "position";
    }

    fun getDefaultCategory() {

    }

    fun isCommonly() {

    }

//    var testNum = 0;
//    var currentIndex = 0;
//    val tabData = List<BottomTabData>(2) { i: Int ->
//        when (i) {
//            0 -> BottomTabData("支出", 0, 0, true);
//            1 -> BottomTabData("收入", 0, 0, false);
//            else -> BottomTabData("收入", 0, 0, false);
//        }
//    };
//
//    fun changeTabSelected(selected: Int): Boolean {
//        if(currentIndex == selected) return false;
//        tabData.forEachIndexed { index, bottomTabData ->
//            bottomTabData.isSelected.set(index == selected);
//        }
//        currentIndex = selected;
//        return true
//    }

    class ViewModelFactory(private val context: Context): ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            val local = AppDatabase.getInstance(context).categoryDao();
            return modelClass.getConstructor(CategoryDao::class.java).newInstance(local);
        }
    }
}