package com.bajie.money.view

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

/**

 * bajie on 2021/3/23 14:47

 */
class BaseViewHolder<T: ViewDataBinding>(val binding: T): RecyclerView.ViewHolder(binding.root) {
}