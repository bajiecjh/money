package com.bajie.money.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bajie.money.BR
import com.bajie.money.R
import com.bajie.money.databinding.*
import com.bajie.money.model.data.MonthRecord
import com.bajie.money.view.BaseRecyclerViewAdapter
import com.bajie.money.view.BaseViewHolder
import com.bajie.money.viewmodel.RecordListViewModel
import com.bajie.money.viewmodel.ViewModelFactoryWRecord

/**

 * bajie on 2021/4/9 16:15

 */
class RecordListActivity: BaseActivity<ActivityRecordListBinding, RecordListViewModel>(),
    View.OnClickListener {

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

    override fun onCreate(savedInstanceState: Bundle?) {
//        overridePendingTransition(R.anim.slide_in_bottom, R.anim.silent);
        super.onCreate(savedInstanceState)
    }

    override fun finish() {
        super.finish()
//        overridePendingTransition( R.anim.bottom_silent, R.anim.bottom_out);
    }
    override fun init() {
        mBinding.header.back.setOnClickListener(this);

        mViewModel.init();
        mViewModel.isLoadMonthRecordFinished.observe(this,  Observer<Boolean> {newValue ->
            if(newValue) {
                mBinding.list.layoutManager = LinearLayoutManager(this);
                mBinding.list.adapter = mAdapter;
                mAdapter.setData(mViewModel.monthRecords);
            }
        })
    }

    override fun onClick(v: View?) {
        when(v!!.id) {
            R.id.back -> finish();
        }
    }

    override fun getViewModel(): RecordListViewModel {
        return ViewModelProvider(this, ViewModelFactoryWRecord(application)).get(RecordListViewModel::class.java);
    }
    inner class MAdapter: BaseRecyclerViewAdapter<ViewDataBinding>(this) {
        lateinit var mData: ArrayList<MonthRecord>
        private var mCurrentOpenGroupIndex = 0;    // 一次只能展开一项
        private var mItemStatusList = ArrayList<ItemStatus>();

        override fun getItemViewType(position: Int): Int {
            return mItemStatusList[position]!!.viewType;
        }

        override fun getItemCount(): Int {
            var itemCount = mData.size;
            if(mCurrentOpenGroupIndex >= 0) {
                itemCount += mData[mCurrentOpenGroupIndex].recordSize + mData[mCurrentOpenGroupIndex].dayRecords.size
            }
            return itemCount;
        }


        override fun onBindViewHolder(holder: BaseViewHolder<ViewDataBinding>, position: Int) {
            val itemStatus = mItemStatusList[position];

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
            } else if(itemStatus.viewType == ItemStatus.VIEW_TYPE_DAY) {
                val dayRecord = mData[itemStatus.monthItemIndex].dayRecords[itemStatus.day];
                val newHolder = holder as BaseViewHolder<ItemRecordDayBinding>;
                newHolder.binding.setVariable(BR.record, dayRecord);
            } else {
                val record = mData[itemStatus.monthItemIndex].dayRecords[itemStatus.day]!!.records[itemStatus.recordItemIndex];
                val newHolder = holder as BaseViewHolder<ItemRecordBinding>;
                newHolder.binding.setVariable(BR.record, record);
                newHolder.binding.setVariable(BR.setMargin, true);
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
                refreshView();
            }

            holder.binding.executePendingBindings();
        }

        override fun getLayout(viewType: Int): Int {
            return when(viewType) {
                ItemStatus.VIEW_TYPE_MONTH -> R.layout.item_record_header;
                ItemStatus.VIEW_TYPE_DAY -> R.layout.item_record_day;
                else -> R.layout.item_record
            }
        }

        open fun setData(data: ArrayList<MonthRecord>) {
            this.mData = data;
            refreshView();
        }


        private fun refreshView() {
            mItemStatusList.clear();
            for (i in 0 until itemCount) {
                mItemStatusList.add(ItemStatus());
            }
            setItemStatus()
            notifyDataSetChanged();

        }

        private fun addMonthItem(position: Int, monthIndex: Int) {
            val itemStatus = ItemStatus();
            itemStatus.viewType = ItemStatus.VIEW_TYPE_MONTH;
            itemStatus.monthItemIndex = monthIndex;
            mItemStatusList[position] = itemStatus;
        }
        private fun setItemStatus() {
            var itemStatus: ItemStatus;
            // 无展开项,全部都是月记录
            if(mCurrentOpenGroupIndex < 0) {
                for(i in 0 until itemCount) {
                    itemStatus = ItemStatus();
                    itemStatus.viewType = ItemStatus.VIEW_TYPE_MONTH;
                    itemStatus.monthItemIndex = i;
                    mItemStatusList[i] = itemStatus;
                }
                return;
            }

            val openItem = mData[mCurrentOpenGroupIndex];
            val dayOfMonth = openItem.dayRecords;

            // 在展开项上面的月记录
            for(i in 0..mCurrentOpenGroupIndex) {
                addMonthItem(i , i);
            }
            val subItemSize = openItem.recordSize + dayOfMonth.size;
            // 在展开项下面的月记录
            for(i in (subItemSize + mCurrentOpenGroupIndex+1) until itemCount) {
                addMonthItem(i, i-subItemSize)
            }

            var preRecordPosition = 0;
            var dayIndex = 0;
            for((day, dayRecords) in dayOfMonth) {
                // 日记录
                val dayPosition = mCurrentOpenGroupIndex + preRecordPosition +1;
                itemStatus = ItemStatus();
                itemStatus.viewType = ItemStatus.VIEW_TYPE_DAY;
                itemStatus.monthItemIndex = mCurrentOpenGroupIndex;
                itemStatus.day = day;
                mItemStatusList[dayPosition] = itemStatus;

                // 消费记录
                for (i in dayPosition+1..dayPosition+dayRecords.records.size) {
                    itemStatus = ItemStatus();
                    itemStatus.viewType = ItemStatus.VIEW_TYPE_RECORD;
                    itemStatus.monthItemIndex = mCurrentOpenGroupIndex;
                    itemStatus.day = day;
                    itemStatus.recordItemIndex = i - dayPosition - 1;
                    mItemStatusList[i] = itemStatus;
                }
                dayIndex ++;
                preRecordPosition += dayRecords.records.size + 1;
            }
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
    var day = 0;
    var recordItemIndex = 0;
}