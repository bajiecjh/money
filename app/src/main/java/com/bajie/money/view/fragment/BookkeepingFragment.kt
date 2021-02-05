package com.bajie.money.view.fragment

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.bajie.money.R
import com.bajie.money.databinding.FragmentBookkeepingBinding
import com.bajie.money.databinding.FragmentBookkeepingChildBinding
import com.bajie.money.viewmodel.BookkeepingViewmodel

/**

 * bajie on 2021/2/1 14:47

 */
class BookkeepingFragment : BaseFragment<FragmentBookkeepingBinding>(), View.OnClickListener {
    private lateinit var mViewModel: BookkeepingViewmodel;
    override fun getLayout(): Int {
        return R.layout.fragment_bookkeeping;
    }

    override fun init() {
        mBinding.pager.adapter = MyAdapter(this);
        mViewModel = ViewModelProvider(this).get(BookkeepingViewmodel::class.java);
        mBinding.vm = mViewModel;
        activity!!.findViewById<TextView>(R.id.income).setOnClickListener(this);
        mBinding.expenditure.setOnClickListener{ scrollToPage(0)};
        mBinding.income.setOnClickListener{scrollToPage(1)};
        mBinding.pager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position);
                mViewModel.changeTabSelected(position);
            }
        })
    }

    override fun onClick(v: View?) {
        when(v?.id) {
        }
    }

    private fun scrollToPage(toPage: Int) {
        if(mViewModel.changeTabSelected(toPage)) {
            mBinding.pager.currentItem = toPage;
        }
    }
}

class MyAdapter(fragment: Fragment): FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return 2;
    }

    override fun createFragment(position: Int): Fragment {
        val fragment = BookkeepingChildFragment().apply {
            arguments = Bundle().apply {
                putInt(BookkeepingChildFragment.PARAMS, position);
            }
        };
        return fragment;
    }
}

