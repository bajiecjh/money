package com.bajie.money.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bajie.money.R
import com.bajie.money.model.dao.CategoryDao
import com.bajie.money.model.data.BottomTabData
import com.bajie.money.model.data.Category
import com.bajie.money.model.loacal.AppDatabase
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers

/**

 * bajie on 2021/1/4 16:04

 */
class CategoryViewmodel constructor(val local: CategoryDao) : ViewModel() {

    fun getList(): Single<ArrayList<Category>>? =
        local.getList()
//            .doOnSuccess {
//                System.out.println("test")
//            }
            .map { t ->
                val array = ArrayList<Category>();
                array.addAll(t);
                val commonly = Category();
                commonly.name = "常用";
                array?.add(0, commonly);
                val add = Category();
                add.name = "添加";
                array.add(add);
                array;
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())



//    val commonly = Category();
//    commonly.name = "常用";
//    list?.add(0, commonly);

//    var testNum = 0;
//    var currentIndex = 0;
//    val bottomTabData = List<BottomTabData>(4) { i: Int ->
//        when (i) {
//            0 -> BottomTabData("账本", R.drawable.icon_tab_hat, R.drawable.icon_tab_hat_unselected, true);
//            1 -> BottomTabData("记账", R.drawable.icon_tab_heels, R.drawable.icon_tab_heels_unselected, false);
//            2 -> BottomTabData("标签三", R.drawable.icon_tab_panties, R.drawable.icon_tab_panties_unseleted, false);
//            3 -> BottomTabData("标签四", R.drawable.icon_tab_sock, R.drawable.icon_tab_sock_unselected, false);
//            else -> BottomTabData("标签四", R.drawable.icon_tab_sock, R.drawable.icon_tab_sock_unselected, false);
//        }
//    };
//
//    fun changeTabSelected(selected: Int): Boolean {
//        if(currentIndex == selected) return false;
//        bottomTabData.forEachIndexed { index, bottomTabData ->
//            bottomTabData.isSelected.set(index == selected);
//        }
//        currentIndex = selected;
//        return true
//    }
}

class CategoryViewModelFactory(private val application: Application): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val local = AppDatabase.getInstance(application).categoryDao();
        return modelClass.getConstructor(CategoryDao::class.java).newInstance(local);
    }

}