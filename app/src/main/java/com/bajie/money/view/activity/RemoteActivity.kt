package com.bajie.money.view.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import com.bajie.money.R
import com.bajie.money.databinding.ActivityRemoteBinding
import com.bajie.money.viewmodel.RemoteViewModel
import com.uber.autodispose.AutoDispose
import com.uber.autodispose.SingleSubscribeProxy
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import io.reactivex.Single

/**

 * bajie on 2021/1/8 16:45

 */
class RemoteActivity: AppCompatActivity() {
    private lateinit var model: RemoteViewModel;
    private lateinit var mBinding: ActivityRemoteBinding;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_remote);

        val factory = RemoteViewModel.RemoteViewModelFactory(application);
        model = ViewModelProvider(this, factory).get(RemoteViewModel::class.java);
        mBinding.vm = model;
        mBinding.btn.setOnClickListener{
            model.loadRemote()
                .bindLifeCycle(this)
                .subscribe { _, error ->
                    error?.let {
                        Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show();
                    }
                }
        }

//

//        mBinding.mask.setOnTouchListener(View.OnTouchListener { _, _ -> true });
    }

    fun  <T> Single<T>.bindLifeCycle(owner: LifecycleOwner): SingleSubscribeProxy<T> {
        return this.`as`(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(owner, Lifecycle.Event.ON_DESTROY)))
    }
}