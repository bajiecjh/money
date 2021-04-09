package com.bajie.money.view.fragment

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.bajie.money.BR
import com.bajie.money.R
import com.bajie.money.databinding.*
import com.bajie.money.model.data.MonthRecord
import com.bajie.money.view.BaseRecyclerViewAdapter
import com.bajie.money.view.BaseViewHolder
import com.bajie.money.view.activity.CategoryActivity
import com.bajie.money.viewmodel.RecordListViewModel
import com.bajie.money.viewmodel.ViewModelFactoryWRecord

/**

 * bajie on 2021/3/29 15:43

 */
class RecordListFragment: BaseFragment<FragmentRecordListBinding, RecordListViewModel>() {
    private val mAdapter: MAdapter by lazy { MAdapter() }

    override fun getLayout(): Int {
        return R.layout.fragment_record_list;
    }

    override fun init() {
        CategoryActivity.startForResult(this, 0, 100);
        mViewModel.init();
        mViewModel.isLoadMonthRecordFinished.observe(this,  Observer<Boolean> {newValue ->
            if(newValue) {
                mBinding.list.layoutManager = LinearLayoutManager(context);
                mBinding.list.adapter = mAdapter;
                mAdapter.setData(mViewModel.monthRecords);
            }
        })
    }

    override fun getViewModel(): RecordListViewModel {
        return ViewModelProvider(this, ViewModelFactoryWRecord(this.activity!!.application)).get(RecordListViewModel::class.java);
    }

    inner class MyAdapter: BaseRecyclerViewAdapter<ItemRecordSubBinding>(context!!) {
        override fun getItemCount(): Int {
           return 100
        }

        override fun onBindViewHolder(holder: BaseViewHolder<ItemRecordSubBinding>, position: Int) {
            holder.binding.setVariable(BR.txt, position.toString())
        }

        override fun getLayout(viewType: Int): Int {
            return R.layout.item_record_sub
        }

    }

    inner class MAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        lateinit var mData: ArrayList<MonthRecord>
        var mCurrentOpenGroupIndex = 0;    // 一次只能展开一项
        private val mLayoutInflater: LayoutInflater = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater;

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            lateinit var binding: ViewBinding;
            if(viewType == ItemStatus.VIEW_TYPE_GROUP_ITEM) {
                binding = DataBindingUtil.inflate(mLayoutInflater, R.layout.item_record_header, parent,false);
                return BaseViewHolder<ItemRecordHeaderBinding>(binding as ItemRecordHeaderBinding);
            } else if(viewType == ItemStatus.VIEW_TYPE_SUB_ITEM) {
                binding = DataBindingUtil.inflate(mLayoutInflater, R.layout.item_record_sub, parent,false);
                return BaseViewHolder<ItemRecordSubBinding>(binding as ItemRecordSubBinding);
            }
            return BaseViewHolder<ItemRecordHeaderBinding>(binding as ItemRecordHeaderBinding);
        }

        override fun getItemViewType(position: Int): Int {
            return if(mCurrentOpenGroupIndex < 0) {
                ItemStatus.VIEW_TYPE_GROUP_ITEM
            } else if(position > mCurrentOpenGroupIndex && position <= mData[mCurrentOpenGroupIndex].records.size + mCurrentOpenGroupIndex) {
                ItemStatus.VIEW_TYPE_SUB_ITEM;
            } else {
                ItemStatus.VIEW_TYPE_GROUP_ITEM
            }
        }

        override fun getItemCount(): Int {
            var itemCount = mData.size;
            if(mCurrentOpenGroupIndex >= 0) {
                itemCount += mData[mCurrentOpenGroupIndex].records.size
            }
            return itemCount;
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val itemStatus = getItemStatusByPosition(position);

            if(itemStatus.viewType == ItemStatus.VIEW_TYPE_GROUP_ITEM) {
                val monthRecord = mData[itemStatus.groupItemIndex];
                val newHolder = holder as BaseViewHolder<ItemRecordHeaderBinding>;
                newHolder.binding.setVariable(BR.monthRecord, monthRecord)
                newHolder.binding.setVariable(BR.balance, monthRecord.income - monthRecord.outlay)
                newHolder.binding.setVariable(BR.isOpen, mCurrentOpenGroupIndex>=0&&mCurrentOpenGroupIndex == itemStatus.groupItemIndex)
                newHolder.binding.reference.measure(0, 0);
                newHolder.binding.reference.post {
                    val maxWidth = newHolder.binding.reference.measuredWidth;
                    val incomeLP = newHolder.binding.incomeView.layoutParams;
                    val income = if(monthRecord.income>0)monthRecord.income else 1.0f
                    incomeLP.width = (income * maxWidth / mViewModel.maxPrice).toInt();
                    newHolder.binding.incomeView.layoutParams = incomeLP
                    val outlayLP = newHolder.binding.outlayView.layoutParams;
                    val outlay = if(monthRecord.outlay>0)monthRecord.outlay else 1.0f
                    outlayLP.width = (outlay * maxWidth / mViewModel.maxPrice).toInt();
                    newHolder.binding.outlayView.layoutParams = outlayLP
                }


                holder.binding.executePendingBindings();
            }

            holder.itemView.setOnClickListener{

                if(itemStatus.viewType == ItemStatus.VIEW_TYPE_GROUP_ITEM) {
                    // 点中已经打开的组,关闭当前组
                    mCurrentOpenGroupIndex = if(itemStatus.groupItemIndex == mCurrentOpenGroupIndex) {
                        -1
                    } else {    // 点中未打开的组
                        itemStatus.groupItemIndex;
                    }
                }
                notifyDataSetChanged();
            }

        }

        public fun setData(data: ArrayList<MonthRecord>) {
            this.mData = data;
            notifyDataSetChanged();
        }

        private fun getItemStatusByPosition(position: Int): ItemStatus {
            val itemStatus = ItemStatus();
            if(mCurrentOpenGroupIndex < 0) {    // 没有展开项，全部都是组项
                itemStatus.viewType = ItemStatus.VIEW_TYPE_GROUP_ITEM;
                itemStatus.groupItemIndex = position;
                return itemStatus;
            }
            // 子项
            if(position > mCurrentOpenGroupIndex && position <= mData[mCurrentOpenGroupIndex].records.size + mCurrentOpenGroupIndex) {
                itemStatus.viewType = ItemStatus.VIEW_TYPE_SUB_ITEM;
                itemStatus.groupItemIndex = mCurrentOpenGroupIndex;
                itemStatus.subItemIndex = position - mCurrentOpenGroupIndex - 1;
            } else {    // 组项
                itemStatus.viewType = ItemStatus.VIEW_TYPE_GROUP_ITEM;
                // 如果组项位于展开项下面，groupItemIndex的值要减去 展开项的子项数
                itemStatus.groupItemIndex = if(position<= mCurrentOpenGroupIndex) position else position - mData[mCurrentOpenGroupIndex].records.size;
            }
            return itemStatus

        }
    }
}

class ItemStatus() {
    companion object {
        const val VIEW_TYPE_GROUP_ITEM = 0;
        const val VIEW_TYPE_SUB_ITEM = 1;
    }
    var viewType = 0;
    var groupItemIndex = 0;
    var subItemIndex = 0;
}