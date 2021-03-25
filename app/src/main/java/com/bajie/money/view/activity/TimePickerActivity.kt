package com.bajie.money.view.activity


import android.content.Intent
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.NumberPicker
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import com.bajie.money.R
import com.bajie.money.databinding.ActivityTimePickerBinding

/**

 * bajie on 2021/3/25 14:38

 */
class TimePickerActivity: BaseActivity<ActivityTimePickerBinding>() {
    companion object {
        fun start(activity: FragmentActivity) {
            val intent = Intent(activity, TimePickerActivity::class.java);
            activity.startActivity(intent);
        }
    }
    override fun getLayout(): Int {
        return R.layout.activity_time_picker;
    }

    override fun init() {
        mBinding.timePicker.setIs24HourView(true);

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
}