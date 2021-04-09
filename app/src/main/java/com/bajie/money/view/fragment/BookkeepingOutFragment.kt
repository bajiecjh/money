package com.bajie.money.view.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.bajie.money.BR
import com.bajie.money.R
import com.bajie.money.databinding.FragmentBookkeepingChildBinding
import com.bajie.money.databinding.ItemCommonlyBinding
import com.bajie.money.model.data.Category
import com.bajie.money.utils.SoftInputUtil
import com.bajie.money.view.BaseRecyclerViewAdapter
import com.bajie.money.view.BaseViewHolder
import com.bajie.money.view.activity.CategoryActivity
import com.bajie.money.view.activity.TimePickerActivity
import com.bajie.money.viewmodel.BookkeepingChildViewmodel
import com.bajie.money.viewmodel.SharedViewModel
import com.bajie.money.viewmodel.ViewModelFactoryWCategoryRecord

/**

 * bajie on 2021/2/1 14:47

 */
class BookkeepingOutFragment(val type: Int) : BaseFragment<FragmentBookkeepingChildBinding, BookkeepingChildViewmodel>(), View.OnClickListener,
    TextView.OnEditorActionListener {

    val shareDataViewModel: SharedViewModel by lazy {
        ViewModelProvider(activity!!).get(SharedViewModel::class.java);
    }

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
        mViewModel.init(type);
        mViewModel.category.observe(this,
            Observer<Category> { t ->
                if(t != null) {
                    mBinding.category.setOnClickListener(this@BookkeepingOutFragment);
                    mBinding.more.setOnClickListener(this);
                    mBinding.setAsCommonly.setOnClickListener(this);
                    mBinding.priceEdit.requestFocus();
                    mBinding.save.setOnClickListener(this);
                    mBinding.time.setOnClickListener(this);
                    mBinding.priceEdit.setOnEditorActionListener(this)
                    mBinding.hint.setOnEditorActionListener(this)
                    SoftInputUtil.showSoftInput(mBinding.priceEdit);
                } else {
                    mBinding.noCategory.setOnClickListener(this@BookkeepingOutFragment);
                }
            })
        mViewModel.commonlyList.observe(this,
            Observer<ArrayList<Category>> { t ->
                if(t == null || t.isEmpty()) {
                    mBinding.noCommonly.setOnClickListener(this@BookkeepingOutFragment);
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
            R.id.category,R.id.noCategory, R.id.no_commonly, R.id.more -> CategoryActivity.startForResult(this, mViewModel.type,REQUEST_CODE_EDIT_CATEGORY);
            R.id.set_as_commonly -> mViewModel.setCategoryAsCommonly();
            R.id.save -> onSave();

            R.id.time -> TimePickerActivity.startForResult(this, REQUEST_CODE_TIME_PICKER, if(mViewModel.recordTime.value!!.equals("此刻"))"" else mViewModel.recordTime.value!!);
        }
    }

    private fun onSave() {
        val price = mBinding.priceEdit.text.toString()
        if(price.isEmpty()) {
            showToast("请输入一个有效金额");
            return
        }
        mViewModel.addRecord(price.toFloat(), mBinding.hint.text.toString())
            .subscribe { t1, t2 ->
                t2?.let {
                    showToast("添加失败")
                }
                t1?.let {
                    showToast("添加成功")
                    mBinding.priceEdit.setText("");
                    mBinding.hint.setText("");
                    shareDataViewModel.setAddedCategory(it);
                }
            }
    }

    inner class CommonlyAdapter(context: Context) : BaseRecyclerViewAdapter<ItemCommonlyBinding>(context){
        override fun getLayout(viewType: Int): Int {
            return R.layout.item_commonly;
        }

        override fun getItemCount(): Int {
            return mViewModel.getCommonlyListSize();
        }

        override fun onBindViewHolder(holder: BaseViewHolder<ItemCommonlyBinding>, position: Int) {
            val data = mViewModel.commonlyList.value!![position];
            var background = if(mViewModel.isSame(position)) {
                if (mViewModel.isOutType()) {
                    R.drawable.commonly_item_selected_bg
                } else {
                    R.drawable.commonly_in_item_selected_bg
                }
            } else {
                if (mViewModel.isOutType()) {
                    R.drawable.commonly_item_bg
                } else {
                    R.drawable.commonly_in_item_bg
                }
            }
            holder.binding.setVariable(BR.background, getDrawable(background));
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

    private fun getDrawable(drawableId: Int): Drawable? {
        return ResourcesCompat.getDrawable(resources, drawableId, null)
    }

    override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
        if(actionId == EditorInfo.IME_ACTION_NEXT) {
            mBinding.hint.requestFocus();
        } else if(actionId == EditorInfo.IME_ACTION_GO) {
            onSave();
        }
        return true;
    }
}
