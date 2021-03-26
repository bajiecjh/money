package com.bajie.money.view.activity

import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import com.bajie.money.BR
import com.bajie.money.R

/**

 * bajie on 2021/1/27 14:33

 */
open abstract class BaseActivity<T : ViewDataBinding, V: ViewModel>: AppCompatActivity() {

    val mBinding: T by lazy {
        DataBindingUtil.setContentView<T>(this, getLayout())
    };
    lateinit var mViewModel: V;
    override fun onCreate(savedInstanceState: Bundle?) {
        setScreenPortrait();
//        window.setFlags(Window.FEATURE_NO_TITLE, Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        mViewModel = getViewModel();
        mBinding.lifecycleOwner = this
        mBinding.setVariable(BR.vm, mViewModel)
        init();
        setStatusBar();
    }

    private fun setStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //获取窗口区域
           window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.statusBarColor = resources.getColor(R.color.white);
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;

        }
    }
    public abstract fun getLayout(): Int;
    public abstract fun init();
    public abstract fun getViewModel(): V;

    private fun createBinding() {
        val inflater = this.getLayout();
//        val viewBindClass = findViewBind
    }


    private fun setScreenPortrait() {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
    }
    private fun setScreenLandscape() {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
    }

    fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

}