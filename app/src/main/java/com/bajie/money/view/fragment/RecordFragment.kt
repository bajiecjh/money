package com.bajie.money.view.fragment

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bajie.money.R
import com.bajie.money.databinding.FragmentRecordBinding
import com.bajie.money.viewmodel.RecordViewmodel

/**

 * bajie on 2021/3/26 17:35

 */
class RecordFragment: BaseFragment<FragmentRecordBinding, RecordViewmodel>() {
    override fun getLayout(): Int {
        return R.layout.fragment_record;
    }

    override fun init() {
        mBinding.pager.adapter = MyAdapter();
    }

    override fun getViewModel(): RecordViewmodel {
        return ViewModelProvider(this).get(RecordViewmodel::class.java);
    }

    public fun showRecordListFragment() {
        mBinding.pager.currentItem = 2;
    }

    inner class MyAdapter(): FragmentStateAdapter(this) {
        override fun getItemCount(): Int {
            return 2;
        }

        override fun createFragment(position: Int): Fragment {
            when(position) {
                0 -> return RecordHomeFragment()
                1 -> return RecordListFragment()
            }
            return RecordHomeFragment()
        }

    }
}