package com.bajie.money.view.fragment

import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.bajie.money.R
import com.bajie.money.databinding.FragmentRecordHomeBinding
import com.bajie.money.model.data.MonthRecord
import com.bajie.money.viewmodel.RecordListViewModel
import com.bajie.money.viewmodel.ViewModelFactoryWRecord

/**

 * bajie on 2021/3/29 15:43

 */
class RecordListFragment: BaseFragment<FragmentRecordHomeBinding, RecordListViewModel>() {
    override fun getLayout(): Int {
        return R.layout.fragment_record_list;
    }

    override fun init() {
        mViewModel.init();
    }

    override fun getViewModel(): RecordListViewModel {
        return ViewModelProvider(this, ViewModelFactoryWRecord(this.activity!!.application)).get(RecordListViewModel::class.java);
    }

    inner class MAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        lateinit var mData: ArrayList<MonthRecord>
        var mCurrentOpenGroupIndex = -1;    // 一次只能展开一项
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        }

        override fun getItemViewType(position: Int): Int {
            return super.getItemViewType(position)
        }

        override fun getItemCount(): Int {
            var itemCount = mData.size;
            if(mCurrentOpenGroupIndex >= 0) {
                itemCount += mData[mCurrentOpenGroupIndex].records.size
            }
            return itemCount;
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        }

        public fun setData(data: ArrayList<MonthRecord>) {
            this.mData = data;
            notifyDataSetChanged();
        }

        private fun getItemStatusByPosition(position: Int) {
            val itemStatus = ItemStatus();
            var itemCount = 0;
            for((index, item) in mData.withIndex()) {

            }
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