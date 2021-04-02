package com.bajie.money.view.activity


import android.app.Activity
import android.content.Intent
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.NumberPicker
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bajie.money.R
import com.bajie.money.databinding.ActivityTimePickerBinding
import com.bajie.money.utils.Canstant
import com.bajie.money.utils.TimeUtils
import com.bajie.money.viewmodel.TimePickerViewModel

/**

 * bajie on 2021/3/25 14:38

 */
class TimePickerActivity: BaseActivity<ActivityTimePickerBinding, TimePickerViewModel>(), View.OnClickListener {
    companion object {
        fun startForResult(fragment: Fragment, requestCode: Int, data: String) {
            val intent = Intent(fragment.context, TimePickerActivity::class.java);
            intent.putExtra(Canstant.INTENT_DATA, data);
            fragment.startActivityForResult(intent, requestCode);
        }
    }

    override fun init() {
        mBinding.timePicker.setIs24HourView(true);
        setPickerSize();
        initPicker();
        mBinding.header.rightBtn.setOnClickListener(this);
        mBinding.header.back.setOnClickListener(this);
    }

    private fun initPicker() {
        var data = intent.getStringExtra(Canstant.INTENT_DATA);
        if(!data.equals("")) {
            val five = TimeUtils.getFiveParams(data);
            mBinding.datePicker.init(five.a, five.b, five.c, null);
            mBinding.timePicker.currentHour = five.d;
            mBinding.timePicker.currentMinute = five.e;
        }
    }


    private fun setPickerSize() {
        val dpContainer = mBinding.datePicker.getChildAt(0) as LinearLayout;
        val dpSpinner = dpContainer.getChildAt(0) as LinearLayout;
        for (i in 0..dpContainer.childCount) {
            val numPicker = dpSpinner.getChildAt(i) as NumberPicker;
            val params = LinearLayout.LayoutParams(200, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.leftMargin = 0;
            params.rightMargin = 30;
            numPicker.layoutParams = params;
        }

        val tpContainer = mBinding.timePicker.getChildAt(0) as LinearLayout;
        val tpSpinner = tpContainer.getChildAt(1) as LinearLayout;
        for(i in 0 until tpSpinner.childCount) {
            // TextView
            if(i == 1) {
                val textView = tpSpinner.getChildAt(i) as TextView;
                val param = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                param.leftMargin = 0;
                param.rightMargin = 30;
                textView.layoutParams = param;
                textView.gravity = Gravity.CENTER ;
                continue;
            }
            val numPicker = tpSpinner.getChildAt(i) as NumberPicker;
            val param = LinearLayout.LayoutParams(200, LinearLayout.LayoutParams.WRAP_CONTENT);
            param.leftMargin = 0;
            param.rightMargin = 30;
            numPicker.layoutParams = param;
        }
    }


    override fun getViewModel(): TimePickerViewModel {
        return ViewModelProvider(this).get(TimePickerViewModel::class.java);
    }

    override fun getLayout(): Int {
        return R.layout.activity_time_picker;
    }


    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.back -> finish();
            R.id.right_btn -> {
                val intent = Intent();
                var time = mBinding.datePicker.year.toString()  + "/" + (mBinding.datePicker.month + 1) + "/" + mBinding.datePicker.dayOfMonth + " " +mBinding.timePicker.currentHour + ":" + mBinding.timePicker.currentMinute;
                intent.putExtra("data", time)
                setResult(Activity.RESULT_OK, intent);
                println("bajietest" + mBinding.datePicker.month);
                finish()
            }
        }
    }
}