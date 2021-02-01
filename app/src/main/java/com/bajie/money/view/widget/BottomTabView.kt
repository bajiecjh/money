package com.bajie.money.view.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import com.bajie.money.R
import com.bajie.money.databinding.ViewBottomTabBinding
import com.bajie.money.model.data.BottomTabData

/**

 * bajie on 2021/1/28 11:28

 */
class BottomTabView : FrameLayout {

    var mBinding: ViewBottomTabBinding = DataBindingUtil.inflate(context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater, R.layout.view_bottom_tab, this, true);

    constructor(context: Context): super(context) {}
    constructor(context: Context, attributeSet: AttributeSet): super(context, attributeSet) {}
    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int): super(context, attributeSet, defStyleAttr){}

    public fun setData(data: BottomTabData) {
        mBinding.data = data;
    }
}