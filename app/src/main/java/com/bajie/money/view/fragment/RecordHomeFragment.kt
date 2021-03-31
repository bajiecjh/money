package com.bajie.money.view.fragment

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bajie.money.BR
import com.bajie.money.R
import com.bajie.money.databinding.FragmentRecordHomeBinding
import com.bajie.money.databinding.ItemRecordBinding
import com.bajie.money.model.data.Record
import com.bajie.money.view.BaseRecyclerViewAdapter
import com.bajie.money.view.BaseViewHolder
import com.bajie.money.viewmodel.RecordHomeViewmodel
import com.bajie.money.viewmodel.ViewModelFactoryWRecord

/**
 * bajie on 2021/3/29 15:43
 */
class RecordHomeFragment: BaseFragment<FragmentRecordHomeBinding, RecordHomeViewmodel>() {
    private val mAdapter: MyAdapter by lazy {
        MyAdapter();
    }

    override fun getLayout(): Int {
        return R.layout.fragment_record_home;
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun init() {
        mBinding.list.layoutManager = LinearLayoutManager(this.context);
        mBinding.list.adapter = mAdapter
        mViewModel.getFiveRecords().subscribe { t1, t2 ->
            t1?.let {
                mAdapter.refresh(t1!!);
            }
        }
        mViewModel.getMonthSpending();

    }

    override fun getViewModel(): RecordHomeViewmodel {
        return ViewModelProvider(this, ViewModelFactoryWRecord(this.activity!!.application)).get(RecordHomeViewmodel::class.java);
    }

    inner class MyAdapter(): BaseRecyclerViewAdapter<ItemRecordBinding>(this.context!!) {
        private val dataList = ArrayList<Record>();
        override fun getItemCount(): Int {
            return dataList.size;
        }

        override fun onBindViewHolder(holder: BaseViewHolder<ItemRecordBinding>, position: Int) {
            holder.binding.setVariable(BR.record, dataList[position]);
            holder.binding.executePendingBindings();
        }

        override fun getLayout(): Int {
            return R.layout.item_record;
        }

        fun refresh(list: ArrayList<Record>) {
            dataList.clear();
            dataList.addAll(list)
            notifyDataSetChanged();
        }

    }
}