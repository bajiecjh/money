package com.bajie.money.view.varietyAdapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bajie.money.view.BaseViewHolder

/**

 * bajie on 2021/4/28 12:26

 */
class VarietyAdapter(
    context: Context,
    private var proxyList: MutableList<Proxy<*, *>> = mutableListOf(),
    var dataList: MutableList<Any> = mutableListOf()
) : RecyclerView.Adapter<ViewHolder>() {
    private val mLayoutInflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater;

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DataBindingUtil.inflate<ViewDataBinding>(mLayoutInflater, proxyList[viewType].getLayout(), parent, false);
        return BaseViewHolder(binding);
    }

    override fun getItemCount(): Int = dataList.size;

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemViewType(position: Int): Int {
        TODO("Not yet implemented")
    }

    fun <T, VH: ViewHolder> addProxy(proxy: Proxy<T, VH>) {
        proxyList.add(proxy);
    }


    abstract class Proxy<T, VH: ViewHolder> {
//        abstract fun onCreateViewHolder(parent: ViewGroup, viewType: Int):BaseViewHolder<VDB>
        abstract fun getLayout(): Int;
        abstract fun onBindViewHolder(holder: VH, position: Int)
    }
}
