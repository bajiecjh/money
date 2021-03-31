package com.bajie.money.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bajie.money.R
import com.bajie.money.model.dao.CategoryDao
import com.bajie.money.model.data.Category
import com.bajie.money.model.loacal.AppDatabase
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers

/**

 * bajie on 2021/1/4 16:04

 */
class CategoryViewmodel constructor(val local: CategoryDao) : ViewModel() {
    // top数据
    var title = "";
    val rightIcon = R.drawable.icon_setting;

    val isEmptyCommonly = MutableLiveData<Boolean>(false);

     val parentList = ArrayList<Category>();
     val childList = ArrayList<Category>();
    var currentSelected = 0;

    var type = 0

    fun init(type:Int) {
        this.type = type
        this.title = if(isOutType()) "支出类别" else "收入类别"
    }



    fun getParentList():Single<ArrayList<Category>> {
        return if(isOutType()) getOutParentList() else getInParentList();
    }

    private fun getInParentList(): Single<ArrayList<Category>> {
        return Single.create { emitter ->
            parentList.clear();
            val commonly = Category();
            commonly.name = "常用";
            parentList?.add(0, commonly);
            val all = Category();
            all.name = "全部";
            parentList.add(all);
            emitter.onSuccess(parentList);
        }
    }
    private fun getOutParentList(): Single<ArrayList<Category>> =
        local.getParentList()
            .map { t ->
                parentList.clear();
                parentList.addAll(t);
                val commonly = Category();
                commonly.name = "常用";
                parentList?.add(0, commonly);
                val add = Category();
                add.name = "添加";
                parentList.add(add);
                parentList;
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())


    private fun getCommonlyList(): Single<ArrayList<Category>> {
        if (isOutType()) {
            return local.getOutCommonlyList()
                .map { list ->
                    childList.clear();
                    childList.addAll(list);
                    isEmptyCommonly.postValue(list.isEmpty());
                    childList;

                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        } else {
            return local.getInCommonlyList()
                .map { list ->
                    childList.clear();
                    childList.addAll(list);
                    isEmptyCommonly.postValue(list.isEmpty());
                    childList;

                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        }
    }

    fun getChildList(): Single<ArrayList<Category>> {
        // 选中常用
        if(isCurrentCommonly()) {
            return getCommonlyList();
        }
        val parentId = if(isOutType()) parentList[currentSelected].id else Category.OUT_PARENT_ID;
        return local.getChildListByParentId(parentId)
            .map { list ->
                childList.clear();
                childList.addAll(list);
                val add = Category();
                add.name = "添加子类";
                childList.add(add);
                isEmptyCommonly.postValue(false);
                childList;
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread());
    }

    fun toggleCommonly(childIndex: Int): Completable {
        val oldCategory = childList[childIndex];
        val category = oldCategory.copy();
        category.commonly = if(isCommonly(childIndex)) 0 else 1;
        return Completable.create(object : CompletableOnSubscribe {
            override fun subscribe(emitter: CompletableEmitter) {
                local.update(category)
                    .subscribe(Action {
                        oldCategory.commonly = category.commonly;
                        if(isCurrentCommonly()) {
                            getCommonlyList().subscribe { list: ArrayList<Category>?, e: Throwable? ->
                                e?.run { emitter.onError(e) }
                                list?.run { emitter.onComplete() }
                             }
                        } else {
                            emitter.onComplete();
                        }
                    }, Consumer<Throwable> {
                        emitter.onError(it);
                    })
            }

        })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    }

    fun isCommonly(position: Int): Boolean {
        return childList[position].commonly == 1;
    }

    fun isCurrentCommonly(): Boolean {
        return currentSelected == 0;
    }

    // 判断是否为添加子类项
    fun isAddChildItem(postion: Int) :Boolean {
        val flag = currentSelected != 0 && postion == childList.size - 1;
        return flag;
    }


    fun getCurrentParentId(): Int {
        return parentList.get(currentSelected).id;
    }

    public fun isOutType():Boolean {
        return type == 0
    }
}