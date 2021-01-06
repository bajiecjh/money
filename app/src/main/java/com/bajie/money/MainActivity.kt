package com.bajie.money

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bajie.money.viewmodel.MainViewmodel
import com.bajie.network.TestUtils

class MainActivity : AppCompatActivity() {
    private lateinit var model: MainViewmodel;
    private var testnum = 0;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        model = ViewModelProvider(this).get(MainViewmodel::class.java);

        // 观察LiveData对象
        val testObserver = Observer<Int> { newNum ->
            setBtnText(newNum.toString());
        }
        model.testNum.observe(this, testObserver);

        findViewById<Button>(R.id.btn).setOnClickListener{
//            TestUtils.getInstance()!!.request();
//            TestUtils.getInstance()!!.request1();
//            TestUtils.getInstance()?.getMovies();
            addNumOne();
        };
        if(savedInstanceState == null) {
            addNumOne();
        }
    }

    private fun setBtnText(text: String) {
        findViewById<Button>(R.id.btn).text = text;
    }

    private fun addNumOne() {
        if(model.testNum.value == null) {
            model.testNum.value = 0;
        } else {
            model.testNum.value = model.testNum.value?.plus(1);
        }
    }
}
