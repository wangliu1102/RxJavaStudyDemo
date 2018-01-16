package com.wl.android.myapplicationfilteroperator;

import android.util.Log;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

import static com.wl.android.myapplicationfilteroperator.MainActivity.TAG;

/**
 * Created by D22397 on 2018/1/16.
 */

public class EventLocationFilter {

    /**
     * 仅选取第1个元素 / 最后一个元素
     */
    static void firstElementOrlastElement() {
        // 获取第1个元素
        Observable.just(1, 2, 3, 4, 5)
                .firstElement()
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "获取到的第一个事件是： " + integer);
                    }
                });

        // 获取最后1个元素
        Observable.just(1, 2, 3, 4, 5)
                .lastElement()
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "获取到的最后1个事件是： " + integer);
                    }
                });
    }

    /**
     * 指定接收某个元素（通过 索引值 确定）
     * 注：允许越界，即获取的位置索引 ＞ 发送事件序列长度
     */
    static void elementAt() {
        // 使用1：获取位置索引 = 2的 元素
        // 位置索引从0开始
        Observable.just(1, 2, 3, 4, 5)
                .elementAt(2)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "获取到的事件元素是： " + integer);
                    }
                });

        // 使用2：获取的位置索引 ＞ 发送事件序列长度时，设置默认参数
        Observable.just(1, 2, 3, 4, 5)
                .elementAt(6, 10)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "获取到的事件元素是： " + integer);
                    }
                });
    }

    /**
     * 在elementAt（）的基础上，当出现越界情况（即获取的位置索引 ＞ 发送事件序列长度）时，即抛出异常
     */
    static void elementAtOrError() {
        Observable.just(1, 2, 3, 4, 5)
                .elementAtOrError(6)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "获取到的事件元素是： " + integer);
                    }
                });
    }
}
