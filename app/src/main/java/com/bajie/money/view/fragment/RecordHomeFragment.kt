package com.bajie.money.view.fragment

import androidx.lifecycle.ViewModelProvider
import com.bajie.money.R
import com.bajie.money.databinding.FragmentRecordHomeBinding
import com.bajie.money.databinding.ItemCommonlyBinding
import com.bajie.money.view.BaseRecyclerViewAdapter
import com.bajie.money.view.BaseViewHolder
import com.bajie.money.viewmodel.RecordHomeViewmodel
import com.bajie.money.viewmodel.ViewModelFactoryWCategoryRecord

/**
 * bajie on 2021/3/29 15:43
 */
class RecordHomeFragment: BaseFragment<FragmentRecordHomeBinding, RecordHomeViewmodel>() {
    override fun getLayout(): Int {
        return R.layout.fragment_record_home;
    }

    override fun init() {
    }

    override fun getViewModel(): RecordHomeViewmodel {
        return ViewModelProvider(this, ViewModelFactoryWCategoryRecord(this.activity!!.application)).get(RecordHomeViewmodel::class.java);
    }

    inner class MyAdapter(): BaseRecyclerViewAdapter<ItemCommonlyBinding>(this.context!!) {
        override fun getItemCount(): Int {
            return 5;
        }

        override fun onBindViewHolder(holder: BaseViewHolder<ItemCommonlyBinding>, position: Int) {

        }

        override fun getLayout(): Int {
            return R.layout.item_commonly;
        }

    }
}