package com.bajie.money.viewmodel

import androidx.lifecycle.ViewModel
import com.bajie.money.R
import com.bajie.money.model.dao.RecordDao
import com.bajie.money.model.data.BottomTabData

/**

 * bajie on 2021/1/4 16:04

 */
class RecordHomeViewmodel(val local: RecordDao) : ViewModel() {

    fun init() {

    }

//    var testNum = 0;
    var currentIndex = 0;
    val bottomTabData = List<BottomTabData>(4) { i: Int ->
        when (i) {
            0 -> BottomTabData("账本", R.drawable.icon_tab_hat, R.drawable.icon_tab_hat_unselected, true);
            1 -> BottomTabData("记账", R.drawable.icon_tab_heels, R.drawable.icon_tab_heels_unselected, false);
            2 -> BottomTabData("标签三", R.drawable.icon_tab_panties, R.drawable.icon_tab_panties_unseleted, false);
            3 -> BottomTabData("标签四", R.drawable.icon_tab_sock, R.drawable.icon_tab_sock_unselected, false);
            else -> BottomTabData("标签四", R.drawable.icon_tab_sock, R.drawable.icon_tab_sock_unselected, false);
        }
    };

    fun changeTabSelected(selected: Int): Boolean {
        if(currentIndex == selected) return false;
        bottomTabData.forEachIndexed { index, bottomTabData ->
            bottomTabData.isSelected.set(index == selected);
        }
        currentIndex = selected;
        return true
    }
}