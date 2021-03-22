package com.bajie.money.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bajie.money.BR
import com.bajie.money.R
import com.bajie.money.databinding.ActivityCategoryBinding
import com.bajie.money.databinding.ItemCategoryChildBinding
import com.bajie.money.databinding.ItemCategoryParentBinding
import com.bajie.money.model.data.Category
import com.bajie.money.viewmodel.CategoryViewmodel
import com.bajie.money.viewmodel.ViewModelFactory
import kotlinx.android.synthetic.main.activity_category.view.*

/**

 * bajie on 2021/1/8 12:14

 */
class CategoryActivity: BaseActivity<ActivityCategoryBinding>(), View.OnClickListener {
    companion object {
        const val ADD_CATEGORY_CODE = 100;
        const val ADD_CHILD_CODE = 101;

        fun start(activity: Activity) {
            val intent = Intent(activity, CategoryActivity::class.java);
            activity.startActivity(intent);
        }
    }

    private lateinit var mViewModel: CategoryViewmodel;
    private lateinit var mParentAdapter: ParentListAdapter;
    private lateinit var mChildAdapter: ChildListAdapter;
    private lateinit var mLayoutInflater: LayoutInflater;

    override fun getLayout(): Int {
        return R.layout.activity_category;
    }

    override fun init() {
        mLayoutInflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater;

        mViewModel = ViewModelProvider(this, ViewModelFactory(application)).get(CategoryViewmodel::class.java);
        mBinding.vm = mViewModel;

        mBinding.parentList.layoutManager = LinearLayoutManager(this);
        mBinding.childList.layoutManager = LinearLayoutManager(this);
        mParentAdapter = ParentListAdapter();
        mChildAdapter = ChildListAdapter();
        mBinding.parentList.adapter = mParentAdapter;
        mBinding.childList.adapter = mChildAdapter;

        refreshParentList();

        mBinding.header.rightBtn.setOnClickListener(this);
        mBinding.header.back.setOnClickListener(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK) {
            when(requestCode) {
                ADD_CATEGORY_CODE -> refreshParentList();
                ADD_CHILD_CODE -> refreshChildList();
            }
        }
    }

    private fun refreshChildList() {
        mViewModel.getChildList()
            ?.subscribe { list: ArrayList<Category>?, err: Throwable? ->
                list?.run {
                        mChildAdapter.notifyDataSetChanged()
                }
                err?.run {
                    showToast("获取小类失败")
                }
            }
    }
    private fun refreshParentList() {
        mViewModel.getList()
            ?.subscribe {t1: ArrayList<Category>?, t: Throwable? ->
                t1?.run {
                    mParentAdapter.addAll(t1);
                    refreshChildList();
                }
                t?.run {
                    Toast.makeText(this@CategoryActivity, "获取列表失败", Toast.LENGTH_SHORT).show();
                }
            }
    }

    override fun onClick(v: View) {
        when(v?.id) {
            R.id.right_btn -> ParentCategoryListActivity.start(this);
            R.id.back -> finish();
        }
    }


    class ParentListViewHolder(val binding: ItemCategoryParentBinding): RecyclerView.ViewHolder(binding.root) {}
    class ChildListViewHolder(val binding: ItemCategoryChildBinding): RecyclerView.ViewHolder(binding.root) {}

    inner class ChildListAdapter(): RecyclerView.Adapter<ChildListViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChildListViewHolder {
            val binding: ItemCategoryChildBinding = DataBindingUtil.inflate(mLayoutInflater, R.layout.item_category_child, parent, false);
            return ChildListViewHolder(binding);
        }

        override fun getItemCount(): Int {
            return mViewModel.childList.size;
        }

        override fun onBindViewHolder(holder: ChildListViewHolder, position: Int) {
            val data = mViewModel.childList[position];
            holder.binding.setVariable(BR.category, data);
            holder.binding.setVariable(BR.isAddItem, mViewModel.isAddChildItem(position));
            holder.itemView.star.setOnClickListener {
                holder.itemView.star.isClickable = false;
                mViewModel.toggleCommonly(position)
                    .subscribe {
                        notifyDataSetChanged();
                        holder.itemView.star.isClickable = true;
                    }

            }
            holder.itemView.setOnClickListener{v: View? ->
                run {
                    // 点击添加
                    if(position == mViewModel.childList.size-1) {
                        EditCategoryActivity.startCreateChild(this@CategoryActivity, ADD_CHILD_CODE, mViewModel.getCurrentParentId());
                    }
                }
            }
        }


    }

    inner class ParentListAdapter(): RecyclerView.Adapter<ParentListViewHolder>() {

        private val mDataList = ArrayList<Category>();
        init {

        }
        override fun onCreateViewHolder( parent: ViewGroup, viewType: Int ): ParentListViewHolder {
            val binding: ItemCategoryParentBinding = DataBindingUtil.inflate(mLayoutInflater, R.layout.item_category_parent, parent,false);
            return ParentListViewHolder(binding);
        }

        override fun getItemCount(): Int {
            return mDataList.size;
        }

        override fun onBindViewHolder( holder: ParentListViewHolder, position: Int ) {
            val data = mDataList[position];
            holder.binding.setVariable(BR.category, data);
            holder.binding.setVariable(BR.isSelected, position == mViewModel.currentSelected);
            holder.binding.setVariable(BR.isAddItem, position == mDataList.size-1);
            holder.binding.executePendingBindings();
            holder.itemView.setOnClickListener{v: View? ->
                run {
                    // 点击添加
                    if(position == mDataList.size-1) {
                        EditCategoryActivity.startAddParent(this@CategoryActivity, CategoryActivity.ADD_CATEGORY_CODE);
                    }else if(position != mViewModel.currentSelected) {
                        mViewModel.currentSelected = position
                        notifyDataSetChanged();
                        refreshChildList();
                    }
                }
            }
        }

        fun addAll(list: ArrayList<Category>) {
            mDataList.clear();
            mDataList.addAll(list);
            notifyDataSetChanged();
        }
    }
}


