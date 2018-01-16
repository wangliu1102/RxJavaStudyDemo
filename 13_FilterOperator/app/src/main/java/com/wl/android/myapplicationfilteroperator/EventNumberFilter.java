package com.wl.android.myapplicationfilteroperator;

import android.util.Log;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import static com.wl.android.myapplicationfilteroperator.MainActivity.TAG;

/**
 * Created by D22397 on 2018/1/16.
 */

public class EventNumberFilter {

    /**
     * 指定观察者最多能接收到的事件数量
     */
    static void take() {
        Observable.just(1, 2, 3, 4, 5)
                .take(2) // 指定了观察者只能接收2个事件
                .subscribe(new Observer<Integer>() {

                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "开始采用subscribe连接");
                    }

                    @Override
                    public void onNext(Integer value) {
                        Log.d(TAG, "过滤后得到的事件是：" + value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "对Error事件作出响应");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "对Complete事件作出响应");
                    }
                });

    }

    /**
     * 指定观察者只能接收到被观察者发送的最后几个事件
     */
    static void takeLast() {
        Observable.just(1, 2, 3, 4, 5)
                .takeLast(3) // 指定观察者只能接受被观察者发送的最后3个事件
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "开始采用subscribe连接");
                    }

                    @Override
                    public void onNext(Integer value) {
                        Log.d(TAG, "过滤后得到的事件是：" + value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "对Error事件作出响应");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "对Complete事件作出响应");
                    }
                });
    }
}
