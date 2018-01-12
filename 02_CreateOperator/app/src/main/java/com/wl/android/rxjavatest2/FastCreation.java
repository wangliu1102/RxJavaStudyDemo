package com.wl.android.rxjavatest2;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

import static com.wl.android.rxjavatest2.MainActivity.TAG;

/**
 * Created by D22397 on 2018/1/11.
 */

public class FastCreation {
    /**
     * 该方法创建的被观察者对象发送事件的特点：不发送任何事件
     */
    static void never() {
        Observable.never()
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.d(TAG, "开始采用subscribe连接");
                    }

                    @Override
                    public void onNext(@NonNull Object o) {
                        Log.d(TAG, "接收到了事件" + o);
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
     * 该方法创建的被观察者对象发送事件的特点：仅发送Error事件，直接通知异常
     * 可自定义异常
     */
    static void error() {
        Observable.error(new RuntimeException())
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.d(TAG, "开始采用subscribe连接");
                    }

                    @Override
                    public void onNext(@NonNull Object o) {
                        Log.d(TAG, "接收到了事件" + o);
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
     * 该方法创建的被观察者对象发送事件的特点：仅发送Complete事件，直接通知完成
     */
    static void empty() {
        Observable.empty()
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.d(TAG, "开始采用subscribe连接");
                    }

                    @Override
                    public void onNext(@NonNull Object object) {
                        Log.d(TAG, "接收到了事件" + object);
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
     * 1.快速创建 被观察者对象（Observable） & 发送10个以上事件（集合形式）
     * 2.集合元素遍历
     */
    static void fromIterable() {
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        Observable.fromIterable(list)
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.d(TAG, "集合遍历");
                    }

                    @Override
                    public void onNext(@NonNull Integer integer) {
                        Log.d(TAG, "集合中的数据元素 = " + integer);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d(TAG, "对Error事件作出响应");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "遍历结束");
                    }
                });
    }

    /**
     * 1.快速创建 被观察者对象（Observable） & 发送10个以上事件（数组形式）
     * 2.数组元素遍历
     */
    static void fromArray() {
        Integer[] items = {1, 2, 3, 4};
        Observable.fromArray(items)
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.d(TAG, "数组遍历");
                    }

                    @Override
                    public void onNext(@NonNull Integer integer) {
                        Log.d(TAG, "数组中的元素 = " + integer);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d(TAG, "对Error事件作出响应");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "遍历结束");
                    }
                });
    }

    /**
     * 快速创建 被观察者对象（Observable） & 发送10个以下事件
     */
    static void just() {
        // 1. 创建时传入整型1、2、3、4
        // 在创建后就会发送这些对象，相当于执行了onNext(1)、onNext(2)、onNext(3)、onNext(4)
        Observable.just(1, 2, 3, 4)
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
}
