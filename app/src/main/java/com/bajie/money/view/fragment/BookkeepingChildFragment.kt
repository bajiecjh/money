package com.bajie.money.view.fragment

import android.app.Activity
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.bajie.money.R
import com.bajie.money.databinding.FragmentBookkeepingChildBinding
import com.bajie.money.view.CategoryActivity
import com.bajie.money.viewmodel.BookkeepingChildViewmodel
import com.bajie.money.viewmodel.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_bookkeeping_child.view.*

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
        mViewModel = ViewModelProvider(this, ViewModelFactory(activity?.application!!)).get(BookkeepingChildViewmodel::class.java);
        mBinding.vm = mViewModel;

        mViewModel.getDefaultCategory().subscribe { category, err ->
            category?.let {
                mBinding.typeName.text = category.name;
                mBinding.commonly.visibility = if(category.commonly == 1) View.GONE else View.VISIBLE;
            }
        }

        mBinding.category.setOnClickListener(this);

    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.category -> CategoryActivity.start((context as Activity?)!!);
        }
    }
}
