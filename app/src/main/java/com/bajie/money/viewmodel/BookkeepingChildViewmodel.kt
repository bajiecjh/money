package com.bajie.money.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bajie.money.model.dao.CategoryDao
import com.bajie.money.model.dao.RecordDao
import com.bajie.money.model.data.Category
import com.bajie.money.model.data.Record
import com.bajie.money.utils.Canstant
import com.bajie.money.utils.TimeUtils
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*
import kotlin.collections.ArrayList

/**

 * bajie on 2021/1/4 16:04

 */
class BookkeepingChildViewmodel constructor(val local: CategoryDao, val recordDao: RecordDao) : ViewModel() {
    var emptyCategoryHint = ""
    var clickToAddHint= ""
    val category = MutableLiveData<Category>(null);
    private var isFirstGetData = true
    val commonlyList: MutableLiveData<ArrayList<Category>> by lazy {
        MutableLiveData<ArrayList<Category>>();
    };
    var recordTime = MutableLiveData<String>("此刻");
    var type = 0;


    fun setRecordTime(time: String) {
        recordTime.value = time;
    }

    fun setCategoryAsCommonly() {
        category.value?.let {
            it.commonly = 1;
            category.value = it;
            local.add(it!!)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    this.commonlyList.value!!.add(category.value!!);
                    this.commonlyList.value = this.commonlyList.value;
                }
        }
    }

    fun init(type: Int) {
        this.type = type;
        emptyCategoryHint = if(isOutType()) "您还未添加支出小类" else "您还未添加收入小类";
        clickToAddHint = if(isOutType()) "点击添加支出类别" else "点击添加收入类别";
        getCommonlyList();
    }


    fun setNewCategory(newCategory: Category?) {
        category.value = newCategory;
    }
    fun getCommonlyItem(position: Int): Category? {
        commonlyList.value?.let {
            return it[position];
        }
        return null;
    }
    fun getDefaultCategory() {
        if (isOutType()) getOutDefaultCategory() else getInDefaultCategory();
    }

    private fun getInDefaultCategory() {
        if(commonlyList.value!!.size > 0) {
            category.value = commonlyList.value!![0]
        } else {
            getFirstInChild().subscribe { t1, _ ->
                category.value = t1;
            }
        }
    }

    private fun getOutDefaultCategory() {
        getDefaultFood().subscribe { t1: Category?, _ ->
            when {
                t1 != null -> {
                    category.value = t1;
                }
                commonlyList.value!!.size>0 -> {    // 获取常用类型第一个
                    category.value = commonlyList.value!![0];
                }
                else -> { // 获取小类第一个
                    getFirstOutChild().subscribe { t1, _ ->
                        category.value = t1;
                    }
                }
            }
        }
    }

    fun getCommonlyList() {
        if(isOutType()) getOutCommonlyList() else getInCommonlyList();
    }

    private fun getOutCommonlyList() {
        local.getOutCommonlyList().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { list, _ ->
                getCommonlySuccess(list)
            }

    }

    private fun getInCommonlyList() {
        local.getInCommonlyList().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { list, _ ->
                getCommonlySuccess(list)
            }
    }

    private fun getCommonlySuccess(list: List<Category>?) {
        list?.let { commonlyList.value= it as ArrayList<Category>; }
        if (isFirstGetData) {
            getDefaultCategory()
            isFirstGetData = false;
        }
    }


    fun addRecord(price:Float, hint:String): Completable {
        return Completable.create{  emitter ->
            local.getCategoryById(category.value!!.parentId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe { t1, t2 ->
                    t1?.let {
                        val time: Long = if(recordTime.value.equals("此刻")) TimeUtils.getTimeString() else TimeUtils.dateToStamp(recordTime.value!!);
                        val record = Record(price, category.value!!.id, hint, time, category.value!!.name, t1.name, type);
                        recordDao.add(record)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe {
                                emitter.onComplete();
                            }
                    }
                    t2?.let {
                        emitter.onError(t2);
                    }
                }
        }
    }



    fun getCommonlyListSize(): Int {
        this.commonlyList.value?.let {
            return it.size;
        };
        return 0;
    }

    private fun getFirstOutChild(): Single<Category> {
        return local.getFirstOutChild()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
    private fun getFirstInChild(): Single<Category> {
        return local.getFirstInChild()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    private fun getDefaultFood(): Single<Category> {
        val calender = Calendar.getInstance();
        val hour = calender.get(Calendar.HOUR_OF_DAY);
        var default = ""
        if(hour == 0 || hour == 1 || hour == 2 || hour == 3 || hour == 4 || hour == 17 || hour == 18 || hour == 19 || hour == 20 || hour == 21 ) {
            default = "晚餐"
        } else if(hour == 12 || hour == 13 || hour == 14 || hour == 15 || hour == 16 ) {
            default = "午餐"
        } else if(hour == 5 || hour == 6 || hour == 7 || hour == 8 || hour == 9 || hour == 10 || hour == 11 )  {
            default = "早餐"
        } else {
            default = "宵夜"
        }

        return local.getCategoryByName(default)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    public fun isSame(position: Int): Boolean {
        return category.value?.id == commonlyList.value!![position].id;
    }

    public fun isOutType(): Boolean {
        return type == Canstant.OUT_TYPE
    }
}