package com.bajie.money.view

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bajie.money.R
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import io.reactivex.functions.Function
import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription

//import java.util.*

/**

 * bajie on 2021/1/12 22:54

 */
class RxJavaTestActivity: AppCompatActivity() {

    // 创建Observer
    val observer: Observer<String> = object : Observer<String> {
        override fun onComplete() {
            Log.d("bajie", "onComplete");
        }

        override fun onNext(t: String) {
            Log.d("bajie", "onNext:string=$t");
        }

        override fun onError(e: Throwable) {
            Log.d("bajie", "onError");
        }

        override fun onSubscribe(d: Disposable) {
        }

    }

    val subscriber = object : Subscriber<String> {
        override fun onComplete() {
            Log.d("bajie", "onComplete");
       }

        override fun onSubscribe(s: Subscription?) {
        }

        override fun onNext(t: String?) {
            Log.d("bajie", "onNext:string=$t");
        }

        override fun onError(t: Throwable?) {
            Log.d("bajie", "onError");
        }
    }




    val observable =  Observable.create(
        object : ObservableOnSubscribe<String> {
            override fun subscribe(emitter: ObservableEmitter<String> ) {
                emitter.onNext("hello");
                emitter.onNext("bajie");
                emitter.onNext("is");
                emitter.onNext("watching");
                emitter.onNext("you");
                emitter.onComplete();
            }
        }
    );

    val onCompletedAction = object :Action {
        override fun run() {
            Log.d("bajie", "onComplete");
        }

    }

    val onNextAction = object :Consumer<String> {
        override fun accept(t: String?) {
            Log.d("bajie", "onNext:$t");
        }

    }

    val onErrorAction = object : Consumer<Throwable> {
        override fun accept(t: Throwable?) {
            Log.d("bajie", "onError:${t?.message}");
        }

    }


    data class Student(val name: String, val course: ArrayList<String>);

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rxjava_test);
        observable.subscribe(observer);
        observable.subscribe(onNextAction);
        observable.subscribe(onNextAction, onErrorAction);
        observable.subscribe(onNextAction, onErrorAction, onCompletedAction);

        Observable.just(R.mipmap.ic_launcher_round)
            .map(object : Function<Int, Bitmap> {
                override fun apply(t: Int): Bitmap {
                    return BitmapFactory.decodeResource(resources, t);
                }
            })
            .subscribe(object : Consumer<Bitmap> {
                override fun accept(t: Bitmap?) {
                    findViewById<ImageView>(R.id.img).setImageBitmap(t);
                }

            });

        val students = ArrayList<Student>();
        val course = ArrayList<String>();
        course.add("数学");
        course.add("语文");
        students.add(Student("test", course));
        students.add(Student("test2", course));


        val subscriber = object :Consumer<String> {
            override fun accept(t: String?) {
                Log.d("bajie", "accept:$t");
            }
        }

        Observable.fromIterable(students)
            .flatMap(object : Function<Student, Observable<String>> {
                override fun apply(t: Student): Observable<String> {
                    return Observable.fromIterable(t.course);
                }
            })
            .subscribe(subscriber);
//            .flatMap(object : Function<Student, Observable<String>> {
//                override fun apply(t: Student): Observable<String> {
//                    return Observable.fromArray(t[])
//                }
//
//
//            })
    }
}

