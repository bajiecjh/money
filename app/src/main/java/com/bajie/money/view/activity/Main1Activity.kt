package com.bajie.money.view.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bajie.money.R
import com.bajie.money.databinding.ActivityMainBinding
import com.bajie.money.viewmodel.MainViewmodel

/**

 * bajie on 2021/1/8 12:14

 */
class Main1Activity: AppCompatActivity()  {

    private lateinit var model: MainViewmodel;
    private lateinit var mBinding: ActivityMainBinding;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        model = ViewModelProvider(this).get(MainViewmodel::class.java)
        mBinding.vm = model;
    }
}