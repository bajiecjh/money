package com.bajie.money.view.varietyAdapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder

/**

 * bajie on 2021/4/28 12:26

 */
class VarietyAdapter(
    private var proxyList: MutableList<Proxy<*, *>> = mutableListOf(),
    var dataList: MutableList<Any> = mutableListOf()
) : RecyclerView.Adapter<ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return proxyList[viewType].onCreateViewHolder(parent, viewType);
    }

    override fun getItemCount(): Int = dataList.size;

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemViewType(position: Int): Int {

    }


    abstract class Proxy<T, VH: ViewHolder> {
        abstract fun onCreateViewHolder(parent: ViewGroup, viewType: Int):ViewHolder
        abstract fun onBindViewHolder(holder: VH, position: Int)
    }
}
