package com.bajie.money.model.data

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.databinding.library.baseAdapters.BR

/**

 * bajie on 2021/1/7 14:35

 */

// BaseObservable
class DataBindingData : BaseObservable {

    @get:Bindable
    var name: String = ""
        set(value) {
            field = value;
            notifyPropertyChanged(BR.name)
        }

    val isFired = ObservableField<Boolean>(false);
    val count = ObservableField<Int>();
    val imgUrl = ObservableField<String>();

    constructor(name: String,count: Int, isFired: Boolean?): this(name, count, isFired, ""){
    }

    constructor(name: String,count: Int, isFired: Boolean?, imgUrl: String) {
        this.name = name;
        this.count.set(count);
        this.isFired.set(isFired);
        this.imgUrl.set(imgUrl);
    }
}