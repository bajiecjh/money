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
import com.bajie.money.utils.SoftInputUtil
import com.bajie.money.utils.TimeUtils
import com.bajie.money.view.BaseRecyclerViewAdapter
import com.bajie.money.view.BaseViewHolder
import com.bajie.money.view.activity.CategoryActivity
import com.bajie.money.view.activity.TimePickerActivity
import com.bajie.money.viewmodel.BookkeepingChildViewmodel
import com.bajie.money.viewmodel.ViewModelFactoryWCategoryRecord
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer

/**

 * bajie on 2021/2/1 14:47

 */
class BookkeepingChildFragment : BaseFragment<FragmentBookkeepingChildBinding, BookkeepingChildViewmodel>(), View.OnClickListener {
    private val mCommonlyAdapter: CommonlyAdapter by lazy {
        CommonlyAdapter(this!!.context!!);
    }

    companion object {
        const val REQUEST_CODE_EDIT_CATEGORY = 100;
        const val REQUEST_CODE_TIME_PICKER = 101;
    }

    override fun getLayout(): Int {
        return R.layout.fragment_bookkeeping_child;
    }

    override fun init() {

        mViewModel.init();
        mViewModel.category.observe(this,
            Observer<Category> { t ->
                if(t != null) {
                    mBinding.category.setOnClickListener(this@BookkeepingChildFragment);
                    mBinding.more.setOnClickListener(this);
                    mBinding.setAsCommonly.setOnClickListener(this);
                    mBinding.editText.requestFocus();
                    mBinding.save.setOnClickListener(this);
                    mBinding.time.setOnClickListener(this);
//                    mBinding.time.text = TimeUtils.getNowTime("MM/dd HH:mm")
//                    mBinding.saveReedit.setOnClickListener(this);
                    SoftInputUtil.showSoftInput(mBinding.editText);
                } else {
                    mBinding.noCategory.setOnClickListener(this@BookkeepingChildFragment);
                }
            })
        mViewModel.commonlyList.observe(this,
            Observer<ArrayList<Category>> { t ->
                if(t == null || t.isEmpty()) {
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
        } else if(resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_TIME_PICKER) {
            mViewModel.setRecordTime(data!!.getStringExtra("data"))
        }
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.category,R.id.noCategory, R.id.no_commonly, R.id.more -> CategoryActivity.startForResult(this, REQUEST_CODE_EDIT_CATEGORY);
            R.id.set_as_commonly -> mViewModel.setCategoryAsCommonly();
            R.id.save -> {
                val price = mBinding.editText.text.toString()
                if(price.isEmpty()) {
                    showToast("请输入一个有效金额");
                    return
                }
                mViewModel.addRecord(price.toFloat(), mBinding.hint.text.toString())
                    .subscribe(
                        Action {
                            showToast("添加成功")
                            mBinding.editText.setText("");
                            mBinding.hint.setText("");
                        },
                        Consumer<Throwable> { showToast("添加失败")})
            };
            R.id.time -> TimePickerActivity.startForResult(this, REQUEST_CODE_TIME_PICKER, mViewModel.recordTime.value!!);
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

    override fun getViewModel(): BookkeepingChildViewmodel {
        return ViewModelProvider(this, ViewModelFactoryWCategoryRecord(activity?.application!!)).get(BookkeepingChildViewmodel::class.java);
    }
}
