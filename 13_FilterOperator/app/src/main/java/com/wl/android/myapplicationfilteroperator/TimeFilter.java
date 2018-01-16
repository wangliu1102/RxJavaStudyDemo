package com.wl.android.myapplicationfilteroperator;

import android.util.Log;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import static com.wl.android.myapplicationfilteroperator.MainActivity.TAG;

/**
 * Created by D22397 on 2018/1/16.
 */

public class TimeFilter {

    /**
     * 在某段时间内，只发送该段时间内第1次事件 / 最后1次事件
     * 如，1段时间内连续点击按钮，但只执行第1次的点击操作
     */
    static void throttleFirstOrthrottleLast() {
        // 在某段时间内，只发送该段时间内第1次事件
        Log.d(TAG, "在某段时间内，只发送该段时间内第1次事件");
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                // 隔段事件发送时间
                e.onNext(1);
                Thread.sleep(500);

                e.onNext(2);
                Thread.sleep(400);

                e.onNext(3);
                Thread.sleep(300);

                e.onNext(4);
                Thread.sleep(300);

                e.onNext(5);
                Thread.sleep(300);

                e.onNext(6);
                Thread.sleep(400);

                e.onNext(7);
                Thread.sleep(300);
                e.onNext(8);

                Thread.sleep(300);
                e.onNext(9);

                Thread.sleep(300);
                e.onComplete();
            }
        }).throttleFirst(1, TimeUnit.SECONDS) //每1秒中采用数据
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "开始采用subscribe连接");
                    }

                    @Override
                    public void onNext(Integer value) {
                        Log.d(TAG, "接收到了事件" + value);
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

        //在某段时间内，只发送该段时间内最后1次事件
        Log.d(TAG, "在某段时间内，只发送该段时间内最后1次事件");
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                // 隔段事件发送时间
                e.onNext(1);
                Thread.sleep(500);

                e.onNext(2);
                Thread.sleep(400);

                e.onNext(3);
                Thread.sleep(300);

                e.onNext(4);
                Thread.sleep(300);

                e.onNext(5);
                Thread.sleep(300);

                e.onNext(6);
                Thread.sleep(400);

                e.onNext(7);
                Thread.sleep(300);
                e.onNext(8);

                Thread.sleep(300);
                e.onNext(9);

                Thread.sleep(300);
                e.onComplete();
            }
        }).throttleLast(1, TimeUnit.SECONDS) //每1秒中采用数据
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "开始采用subscribe连接");
                    }

                    @Override
                    public void onNext(Integer value) {
                        Log.d(TAG, "接收到了事件" + value);
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
     * 在某段时间内，只发送该段时间内最新（最后）1次事件
     * 与 throttleLast（） 操作符类似
     */
    static void sample() {

    }

    /**
     * 发送数据事件时，若2次发送事件的间隔＜指定时间，就会丢弃前一次的数据，
     * 直到指定时间内都没有新数据发射时才会发送后一次的数据
     */
    static void throttleWithTimeoutOrdebounce() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                // 隔段事件发送时间
                e.onNext(1);
                Thread.sleep(500);
                e.onNext(2); // 1和2之间的间隔小于指定时间1s，所以前1次数据（1）会被抛弃，2会被保留
                Thread.sleep(1500);  // 因为2和3之间的间隔大于指定时间1s，所以之前被保留的2事件将发出
                e.onNext(3);
                Thread.sleep(1500);  // 因为3和4之间的间隔大于指定时间1s，所以3事件将发出
                e.onNext(4);
                Thread.sleep(500); // 因为4和5之间的间隔小于指定时间1s，所以前1次数据（4）会被抛弃，5会被保留
                e.onNext(5);
                Thread.sleep(500); // 因为5和6之间的间隔小于指定时间1s，所以前1次数据（5）会被抛弃，6会被保留
                e.onNext(6);
                Thread.sleep(1500); // 因为6和Complete实践之间的间隔大于指定时间1s，所以之前被保留的6事件将发出

                e.onComplete();
            }
        }).throttleWithTimeout(1, TimeUnit.SECONDS) //每1秒中采用数据
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Integer value) {
                        Log.d(TAG, "接收到了事件" + value);
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
