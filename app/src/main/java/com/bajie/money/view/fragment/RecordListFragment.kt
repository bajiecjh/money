package com.bajie.money.view.fragment

import androidx.lifecycle.ViewModelProvider
import com.bajie.money.R
import com.bajie.money.databinding.FragmentRecordHomeBinding
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
}