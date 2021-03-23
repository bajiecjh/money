package com.bajie.money.view.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.bajie.money.BR
import com.bajie.money.R
import com.bajie.money.databinding.FragmentBookkeepingChildBinding
import com.bajie.money.databinding.ItemCommonlyBinding
import com.bajie.money.model.data.Category
import com.bajie.money.view.BaseRecyclerViewAdapter
import com.bajie.money.view.BaseViewHolder
import com.bajie.money.view.activity.CategoryActivity
import com.bajie.money.viewmodel.BookkeepingChildViewmodel
import com.bajie.money.viewmodel.ViewModelFactory

/**

 * bajie on 2021/2/1 14:47

 */
class BookkeepingChildFragment : BaseFragment<FragmentBookkeepingChildBinding>(), View.OnClickListener {
    private lateinit var mViewModel: BookkeepingChildViewmodel;
    private val mCommonlyAdapter: CommonlyAdapter by lazy {
        CommonlyAdapter(this!!.context!!);
    }

    companion object {
        const val REQUEST_CODE_EDIT_CATEGORY = 100;
    }

    override fun getLayout(): Int {
        return R.layout.fragment_bookkeeping_child;
    }
    override fun init() {
        mViewModel = ViewModelProvider(this, ViewModelFactory(activity?.application!!)).get(BookkeepingChildViewmodel::class.java);
        mBinding.vm = mViewModel;

        mViewModel.init();
        mViewModel.category.observe(this,
            Observer<Category> { t ->
                if(t != null) {
                    mBinding.category.setOnClickListener(this@BookkeepingChildFragment);
                } else {
                    mBinding.noCategory.setOnClickListener(this@BookkeepingChildFragment);
                }
            })
        mViewModel.commonlyList.observe(this,
            Observer<ArrayList<Category>> { t ->
                if(t!!.isEmpty()) {
                    mBinding.noCommonly.setOnClickListener(this@BookkeepingChildFragment);
                } else {
                    mBinding.commonlyList.layoutManager = GridLayoutManager(context, 4);
                    mBinding.commonlyList.adapter = mCommonlyAdapter;
                }
            })
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_EDIT_CATEGORY) {
            data?.getSerializableExtra("category")?.let {
                mViewModel.setNewCategory(it as Category);
                mCommonlyAdapter.notifyDataSetChanged();
            };
            if(mViewModel.category.value ==null) {
                mViewModel.getDefaultCategory();
            }
            mViewModel.getCommonlyList();
        }
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.category,R.id.noCategory, R.id.no_commonly -> CategoryActivity.startForResult(this, REQUEST_CODE_EDIT_CATEGORY);
        }
    }

    inner class CommonlyAdapter(context: Context) : BaseRecyclerViewAdapter<ItemCommonlyBinding>(context){
        override fun getLayout(): Int {
            return R.layout.item_commonly;
        }

        override fun getItemCount(): Int {
            return mViewModel.getCommonlyListSize();
        }

        override fun onBindViewHolder(holder: BaseViewHolder<ItemCommonlyBinding>, position: Int) {
            val data = mViewModel.commonlyList.value!![position];
            holder.binding.setVariable(BR.category, data);
            holder.binding.setVariable(BR.isSelected, mViewModel.isSame(position))
            holder.binding.executePendingBindings();
            holder.itemView.setOnClickListener {
                mViewModel.setNewCategory(mViewModel.getCommonlyItem(position));
                notifyDataSetChanged();
            }
        }

    }
}
