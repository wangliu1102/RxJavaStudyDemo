package com.wl.android.getcacheddatafromdiskormemory;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "RxJava";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 用于模拟内存缓存 & 磁盘缓存中的数据
        final String memoryCache = null;
        final String diskCache = "从磁盘缓存中获取数据";

         /*
         * 设置第1个Observable：检查内存缓存是否有该数据的缓存
         **/
        Observable<String> memory = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> emitter) throws Exception {
                if (memoryCache != null) {
                    emitter.onNext(memoryCache);
                } else {
                    emitter.onComplete();
                }
            }
        });

        /*
         * 设置第2个Observable：检查磁盘缓存是否有该数据的缓存
         **/
        Observable<String> disk = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> emitter) throws Exception {
                if (diskCache != null) {
                    emitter.onNext(diskCache);
                } else {
                    emitter.onComplete();
                }
            }
        });

        /*
         * 设置第3个Observable：通过网络获取数据
         **/
        Observable<String> network = Observable.just("从网络中获取数据");

        // 1. 通过concat（）合并memory、disk、network 3个被观察者的事件
        // （即检查内存缓存、磁盘缓存 & 发送网络请求）并将它们按顺序串联成队列
        Observable.concat(memory, disk, network)
                // 2. 通过firstElement()，从串联队列中取出并发送第1个有效事件（Next事件），
                // 即依次判断检查memory、disk、network
                .firstElement()
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Log.d(TAG, "最终获取的数据来源 =  " + s);
                    }
                });
    }
}
