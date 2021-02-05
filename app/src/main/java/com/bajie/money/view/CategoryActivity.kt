package com.bajie.money.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bajie.money.BR
import com.bajie.money.R
import com.bajie.money.databinding.ActivityCategoryBinding
import com.bajie.money.databinding.ActivityMainBinding
import com.bajie.money.databinding.ItemCategoryParentBinding
import com.bajie.money.model.data.Category
import com.bajie.money.viewmodel.CategoryViewModelFactory
import com.bajie.money.viewmodel.CategoryViewmodel
import com.bajie.money.viewmodel.MainViewmodel
import java.util.function.Consumer

/**

 * bajie on 2021/1/8 12:14

 */
class CategoryActivity: BaseActivity<ActivityCategoryBinding>()  {

    private lateinit var mViewModel: CategoryViewmodel;
    private lateinit var mParentAdapter: ParentListAdapter;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
    }

    override fun getLayout(): Int {
        return R.layout.activity_category;
    }

    override fun init() {
        mViewModel = ViewModelProvider(this, CategoryViewModelFactory(application)).get(CategoryViewmodel::class.java);
        mBinding.vm = mViewModel;
        mBinding.parentList.layoutManager = LinearLayoutManager(this);
        mBinding.childList.layoutManager = LinearLayoutManager(this);
        mParentAdapter = ParentListAdapter(this);
        mBinding.parentList.adapter = mParentAdapter;
        mViewModel.getList()
            ?.subscribe {t1: ArrayList<Category>?, _ ->
                t1?.run {
                    mParentAdapter.addAll(t1);
                }
            }
    }
}

class ParentListViewHolder(val binding: ItemCategoryParentBinding): RecyclerView.ViewHolder(binding.root) {
//    fun bind(category: Category) {
//        binding.name.text = category.name;
//    }
}

class ParentListAdapter(val context: Context): RecyclerView.Adapter<ParentListViewHolder>() {
    var currentSelected = 0;
    val preViewSelected = -1;
    private val mLayoutInflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater;
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
        holder.binding.setVariable(BR.isSelected, position == currentSelected);
        holder.binding.executePendingBindings();
        holder.itemView.setOnClickListener{v: View? ->
            run {
                if(position != currentSelected) {
                    currentSelected =position
                    notifyDataSetChanged();
                }
            }
        }
    }

    fun addAll(list: ArrayList<Category>) {
        mDataList.addAll(list);
    }
}
