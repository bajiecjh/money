package com.bajie.money.view.fragment

import androidx.lifecycle.ViewModelProvider
import com.bajie.money.R
import com.bajie.money.databinding.FragmentHomeBinding
import com.bajie.money.viewmodel.HomeViewmodel

/**

 * bajie on 2021/1/29 11:58

 */
class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewmodel> {
    private var text = ""
    constructor() {

    }
    constructor(text: String) {
        this.text = text;
    }
    override fun getLayout(): Int {
        return R.layout.fragment_home;
    }

    override fun init() {
        mBinding.text = text;
    }

    override fun getViewModel(): HomeViewmodel {
        return ViewModelProvider(this).get(HomeViewmodel::class.java);
    }


}