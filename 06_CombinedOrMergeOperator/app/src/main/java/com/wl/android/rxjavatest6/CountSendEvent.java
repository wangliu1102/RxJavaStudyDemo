package com.wl.android.rxjavatest6;

import android.util.Log;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

import static com.wl.android.rxjavatest6.MainActivity.TAG;

/**
 * Created by D22397 on 2018/1/12.
 */

public class CountSendEvent {

    /**
     * 统计被观察者发送事件的数量
     */
    public static void count() {
        Observable.just(1, 2, 3, 4)
                .count()
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        Log.d(TAG, "发送的事件数量 =  " + aLong);
                    }
                });
    }

}
