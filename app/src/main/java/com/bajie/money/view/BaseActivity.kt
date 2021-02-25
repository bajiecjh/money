package com.bajie.money.view

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

/**

 * bajie on 2021/1/27 14:33

 */
open abstract class BaseActivity<T : ViewDataBinding>: AppCompatActivity() {

    val mBinding: T by lazy {
        DataBindingUtil.setContentView<T>(this, getLayout())
    };

    override fun onCreate(savedInstanceState: Bundle?) {
        setScreenPortrait();
//        window.setFlags(Window.FEATURE_NO_TITLE, Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        mBinding.lifecycleOwner = this
        init();
    }

    public abstract fun getLayout(): Int;
    public abstract fun init();

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