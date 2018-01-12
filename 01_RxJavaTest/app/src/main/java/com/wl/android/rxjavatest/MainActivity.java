package com.wl.android.rxjavatest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "RxJavaTest";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Step();

        callChaining();
    }

    /**
     * 基于事件流的链式调用方式
     */
    private void callChaining() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onNext(3);
                emitter.onComplete();
            }
        }).subscribe(new Observer<Integer>() {

            // 采用 Disposable.dispose() 切断观察者 与 被观察者 之间的连接
            // 1. 定义Disposable类变量
            private Disposable disposable;

            @Override
            public void onSubscribe(@NonNull Disposable d) {
                // 2. 对Disposable类变量赋值
                disposable = d;
                Log.d(TAG, "开始采用subscribe连接");
            }

            @Override
            public void onNext(@NonNull Integer integer) {
                Log.d(TAG, "对Next事件" + integer + "作出响应");
                if (integer == 2) {
                    // 设置在接收到第二个事件后切断观察者和被观察者的连接
                    disposable.dispose();
                    Log.d(TAG, "已经切断了连接：" + disposable.isDisposed());
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.d(TAG, "对Error事件作出响应");
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "对Complete事件作出响应");
            }
        });
    }

    /**
     * 分步骤实现
     */
    private void Step() {
        // 步骤1：创建被观察者 Observable & 生产事件

        //  1. 创建被观察者 Observable 对象
        Observable<Integer> observable = Observable.create(new ObservableOnSubscribe<Integer>() {

            // 2. 在复写的subscribe（）里定义需要发送的事件

            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onNext(3);
                emitter.onComplete();
            }
        });

        // 步骤2：创建观察者 Observer 并 定义响应事件行为

        Observer<Integer> observer = new Observer<Integer>() {

            // 注：整体方法调用顺序：
            // 观察者.onSubscribe（）> 被观察者.subscribe（）> 观察者.onNext（）>观察者.onComplete()

            //  默认最先调用复写的 onSubscribe（）
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                Log.d(TAG, "开始采用subscribe连接");
            }

            @Override
            public void onNext(@NonNull Integer integer) {
                Log.d(TAG, "对Next事件" + integer + "作出响应");
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.d(TAG, "对Error事件作出响应");
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "对Complete事件作出响应");
            }
        };

        // 步骤3：通过订阅（subscribe）连接观察者和被观察者

        observable.subscribe(observer);
    }
}
