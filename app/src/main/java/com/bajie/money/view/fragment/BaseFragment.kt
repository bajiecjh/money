package com.bajie.money.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment

/**

 * bajie on 2021/1/29 11:57

 */
abstract class BaseFragment<T : ViewDataBinding>: Fragment {

    lateinit var mBinding: T;

    constructor() {

    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        mBinding = DataBindingUtil.inflate<T>(inflater, getLayout(), container, false);
        return mBinding.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init();
    }
    public abstract fun getLayout(): Int;
    public abstract fun init();
}