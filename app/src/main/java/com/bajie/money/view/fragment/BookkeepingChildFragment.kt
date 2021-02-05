package com.bajie.money.view.fragment

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bajie.money.R
import com.bajie.money.databinding.FragmentBookkeepingBinding
import com.bajie.money.databinding.FragmentBookkeepingChildBinding
import com.bajie.money.viewmodel.BookkeepingViewmodel

/**

 * bajie on 2021/2/1 14:47

 */
class BookkeepingChildFragment : BaseFragment<FragmentBookkeepingChildBinding>(), View.OnClickListener {
    companion object {
        val PARAMS = "params";
    }

    override fun getLayout(): Int {
        return R.layout.fragment_bookkeeping_child;
    }

    override fun init() {
        arguments?.takeIf { it.containsKey(PARAMS) }?.apply {
            mBinding.text = getInt(PARAMS).toString();
        }

    }

    override fun onClick(v: View?) {
        when(v?.id) {

        }
    }
}
