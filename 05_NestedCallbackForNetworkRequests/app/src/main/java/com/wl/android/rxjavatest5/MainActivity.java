package com.wl.android.rxjavatest5;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "RxJava";

    // 定义Observable接口类型的网络请求对象
    Observable<Translation1> mObservable1;
    Observable<Translation2> mObservable2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://fy.iciba.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        GetRequest_Interface request = retrofit.create(GetRequest_Interface.class);

        mObservable1 = request.getCall1();
        mObservable2 = request.getCall_2();

        mObservable1.subscribeOn(Schedulers.io())           // （初始被观察者）切换到IO线程进行网络请求1
                .observeOn(AndroidSchedulers.mainThread())  //（新观察者）切换到主线程 处理网络请求1的结果
                .doOnNext(new Consumer<Translation1>() {
                    @Override
                    public void accept(Translation1 translation1) throws Exception {
                        // 对第1次网络请求返回的结果进行操作 = 显示翻译结果
                        Log.d(TAG, "第1次网络请求成功");
                        Log.d(TAG, translation1.toString());
                    }
                })

                .observeOn(Schedulers.io())
                // （新被观察者，同时也是新观察者）切换到IO线程去发起登录请求
                // 特别注意：因为flatMap是对初始被观察者作变换，所以对于旧被观察者，它是新观察者，所以通过observeOn切换线程
                // 但对于初始观察者，它则是新的被观察者
                .flatMap(new Function<Translation1, ObservableSource<Translation2>>() {
                    @Override
                    public ObservableSource<Translation2> apply(@NonNull Translation1 translation1) throws Exception {
                        // 将网络请求1转换成网络请求2，即发送网络请求2
                        return mObservable2;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())  // （初始观察者）切换到主线程 处理网络请求2的结果
                .subscribe(new Consumer<Translation2>() {
                    @Override
                    public void accept(Translation2 translation2) throws Exception {
                        // 对第2次网络请求返回的结果进行操作 = 显示翻译结果
                        Log.d(TAG, "第2次网络请求成功");
                        Log.d(TAG, translation2.toString());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d(TAG, "登录失败");
                    }
                });
    }
}
