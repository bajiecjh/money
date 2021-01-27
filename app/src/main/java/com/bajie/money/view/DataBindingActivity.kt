package com.bajie.money.view

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.PersistableBundle
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bajie.money.BR
import com.bajie.money.R
import com.bajie.money.databinding.ActivityDataBindingBinding
import com.bajie.money.model.data.DataBindingData
import com.bajie.money.model.data.TestData
import com.bumptech.glide.Glide

/**

 * bajie on 2021/1/19 17:20

 */
class DataBindingActivity: AppCompatActivity() {

    lateinit var mBinding: ActivityDataBindingBinding;
    val data = DataBindingData("bajie", 3, true, "https://img.mukewang.com/user/545862440001cef302200220-40-40.jpg");
    private lateinit var mAdapter: ListAdapter;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_data_binding);
        mBinding.lifecycleOwner = this;
        mBinding.data = data;
        mBinding.presenter = Presenter();
        mBinding.viewstub.viewStub!!.inflate();

        mBinding.recyclerView.layoutManager = LinearLayoutManager(this);
        mAdapter = ListAdapter(this);
        mBinding.recyclerView.adapter = mAdapter;
        val data = ArrayList<DataBindingData>();
        data.add(DataBindingData("bajie1", 1, false));
        data.add(DataBindingData("bajie1", 2, false));
        data.add(DataBindingData("bajie1", 3, true));
        data.add(DataBindingData("bajie1", 4, false));
        mAdapter.addAll(data);

        // 动画
        mBinding.addOnRebindCallback(object : OnRebindCallback<ActivityDataBindingBinding>() {
            override fun onPreBind(binding: ActivityDataBindingBinding?): Boolean {
                val view = binding?.root as ViewGroup;
                TransitionManager.beginDelayedTransition(view);
                return true;
            }
        })

    }

    public interface OnItemClickListener {
        fun onItemClick(data: DataBindingData);
    }

    inner class ListAdapter(val context: Context): RecyclerView.Adapter<MViewHolder<ViewDataBinding>>() {

        private val ITEM_VIEW_TYPE_ON = 1;
        private val ITEM_VIEW_TYPE_OFF = 2;

        private var mLayoutInflatere: LayoutInflater;
        public var listener: OnItemClickListener? = null;
        private var mDataList: java.util.ArrayList<DataBindingData>;


        init {
            mLayoutInflatere = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater;
            mDataList = ArrayList<DataBindingData>();
        }

        override fun onCreateViewHolder(parent: ViewGroup,viewType: Int): MViewHolder<ViewDataBinding> {
            val layout = if(viewType == ITEM_VIEW_TYPE_ON) R.layout.item_on else R.layout.item_off;
            val binding: ViewDataBinding = DataBindingUtil.inflate(mLayoutInflatere, layout, parent, false);
            return MViewHolder(binding);
        }

        override fun getItemViewType(position: Int): Int {
            return if (mDataList[position].isFired.get()!!) ITEM_VIEW_TYPE_OFF else ITEM_VIEW_TYPE_ON;
        }

        override fun getItemCount(): Int {
            return mDataList.size;
        }

        override fun onBindViewHolder(holder: MViewHolder<ViewDataBinding>, position: Int) {
            val data = mDataList[position];
//            holder.getBinding().setVariable(BR.item)
            holder.binding.setVariable(BR.data, data);
            holder.binding.executePendingBindings();
            holder.itemView.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View?) {
                    listener?.onItemClick(data)
                }

            })
        }

        public fun addAll(list: List<DataBindingData>) {
            mDataList.addAll(list);
        }
        public fun add(data: DataBindingData) {
            mDataList.add(data);
            notifyItemInserted(mDataList.size);
        }
        public fun remove() {
            if(mDataList.size > 0) {
                mDataList.removeAt(0);
                notifyItemRemoved(0);
            }
        }
    }

    inner class MViewHolder<T: ViewDataBinding> (val binding: T): RecyclerView.ViewHolder(binding.root)  {
    }

    public inner class Presenter {
        public fun onClickBinding() {
            data.count.set(data.count.get()?.plus(1))
            mAdapter.add(DataBindingData("bajie", 5, false));
//            mBinding.data = data;
        }

        public fun onClick(v: View) {
            data.name = "bajie1";
            mAdapter.remove();
//            mBinding.data = data;
        }
    }
}

@androidx.databinding.BindingAdapter(value=["app:imageUrl", "app:placeholder"], requireAll = false )
public fun loadImageFromUrl(view: ImageView, url: String?, drawable: Drawable?) {
    Glide.with(view.context).load(url).placeholder(drawable).into(view);
}

@BindingConversion
public fun convertColorToDrawable(color: Int): ColorDrawable  {
    return ColorDrawable(color);
}

@InverseBindingAdapter(attribute = "android:text")
public fun getTextString(view: TextView): String {
    return view.text.toString();
}