package com.bajie.money.view.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bajie.money.BR
import com.bajie.money.R
import com.bajie.money.databinding.ActivityEditCategoryBinding
import com.bajie.money.databinding.ItemParentCategoryBinding
import com.bajie.money.view.widget.BaseDialog
import com.bajie.money.view.widget.DialogListener
import com.bajie.money.viewmodel.EditCategoryViewmodel
import com.bajie.money.viewmodel.ViewModelFactory
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer

/**

 * bajie on 2021/2/5 18:01

 */
class EditCategoryActivity: BaseActivity<ActivityEditCategoryBinding, EditCategoryViewmodel>(), View.OnClickListener,
    DialogListener {

    companion object {
        const val REQUEST_CODE_ADD_CHILD_CATEGORY = 100;
        const val REQUEST_CODE_EDIT_CHILD_CATEGORY = 101;
        const val PARAMS = "params";
        const val TYPE = "type";
        const val PARENT_ID = "parentId";
        const val ID = "id";

        fun startCreateChild(context: Activity, requestCode: Int, parentId: Int) {
            val intent = Intent(context, EditCategoryActivity::class.java);
            val params = HashMap<String, Int>();
            params[TYPE] = EditCategoryViewmodel.TYPE_CREATE_CHILD;
            params[PARENT_ID] = parentId;
            intent.putExtra(PARAMS, params);
            context.startActivityForResult(intent, requestCode);
        }

        fun startAddParent(context: Activity, requestCode: Int) {
            val intent = Intent(context, EditCategoryActivity::class.java);
            val params = HashMap<String, Int>();
            params[TYPE] = EditCategoryViewmodel.TYPE_CREATE_PARENT;
            params[PARENT_ID] = -1;
            intent.putExtra(PARAMS, params);
            context.startActivityForResult(intent, requestCode);
        }

        fun startEditParent(context: Activity, requestCode: Int, id: Int) {
            val intent = Intent(context, EditCategoryActivity::class.java);
            val params = HashMap<String, Int>();
            params[TYPE] = EditCategoryViewmodel.TYPE_EDIT_PARENT;
            params[ID] = id;
            intent.putExtra(PARAMS, params);
            context.startActivityForResult(intent, requestCode);
        }

        fun startEditChild(context: Activity, requestCode: Int, id: Int) {
            val intent = Intent(context, EditCategoryActivity::class.java);
            val params = HashMap<String, Int>();
            params[TYPE] = EditCategoryViewmodel.TYPE_EDIT_CHILD;
            params[ID] = id;
            intent.putExtra(PARAMS, params);
            context.startActivityForResult(intent, requestCode);
        }
    }
    private lateinit var mChildAdapter: ChildListAdapter;
    override fun getLayout(): Int {
        return R.layout.activity_edit_category;
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_ADD_CHILD_CATEGORY || requestCode == REQUEST_CODE_EDIT_CHILD_CATEGORY) {
            mViewModel.getChildList().subscribe { list, err ->
                list?.run { mChildAdapter.notifyDataSetChanged(); }
                err?.run { err.message?.let { showToast(it) } }
            }
        }
    }

    override fun init() {
        mViewModel.initData(intent.extras!!.getSerializable(PARAMS) as HashMap<String, Int>)
            .subscribe(
                Action {
                    if(mViewModel.isEditParent()) {
                        mBinding.editText.setSelection(mViewModel.getNameLength());
                        mBinding.childList.layoutManager = LinearLayoutManager(this);
                        mChildAdapter = ChildListAdapter();
                        mBinding.childList.adapter = mChildAdapter;
                    } else if(mViewModel.isEditChild()) {
                        mBinding.editText.setSelection(mViewModel.getNameLength());
                    }

                },
                Consumer<Throwable> { t -> t?.message?.let { showToast(it) }; }
            )


        mBinding.title.back.setOnClickListener(this);
        mBinding.save1.setOnClickListener(this);
        mBinding.save.setOnClickListener(this);
        mBinding.delete.setOnClickListener(this);
        mBinding.delete1.setOnClickListener(this);

    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.back -> finish();
            R.id.save1, R.id.save -> {
                if(mBinding.editText.text.isEmpty()) {
                    showToast("类别名称不能为空");
                    return;
                }
                mViewModel.addCategory(mBinding.editText.text.toString())
                    .subscribe {
                        val msg = if(mViewModel.isAdd()) "添加成功" else "修改成功";
                        showToast(msg);
                        val intent = Intent();
                        setResult(Activity.RESULT_OK, intent);
                        finish();
                    }
            };
            R.id.delete, R.id.delete1 -> {
                val msg = if(mViewModel.isEditParent()) "确定删除该大类？" else "确定删除该小类？";
                BaseDialog(this)
                    .setMessage(msg).show(supportFragmentManager, "delete");

            }
        }
    }

    override fun onDialogPositiveClick(dialog: DialogFragment) {
        mViewModel.deleteItem()?.subscribe ( Action {
            showToast("删除成功");
            val intent = Intent();
            setResult(Activity.RESULT_OK, intent);
            finish();
        }, Consumer<Throwable> {
            showToast(it.message!!);
        } );
    }

    override fun onDialogNegativeClick(dialog: DialogFragment) {}

    class ChildListViewHolder(val binding: ItemParentCategoryBinding): RecyclerView.ViewHolder(binding.root){}

    inner class ChildListAdapter(): RecyclerView.Adapter<ChildListViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChildListViewHolder {
            val binding: ItemParentCategoryBinding = DataBindingUtil.inflate(getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater, R.layout.item_parent_category,parent, false);
            return ChildListViewHolder(
                binding
            );
        }

        override fun getItemCount(): Int {
            return mViewModel.childList.size;
        }

        override fun onBindViewHolder(holder: ChildListViewHolder, position: Int) {
            val data = mViewModel.childList[position];
            holder.binding.setVariable(BR.category, data);
            holder.itemView.setOnClickListener {
                if(position == mViewModel.childList.size - 1) {
                    startCreateChild(
                        this@EditCategoryActivity,
                        REQUEST_CODE_ADD_CHILD_CATEGORY,
                        mViewModel.getCategoryId()
                    );
                } else {
                    startEditChild(
                        this@EditCategoryActivity,
                        REQUEST_CODE_EDIT_CHILD_CATEGORY,
                        mViewModel.getChildId(position)
                    );
                }
            }
        }

    }

    override fun getViewModel(): EditCategoryViewmodel {
        return ViewModelProvider(this, ViewModelFactory(application)).get(EditCategoryViewmodel::class.java);
    }
}

