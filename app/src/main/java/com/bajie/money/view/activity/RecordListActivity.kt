package com.bajie.money.view.activity

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bajie.money.BR
import com.bajie.money.R
import com.bajie.money.databinding.ActivityRecordListBinding
import com.bajie.money.databinding.ItemRecordHeaderBinding
import com.bajie.money.model.data.MonthRecord
import com.bajie.money.view.BaseRecyclerViewAdapter
import com.bajie.money.view.BaseViewHolder
import com.bajie.money.viewmodel.RecordListViewModel
import com.bajie.money.viewmodel.ViewModelFactoryWRecord

/**

 * bajie on 2021/4/9 16:15

 */
class RecordListActivity: BaseActivity<ActivityRecordListBinding, RecordListViewModel>() {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, RecordListActivity::class.java);
            context.startActivity(intent);
        }
    }

    private val mAdapter: MAdapter by lazy { MAdapter() }

    override fun getLayout(): Int {
        return R.layout.activity_record_list
    }

    override fun init() {
        mViewModel.init();
        mViewModel.isLoadMonthRecordFinished.observe(this,  Observer<Boolean> {newValue ->
            if(newValue) {
                mBinding.list.layoutManager = LinearLayoutManager(this);
                mBinding.list.adapter = mAdapter;
                mAdapter.setData(mViewModel.monthRecords);
            }
        })
    }

    override fun getViewModel(): RecordListViewModel {
        return ViewModelProvider(this, ViewModelFactoryWRecord(application)).get(RecordListViewModel::class.java);
    }
    inner class MAdapter: BaseRecyclerViewAdapter<ViewDataBinding>(this) {
        lateinit var mData: ArrayList<MonthRecord>
        var mCurrentOpenGroupIndex = 0;    // 一次只能展开一项
        private val mLayoutInflater: LayoutInflater = this@RecordListActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater;

//

        override fun getItemViewType(position: Int): Int {
            return if(mCurrentOpenGroupIndex < 0) {
                ItemStatus.VIEW_TYPE_MONTH
            } else if(position > mCurrentOpenGroupIndex && position <= mData[mCurrentOpenGroupIndex].recordSize + mCurrentOpenGroupIndex) {
                ItemStatus.VIEW_TYPE_RECORD;
            } else {
                ItemStatus.VIEW_TYPE_MONTH
            }
        }

        override fun getItemCount(): Int {
            var itemCount = mData.size;
            if(mCurrentOpenGroupIndex >= 0) {
                itemCount += mData[mCurrentOpenGroupIndex].recordSize
            }
            return itemCount;
        }


        override fun onBindViewHolder(holder: BaseViewHolder<ViewDataBinding>, position: Int) {
            val itemStatus = getItemStatusByPosition(position);

            if(itemStatus.viewType == ItemStatus.VIEW_TYPE_MONTH) {
                val monthRecord = mData[itemStatus.monthItemIndex];
                val newHolder = holder as BaseViewHolder<ItemRecordHeaderBinding>;
                newHolder.binding.setVariable(BR.monthRecord, monthRecord)
                newHolder.binding.setVariable(BR.balance, monthRecord.income - monthRecord.outlay)
                newHolder.binding.setVariable(BR.isOpen, mCurrentOpenGroupIndex>=0&&mCurrentOpenGroupIndex == itemStatus.monthItemIndex)
                newHolder.binding.reference.measure(0, 0);
                newHolder.binding.reference.post {      // 设置收入支出进度条的长度
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

            }

            holder.itemView.setOnClickListener{

                if(itemStatus.viewType == ItemStatus.VIEW_TYPE_MONTH) {
                    // 点中已经打开的组,关闭当前组
                    mCurrentOpenGroupIndex = if(itemStatus.monthItemIndex == mCurrentOpenGroupIndex) {
                        -1
                    } else {    // 点中未打开的组
                        itemStatus.monthItemIndex;
                    }
                }
                notifyDataSetChanged();
            }

            holder.binding.executePendingBindings();
        }

        override fun getLayout(viewType: Int): Int {
            return when(viewType) {
                ItemStatus.VIEW_TYPE_MONTH -> R.layout.item_record_header;
                else -> R.layout.item_record_sub
            }
        }

        public fun setData(data: ArrayList<MonthRecord>) {
            this.mData = data;
            notifyDataSetChanged();
        }

        private fun getItemStatusByPosition(position: Int): ItemStatus {
            val itemStatus = ItemStatus();
            if(mCurrentOpenGroupIndex < 0) {    // 没有展开项，全部都是月记录
                itemStatus.viewType = ItemStatus.VIEW_TYPE_MONTH;
                itemStatus.monthItemIndex = position;
                return itemStatus;
            }
            // 月记录
            val subItemSize = mData[mCurrentOpenGroupIndex].recordSize + mData[mCurrentOpenGroupIndex].dayRecords.size
            if(position <= mCurrentOpenGroupIndex && position > subItemSize  + mCurrentOpenGroupIndex) {
                itemStatus.viewType = ItemStatus.VIEW_TYPE_MONTH;
                itemStatus.monthItemIndex = if (position > mCurrentOpenGroupIndex) {
                    position - subItemSize;
                } else {
                    position
                }
                return itemStatus;
            }

            // 日记录
            for((index, item) in mData[mCurrentOpenGroupIndex].dayRecords) {
                if(position == )
            }



            // 子项
            if(position > mCurrentOpenGroupIndex && position <= mData[mCurrentOpenGroupIndex].recordSize + mCurrentOpenGroupIndex) {
                itemStatus.viewType = ItemStatus.VIEW_TYPE_RECORD;
                itemStatus.monthItemIndex = mCurrentOpenGroupIndex;
                itemStatus.recordItemIndex = position - mCurrentOpenGroupIndex - 1;
            } else {    // 组项
                itemStatus.viewType = ItemStatus.VIEW_TYPE_MONTH;
                // 如果组项位于展开项下面，groupItemIndex的值要减去 展开项的子项数
                itemStatus.monthItemIndex = if(position<= mCurrentOpenGroupIndex) position else position - mData[mCurrentOpenGroupIndex].recordSize;
            }
            return itemStatus

        }




    }
}
class ItemStatus() {
    companion object {
        const val VIEW_TYPE_MONTH = 0;
        const val VIEW_TYPE_DAY = 1;
        const val VIEW_TYPE_RECORD = 2;
    }
    var viewType = 0;
    var monthItemIndex = 0;
    var dayItemIndex = 0;
    var recordItemIndex = 0;
}