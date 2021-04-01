package com.bajie.money.view.fragment

import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.bajie.money.R
import com.bajie.money.databinding.FragmentBookkeepingBinding
import com.bajie.money.utils.Canstant
import com.bajie.money.viewmodel.BookkeepingViewmodel

/**

 * bajie on 2021/2/1 14:47

 */
class BookkeepingFragment : BaseFragment<FragmentBookkeepingBinding, BookkeepingViewmodel>(), View.OnClickListener {
//    private lateinit var mViewModel: BookkeepingViewmodel;
    override fun getLayout(): Int {
        return R.layout.fragment_bookkeeping;
    }

    override fun init() {
        mBinding.pager.adapter = MyAdapter(this);
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

    override fun getViewModel(): BookkeepingViewmodel {
        return ViewModelProvider(this).get(BookkeepingViewmodel::class.java);
    }
}

class MyAdapter(fragment: Fragment): FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return 2;
    }

    override fun createFragment(position: Int): Fragment {
        return if (position == 0) BookkeepingOutFragment(Canstant.OUT_TYPE) else BookkeepingOutFragment(Canstant.IN_TYPE);
    }
}

