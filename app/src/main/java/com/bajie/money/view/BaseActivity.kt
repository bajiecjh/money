package com.bajie.money.view

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.PersistableBundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.viewbinding.ViewBinding
import java.lang.reflect.ParameterizedType

/**

 * bajie on 2021/1/27 14:33

 */
open abstract class BaseActivity<T : ViewDataBinding>: AppCompatActivity() {

    val mBinding: T by lazy {
        DataBindingUtil.setContentView<T>(this, getLayout())
    };

    override fun onCreate(savedInstanceState: Bundle?) {
        setScreenPortrait();
        super.onCreate(savedInstanceState);
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

}