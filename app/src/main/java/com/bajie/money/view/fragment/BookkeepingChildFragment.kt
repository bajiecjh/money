package com.bajie.money.view.fragment

import android.app.Activity
import android.content.Intent
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.bajie.money.R
import com.bajie.money.databinding.FragmentBookkeepingChildBinding
import com.bajie.money.model.data.Category
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
        const val REQUEST_CODE_NO_CATEGORY = 100;
    }

    override fun getLayout(): Int {
        return R.layout.fragment_bookkeeping_child;
    }
    override fun init() {
        mViewModel = ViewModelProvider(this, ViewModelFactory(activity?.application!!)).get(BookkeepingChildViewmodel::class.java);
        mBinding.vm = mViewModel;

        mViewModel.getDefaultCategory();
        mBinding.category.setOnClickListener(this);
        mBinding.noCategory.setOnClickListener(this);

    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_NO_CATEGORY) {
            mViewModel.getDefaultCategory();
        }
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.category -> CategoryActivity.start((context as Activity?)!!);
            R.id.noCategory -> CategoryActivity.startForResult(this, REQUEST_CODE_NO_CATEGORY);
        }
    }
}
