package com.bajie.money.view

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bajie.money.R
import com.bajie.money.databinding.ActivityRemoteBinding
import com.bajie.money.viewmodel.RemoteViewModel

/**

 * bajie on 2021/1/8 16:45

 */
class RemoteActivity: AppCompatActivity() {
    private lateinit var model: RemoteViewModel;
    private lateinit var mBinding: ActivityRemoteBinding;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_remote);
        model = ViewModelProvider(this).get(RemoteViewModel::class.java)
        mBinding.vm = model;
        mBinding.btn.setOnClickListener{
            model.loadRemote().subscribe { _, error ->
                error?.let {
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show();
                }
            }
        }

//

//        mBinding.mask.setOnTouchListener(View.OnTouchListener { _, _ -> true });
    }
}