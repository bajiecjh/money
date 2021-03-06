package com.bajie.money.view.activity


import android.view.MotionEvent
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bajie.money.R
import com.bajie.money.databinding.ActivityHomeBinding
import com.bajie.money.view.fragment.BookkeepingFragment
import com.bajie.money.view.fragment.HomeFragment
import com.bajie.money.view.fragment.RecordFragment
import com.bajie.money.viewmodel.HomeViewmodel

/**

 * bajie on 2021/1/19 17:20

 */
class HomeActivity: BaseActivity<ActivityHomeBinding, HomeViewmodel>(), View.OnClickListener {

    private val recordFragment: RecordFragment by lazy {
        RecordFragment()
    };
    private val bookkeepingFragment: BookkeepingFragment by lazy {
        BookkeepingFragment();
    };
    private lateinit var fragment3: HomeFragment;
    private lateinit var fragment4: HomeFragment;
    private val onTouchListeners: ArrayList<MyOnTouchListener> by lazy {
        ArrayList<MyOnTouchListener>(10);
    }


    override fun getLayout(): Int {
        return R.layout.activity_home;
    }

    override fun init() {
        showFragment1();
        mBinding.tab1.setOnClickListener(this);
        mBinding.tab2.setOnClickListener(this);
        mBinding.tab3.setOnClickListener(this);
        mBinding.tab4.setOnClickListener(this);
    }

    private fun showFragment1() {
        if(mViewModel.changeTabSelected(0)) {
            showFragment(recordFragment);
        }
    }
    public fun showBookkeepingFragment() {
        if(mViewModel.changeTabSelected(1)) {
            showFragment(bookkeepingFragment);
        }

    }
    private fun showFragment3() {
        if(!::fragment3.isInitialized) {
            fragment3 = HomeFragment("3");
        } else if(fragment3 == null) {
            fragment3 = supportFragmentManager.findFragmentByTag(fragment3.javaClass.name) as HomeFragment;
            if(fragment3 == null) {
                fragment3 = HomeFragment("3");
            }
        }
        showFragment(fragment3);
    }
    private fun showFragment4() {
        if(!::fragment4.isInitialized) {
            fragment4 = HomeFragment("4");
        } else if(fragment4 == null) {
            fragment4 = supportFragmentManager.findFragmentByTag(fragment4.javaClass.name) as HomeFragment;
            if(fragment4 == null) {
                fragment4 = HomeFragment("4");
            }
        }
        showFragment(fragment4);
    }

    private fun showFragment(toFragment: Fragment) {
        if(toFragment == null) return;
        val tag = toFragment.javaClass.name;
        val transaction = supportFragmentManager.beginTransaction();
        if(!toFragment.isAdded) {
            transaction.add(mBinding.content.id, toFragment, tag);
        }
        val fragments = supportFragmentManager.fragments;
        if(fragments != null && fragments.size > 0) {
            fragments.forEach { fragment: Fragment? ->
                fragment?.run {
                    if (fragment == toFragment) {
                        transaction.show(fragment);
                    } else {
                        transaction.hide(fragment);
                    }
                }
            }
        }
        transaction.commitAllowingStateLoss();
    }


    override fun onClick(v: View?) {
        val id = v?.id;
        when(id) {
            R.id.tab1 -> {
                showFragment1();
            }
            R.id.tab2 -> {
                showBookkeepingFragment();
            }
            R.id.tab3 -> {
                if(mViewModel.changeTabSelected(2)) {
                    showFragment3();
                }
            }
            R.id.tab4 -> {
                if(mViewModel.changeTabSelected(3)) {
                    showFragment4();
                }
            }
        }
    }

    override fun getViewModel(): HomeViewmodel {
        return ViewModelProvider(this).get(HomeViewmodel::class.java);
    }


    public interface MyOnTouchListener {
        fun onTouch(event: MotionEvent): Boolean;
    }

}