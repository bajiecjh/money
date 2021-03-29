package com.bajie.money.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import com.bajie.money.BR

/**

 * bajie on 2021/1/29 11:57

 */
abstract class BaseFragment<T : ViewDataBinding, V: ViewModel>: Fragment {

    lateinit var mBinding: T;
    lateinit var mViewModel: V;
    constructor() {

    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        mBinding = DataBindingUtil.inflate<T>(inflater, getLayout(), container, false);
        mViewModel = getViewModel();
        mBinding.lifecycleOwner = this;
        mBinding.setVariable(BR.vm, mViewModel);
        return mBinding.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init();
    }
    public abstract fun getLayout(): Int;
    public abstract fun init();
    public abstract fun getViewModel(): V;
    fun showToast(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}