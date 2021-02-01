package com.bajie.money.model.data

import androidx.databinding.ObservableField

/**

 * bajie on 2021/1/29 10:27

 */
data class BottomTabData(val title: String, val selected: Int, val unselected: Int, val isSelect: Boolean) {
    val isSelected = ObservableField<Boolean>(isSelect);
}