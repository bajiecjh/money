package com.bajie.money.view.fragment

import android.app.Activity
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.bajie.money.R
import com.bajie.money.databinding.FragmentBookkeepingChildBinding
import com.bajie.money.view.CategoryActivity
import com.bajie.money.viewmodel.BookkeepingChildViewmodel

/**

 * bajie on 2021/2/1 14:47

 */
class BookkeepingChildFragment : BaseFragment<FragmentBookkeepingChildBinding>(), View.OnClickListener {
    private lateinit var mViewModel: BookkeepingChildViewmodel;

    companion object {
    }

    override fun getLayout(): Int {
        return R.layout.fragment_bookkeeping_child;
    }

    override fun init() {
        mViewModel = ViewModelProvider(this, BookkeepingChildViewmodel.ViewModelFactory(context!!)).get(BookkeepingChildViewmodel::class.java);
        mBinding.vm = mViewModel;

        mBinding.category.setOnClickListener(this);

    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.category -> CategoryActivity.start((context as Activity?)!!);
        }
    }
}
