package com.bajie.money.view.varietyAdapter.proxy

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bajie.money.R
import com.bajie.money.databinding.FragmentRecordHomeHeaderBinding
import com.bajie.money.model.data.Record
import com.bajie.money.view.BaseViewHolder
import com.bajie.money.view.varietyAdapter.*

/**

 * bajie on 2021/4/29 11:17

 */
class RecordHeaderProxy: VarietyAdapter.Proxy<Record, TextViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.fragment_record_home_header;
    }

    override fun onBindViewHolder(holder: TextViewHolder, position: Int) {
        TODO("Not yet implemented")
    }


}

class TextViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

}