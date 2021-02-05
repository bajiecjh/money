package com.bajie.money.view


import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bajie.money.R
import com.bajie.money.databinding.ActivityHomeBinding
import com.bajie.money.view.fragment.BaseFragment
import com.bajie.money.view.fragment.BookkeepingFragment
import com.bajie.money.view.fragment.HomeFragment
import com.bajie.money.viewmodel.HomeViewmodel

/**

 * bajie on 2021/1/19 17:20

 */
class HomeActivity: BaseActivity<ActivityHomeBinding>(), View.OnClickListener {

    private lateinit var fragment1: HomeFragment;
    private lateinit var fragment2: BookkeepingFragment;
    private lateinit var fragment3: HomeFragment;
    private lateinit var fragment4: HomeFragment;

    private lateinit var mViewModel: HomeViewmodel;

    override fun getLayout(): Int {
        return R.layout.activity_home;
    }

    override fun init() {
        mViewModel = ViewModelProvider(this).get(HomeViewmodel::class.java);
        mBinding.vm = mViewModel;
        showFragment1();
        mBinding.tab1.setOnClickListener(this);
        mBinding.tab2.setOnClickListener(this);
        mBinding.tab3.setOnClickListener(this);
        mBinding.tab4.setOnClickListener(this);
    }

    private fun showFragment1() {
        if(!::fragment1.isInitialized) {
            fragment1 = HomeFragment("1");
        } else if(fragment1 == null) {
            fragment1 = supportFragmentManager.findFragmentByTag(fragment1.javaClass.name) as HomeFragment;
            if(fragment1 == null) {
                fragment1 = HomeFragment("1");
            }
        }
        showFragment(fragment1);
    }
    private fun showFragment2() {
        if(!::fragment2.isInitialized) {
            fragment2 = BookkeepingFragment();
        } else if(fragment2 == null) {
            fragment2 = supportFragmentManager.findFragmentByTag(fragment2.javaClass.name) as BookkeepingFragment;
            if(fragment2 == null) {
                fragment2 = BookkeepingFragment();
            }
        }
        showFragment(fragment2);
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
                if(mViewModel.changeTabSelected(0)) {
                    showFragment1();
                }
            }
            R.id.tab2 -> {
                if(mViewModel.changeTabSelected(1)) {
                    showFragment2();
                }
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


}