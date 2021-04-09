package com.bajie.money.view.fragment

import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bajie.money.BR
import com.bajie.money.R
import com.bajie.money.databinding.FragmentRecordHomeBinding
import com.bajie.money.databinding.FragmentRecordHomeHeaderBinding
import com.bajie.money.model.data.Record
import com.bajie.money.view.BaseRecyclerViewAdapter
import com.bajie.money.view.BaseViewHolder
import com.bajie.money.view.activity.CategoryActivity
import com.bajie.money.view.activity.HomeActivity
import com.bajie.money.view.widget.EndlessRecyclerOnScrollListener
import com.bajie.money.viewmodel.RecordHomeViewmodel
import com.bajie.money.viewmodel.SharedViewModel
import com.bajie.money.viewmodel.ViewModelFactoryWRecord

/**
 * bajie on 2021/3/29 15:43
 */
class RecordFragment: BaseFragment<FragmentRecordHomeBinding, RecordHomeViewmodel>(),
    View.OnClickListener {

    private val shareDataViewModel: SharedViewModel by lazy {
        ViewModelProvider(activity!!).get(SharedViewModel::class.java);
    }
    private val mAdapter: MAdapter by lazy {
        MAdapter();
    }

    override fun getLayout(): Int {
        return R.layout.fragment_record_home;
    }

    override fun init() {

        mBinding.click2Add.setOnClickListener(this)
        mBinding.arrow.setOnClickListener(this)

        mBinding.list.layoutManager = LinearLayoutManager(this.context);
        mBinding.list.adapter = mAdapter
        mBinding.list.addOnScrollListener(object : EndlessRecyclerOnScrollListener(){
            override fun scrollToEnd() {
                CategoryActivity.startForResult(this@RecordFragment, 0, 100);
            }
        })

        mViewModel.getFiveRecords().subscribe { t1, _ ->
            t1?.let {
                mAdapter.refresh(t1!!);
            }
        }
        mViewModel.getMonthSpending();
        mViewModel.getMonthIn()

        shareDataViewModel.addedRecord.observe(viewLifecycleOwner, Observer<Record> {record ->
            mViewModel.addNewRecord(record).subscribe { t1, _ ->
                mAdapter.refresh(t1)
            }
        })

    }

    override fun getViewModel(): RecordHomeViewmodel {
        return ViewModelProvider(this, ViewModelFactoryWRecord(this.activity!!.application)).get(RecordHomeViewmodel::class.java);
    }

    companion object {
        const val VIEW_TYPE_HEADER = 0;
        const val VIEW_TYPE_ITEM = 1;
        const val VIEW_TYPE_FOOTER = 2;
    }
    inner class MAdapter: BaseRecyclerViewAdapter<ViewDataBinding>(context!!) {

        private val mData = ArrayList<Record>();

        override fun getItemCount(): Int {
            return mData.size + 2;
        }

        override fun onBindViewHolder(holder: BaseViewHolder<ViewDataBinding>, position: Int) {
            when(getItemViewType(position)) {
                VIEW_TYPE_HEADER -> {
                    val headerBinding = holder.binding as FragmentRecordHomeHeaderBinding
                    headerBinding.setVariable(BR.vm, mViewModel);
                    headerBinding.bookkeeping.setOnClickListener(this@RecordFragment)
                }
                VIEW_TYPE_ITEM -> {
                    holder.binding.setVariable(BR.record, mData[position-1]);
                }
            }
            holder.binding.executePendingBindings();
        }

        override fun getLayout(viewType: Int): Int {
            return when(viewType) {
                VIEW_TYPE_HEADER -> R.layout.fragment_record_home_header
                VIEW_TYPE_FOOTER -> R.layout.fragment_record_home_footer
                else -> R.layout.item_record;
            }
        }

        override fun getItemViewType(position: Int): Int {
            return when (position) {
                0 ->  VIEW_TYPE_HEADER
                mData.size + 1 -> VIEW_TYPE_FOOTER
                else -> VIEW_TYPE_ITEM
            }
        }

        fun refresh(list: MutableList<Record>) {
            mData.clear();
            mData.addAll(list)
            notifyDataSetChanged();
        }

    }

    override fun onClick(v: View?) {
        when(v!!.id) {
            R.id.click_2_add, R.id.bookkeeping -> (activity as HomeActivity).showBookkeepingFragment();
            R.id.arrow ->  CategoryActivity.startForResult(this@RecordFragment, 0, 100);
        }
    }
}

