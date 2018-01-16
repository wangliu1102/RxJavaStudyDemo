package com.wl.android.myapplicationfilteroperator;

import android.util.Log;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;

import static com.wl.android.myapplicationfilteroperator.MainActivity.TAG;

/**
 * Created by D22397 on 2018/1/16.
 */

public class ConditionFilter {

    /**
     * 过滤 特定条件的事件
     */
    static void filter() {
        Observable.just(1, 2, 3, 4, 5)
                .filter(new Predicate<Integer>() {
                    @Override
                    public boolean test(@NonNull Integer integer) throws Exception {
                        // a. 返回true，则继续发送
                        // b. 返回false，则不发送（即过滤）
                        return integer > 3;
                        // 本例子 = 过滤了整数≤3的事件
                    }
                }).subscribe(new Observer<Integer>() {

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
     * 过滤 特定数据类型的数据
     */
    static void ofType() {
        Observable.just(1, "Carson", 3, "Ho", 5)
                .ofType(Integer.class)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "获取到的整型事件元素是： " + integer);
                    }
                });
    }

    /**
     * 跳过某个事件
     */
    static void skipOrskipLast() {
        // 使用1：根据顺序跳过数据项
        Log.d(TAG, "根据顺序跳过数据项");
        Observable.just(1, 2, 3, 4, 5)
                .skip(1) // 跳过正序的前1项
                .skipLast(2) // 跳过正序的后2项
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "获取到的整型事件元素是： " + integer);
                    }
                });

        // 使用2：根据时间跳过数据项
        Log.d(TAG, "根据时间跳过数据项");
        // 发送事件特点：发送数据0-5，每隔1s发送一次，每次递增1；第1次发送延迟0s
        Observable.intervalRange(0, 5, 0, 1, TimeUnit.SECONDS)
                .skip(1, TimeUnit.SECONDS) // 跳过第1s发送的数据
                .skipLast(1, TimeUnit.SECONDS) // 跳过最后1s发送的数据
                .subscribe(new Consumer<Long>() {

                    @Override
                    public void accept(Long along) throws Exception {
                        Log.d(TAG, "获取到的整型事件元素是： " + along);
                    }
                });
    }

    /**
     * 过滤事件序列中重复的事件 / 连续重复的事件
     */
    static void distinctOrdistinctUntilChanged() {
        // 使用1：过滤事件序列中重复的事件
        Log.d(TAG, "过滤事件序列中重复的事件");
        Observable.just(1, 2, 3, 1, 2)
                .distinct()
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "不重复的整型事件元素是： " + integer);
                    }
                });

        // 使用2：过滤事件序列中 连续重复的事件
        Log.d(TAG, "过滤事件序列中连续重复的事件");
        // 下面序列中，连续重复的事件 = 3、4
        Observable.just(1, 2, 3, 1, 2, 3, 3, 4, 4)
                .distinctUntilChanged()
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "不连续重复的整型事件元素是： " + integer);
                    }
                });
    }


}
