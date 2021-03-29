package com.bajie.money.view.fragment

import androidx.lifecycle.ViewModelProvider
import com.bajie.money.R
import com.bajie.money.databinding.FragmentRecordHomeBinding
import com.bajie.money.viewmodel.RecordHomeViewmodel
/**

 * bajie on 2021/3/29 15:43

 */
class RecordListFragment: BaseFragment<FragmentRecordHomeBinding, RecordHomeViewmodel>() {
    override fun getLayout(): Int {
        return R.layout.fragment_record_list;
    }

    override fun init() {
    }

    override fun getViewModel(): RecordHomeViewmodel {
        return ViewModelProvider(this).get(RecordHomeViewmodel::class.java);
    }
}