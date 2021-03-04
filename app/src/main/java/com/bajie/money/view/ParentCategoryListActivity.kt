package com.bajie.money.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bajie.money.BR
import com.bajie.money.R
import com.bajie.money.databinding.ActivityParentCategoryListBinding
import com.bajie.money.databinding.ItemParentCategoryBinding
import com.bajie.money.model.data.Category
import com.bajie.money.viewmodel.ParentCategoryListVMFactory
import com.bajie.money.viewmodel.ParentCategoryListViewmodel

/**

 * bajie on 2021/2/20 18:03

 */
class ParentCategoryListActivity: BaseActivity<ActivityParentCategoryListBinding>(),
    View.OnClickListener {
    companion object {
        const val ADD_CATEGORY_CODE = 100;
        const val EDIT_CATEGORY_CODE = 101;
        fun start(context: Activity) {
            val intent = Intent(context, ParentCategoryListActivity::class.java);
            context.startActivity(intent);
        }
    }
    private lateinit var mViewModel: ParentCategoryListViewmodel;
    private lateinit var mAdapter: ListAdapter;

    override fun getLayout(): Int {
        return R.layout.activity_parent_category_list;
    }

    override fun init() {
        mViewModel = ViewModelProvider(this, ParentCategoryListVMFactory(application)).get(ParentCategoryListViewmodel::class.java);
        mBinding.vm = mViewModel;
        mBinding.list.layoutManager = LinearLayoutManager(this);
        mAdapter = ListAdapter(this);
        mBinding.list.adapter = mAdapter;
        this.refreshiList();

        mBinding.header.back.setOnClickListener(this);
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK) {
            when(requestCode) {
                ADD_CATEGORY_CODE, EDIT_CATEGORY_CODE -> refreshiList();
            }
        }
    }

    private fun refreshiList() {
        mViewModel.getList()?.subscribe { list: List<Category>?, err: Throwable? ->
            list?.run {
                mAdapter.addAll(list);
            }
            err?.run {
                showToast("加载列表失败");
            }
        }
    }


    class ListViewHolder(val binding: ItemParentCategoryBinding): RecyclerView.ViewHolder(binding.root) {
    }
    class ListAdapter(val context: Context): RecyclerView.Adapter<ListViewHolder>() {
        private val mDataList = ArrayList<Category>();
        private val mLayoutInflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater;
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
            val binding : ItemParentCategoryBinding = DataBindingUtil.inflate(mLayoutInflater, R.layout.item_parent_category, parent, false);
            return ListViewHolder(binding);
        }

        override fun getItemCount(): Int {
            return mDataList.size;
        }

        override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
            val data = mDataList[position];
            holder.binding.setVariable(BR.category, data);
            holder.binding.setVariable(BR.isFirstItem, position == 0);
            holder.itemView.setOnClickListener{
                if(position == itemCount - 1) {
                    EditCategoryActivity.startAddParent(context as Activity, ADD_CATEGORY_CODE);
                } else {
                    EditCategoryActivity.startEditParent(context as Activity, EDIT_CATEGORY_CODE, data.id);
                }
            }
        }

        fun addAll(list: List<Category>) {
            mDataList.clear();
            mDataList.addAll(list);
            notifyDataSetChanged();
        }

    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.back -> finish();
        }
    }
}

@BindingAdapter("android:layout_marginRight")
public fun setRightMargin(view: View, rightMar: Float) {
    val layoutParams: ViewGroup.MarginLayoutParams =
        view.layoutParams as ViewGroup.MarginLayoutParams;
    layoutParams.rightMargin = rightMar.toInt();
    view.layoutParams = layoutParams;
}
@BindingAdapter("android:layout_marginLeft")
public fun setLeftMargin(view: View, leftMar: Float) {
    val layoutParams: ViewGroup.MarginLayoutParams =
        view.layoutParams as ViewGroup.MarginLayoutParams;
    layoutParams.leftMargin = leftMar.toInt();
    view.layoutParams = layoutParams;
}
