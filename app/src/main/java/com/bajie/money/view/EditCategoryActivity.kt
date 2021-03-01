package com.bajie.money.view

import android.app.Activity
import android.content.Intent
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.bajie.money.R
import com.bajie.money.databinding.ActivityAddCategoryBinding
import com.bajie.money.viewmodel.AddCategoryViewmodel
import com.bajie.money.viewmodel.ViewModelFactory

/**

 * bajie on 2021/2/5 18:01

 */
class EditCategoryActivity: BaseActivity<ActivityAddCategoryBinding>(), View.OnClickListener {
    companion object {
        const val TYPE_
        const val PARENT_ID = "parentId"


        fun start(context: Activity, parentId: Int, requestCode: Int) {
            val intent = Intent(context, EditCategoryActivity::class.java);
            intent.putExtra(PARENT_ID, parentId);
            context.startActivityForResult(intent, requestCode);
        }
    }
    private lateinit var mViewModel: AddCategoryViewmodel;
    override fun getLayout(): Int {
        return R.layout.activity_add_category;
    }
    override fun init() {
        mViewModel = ViewModelProvider(this, ViewModelFactory(application)).get(AddCategoryViewmodel::class.java);
        mBinding.vm = mViewModel;
        mViewModel.setTitle(intent.extras!!.getInt("parentId"));
        mBinding.title.back.setOnClickListener(this);
        mBinding.save.setOnClickListener(this);
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.back -> finish();
            R.id.save -> {
                if(mBinding.editText.text.isEmpty()) {
                    showToast("列别名称不能为空");
                    return;
                }
                mViewModel.addCategory(mBinding.editText.text.toString())
                    .subscribe {
                        showToast("添加成功");
                        val intent = Intent();
                        setResult(Activity.RESULT_OK, intent);
                        finish();
                    }
            }
        }
    }
}