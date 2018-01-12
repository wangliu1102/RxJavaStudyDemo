package com.wl.android.rxjavatest2;

import android.util.Log;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

import static com.wl.android.rxjavatest2.MainActivity.TAG;

/**
 * Created by D22397 on 2018/1/11.
 */

public class DelayCreation {

    /**
     * 直到有观察者（Observer ）订阅时，才动态创建被观察者对象（Observable） & 发送事件
     * 每次订阅后，都会得到一个刚创建的最新的Observable对象，这可以确保Observable对象里的数据是最新的
     */
    static Integer i = 10;

    static void defer() {
        // 第1次对i赋值
        final Observable<Integer> observable = Observable.defer(new Callable<ObservableSource<? extends Integer>>() {

            @Override
            public ObservableSource<? extends Integer> call() throws Exception {
                return Observable.just(i);
            }
        });

        // 第2次对i赋值
        i = 15;

        // 注：此时，才会调用defer（）创建被观察者对象（Observable）
        observable.subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                Log.d(TAG, "开始采用subscribe连接");
            }

            @Override
            public void onNext(@NonNull Integer integer) {
                Log.d(TAG, "接收到的整数是" + integer);
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
     * 延迟指定时间后，发送1个数值0（Long类型），一般用于检测
     */
    static void timer() {
        // 注：timer操作符默认运行在一个新线程上
        // 也可自定义线程调度器（第3个参数）：timer(long,TimeUnit,Scheduler)

        // 该例子 = 延迟2s后，发送一个long类型数值
        Observable.timer(2, TimeUnit.SECONDS)
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.d(TAG, "开始采用subscribe连接");
                    }

                    @Override
                    public void onNext(@NonNull Long aLong) {
                        Log.d(TAG, "接收到了事件" + aLong);
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
     * 每隔指定时间 就发送 事件
     * 发送的事件序列 = 从0开始、无限递增1的的整数序列
     */
    static void interval() {
        // 注：interval默认在computation调度器上执行
        // 也可自定义指定线程调度器（第3个参数）：interval(long,TimeUnit,Scheduler)

        // 参数说明：
        // 参数1 = 第1次延迟时间；
        // 参数2 = 间隔时间数字；
        // 参数3 = 时间单位；

        // 该例子发送的事件序列特点：延迟3s后发送事件，每隔1秒产生1个数字（从0开始递增1，无限个）
        Observable.interval(3, 1, TimeUnit.SECONDS)
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.d(TAG, "开始采用subscribe连接");
                    }

                    @Override
                    public void onNext(@NonNull Long aLong) {
                        Log.d(TAG, "接收到了事件" + aLong);
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
     * 每隔指定时间 就发送 事件，可指定发送的数据的数量
     */
    static void intervalRange() {
        // 参数说明：
        // 参数1 = 事件序列起始点；
        // 参数2 = 事件数量；
        // 参数3 = 第1次事件延迟发送时间；
        // 参数4 = 间隔时间数字；
        // 参数5 = 时间单位

        // 该例子发送的事件序列特点：
        // 1. 从3开始，一共发送10个事件；
        // 2. 第1次延迟2s发送，之后每隔2秒产生1个数字（从0开始递增1，无限个）
        Observable.intervalRange(3, 10, 2, 1, TimeUnit.SECONDS)
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.d(TAG, "开始采用subscribe连接");
                    }

                    @Override
                    public void onNext(@NonNull Long aLong) {
                        Log.d(TAG, "接收到了事件" + aLong);
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
     * 连续发送 1个事件序列，可指定范围,无延迟发送事件
     */
    static void range() {
        // 参数说明：
        // 参数1 = 事件序列起始点；
        // 参数2 = 事件数量；
        // 注：若设置为负数，则会抛出异常

        // 该例子发送的事件序列特点：从3开始发送，每次发送事件递增1，一共发送10个事件
        Observable.range(3, 10)
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.d(TAG, "开始采用subscribe连接");
                    }

                    @Override
                    public void onNext(@NonNull Integer integer) {
                        Log.d(TAG, "接收到了事件" + integer);
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
     * 类似于range（），区别在于该方法支持数据类型 = Long
     * 不作过多描述
     */
    static void rangeLong(){

    }
}
