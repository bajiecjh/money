package com.bajie.money.view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

/**

 * bajie on 2021/3/23 14:52

 */

open abstract class BaseRecyclerViewAdapter<T: ViewDataBinding>(context: Context): RecyclerView.Adapter<BaseViewHolder<T>>() {

    private val mLayoutInflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater;

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<T> {
        val layout = getLayout(viewType);
        val binding: T = DataBindingUtil.inflate(mLayoutInflater, getLayout(viewType), parent, false);
        return BaseViewHolder(binding);
    }

    abstract override fun getItemCount(): Int;

    abstract override fun onBindViewHolder(holder: BaseViewHolder<T>, position: Int);
    abstract fun getLayout(viewType: Int): Int;
}