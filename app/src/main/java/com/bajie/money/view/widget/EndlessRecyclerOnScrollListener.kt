package com.bajie.money.view.widget

import android.graphics.Rect
import android.os.Handler
import android.widget.RelativeLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**

 * bajie on 2021/4/8 14:46

 */
abstract class EndlessRecyclerOnScrollListener: RecyclerView.OnScrollListener() {


    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
        val manager: LinearLayoutManager = recyclerView.layoutManager as LinearLayoutManager;
        // 不滑动时
        if(newState == RecyclerView.SCROLL_STATE_IDLE) {
            // 获取最后一个完全显示的itemPosition
            val lastItemPosition = manager.findLastCompletelyVisibleItemPosition();
            val itemCount = manager.itemCount;

            // 判断是否滑动到最后一个item
            if(lastItemPosition >= (itemCount - 2)) {
                // footer的高度
                val footerView = manager.findViewByPosition(itemCount - 1)
                val height = (footerView!!.layoutParams as RecyclerView.LayoutParams).height;
                val rect = Rect();
                footerView.getLocalVisibleRect(rect);
                if(rect.bottom == height) {

                    scrollToEnd();
                    println("bajie::scroll::$newState");
                    Handler().postDelayed({
                        recyclerView.scrollBy(0, -rect.bottom);
                    }, 1000)
                } else {
                    recyclerView.scrollBy(0, -rect.bottom);
                }
            }


//            manager.findViewByPosition(itemCount - 2)!!.getLocalVisibleRect(rect);
//            println("bajie::scroll::" + rect.top + "::" + rect.bottom);
//            val rect1 = Rect();
//            manager.findViewByPosition(itemCount - 1)!!.getLocalVisibleRect(rect1);
//            println("bajie::scroll::" + rect1.top + "::" + rect1.bottom);
        }

    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
    }

    public abstract fun scrollToEnd();

}