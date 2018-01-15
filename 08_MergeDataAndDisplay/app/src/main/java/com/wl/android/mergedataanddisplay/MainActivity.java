package com.wl.android.mergedataanddisplay;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "RxJava";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        mergeExample();

        zipExample();
    }

    /**
     * Zip（）例子：结合Retrofit 与RxJava，实现较为复杂的合并2个网络请求向2个服务器获取数据 & 统一展示
     */
    private void zipExample() {
        Observable<Translation1> observable1;
        Observable<Translation2> observable2;

        // 步骤1：创建Retrofit对象
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://fy.iciba.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        // 步骤2：创建 网络请求接口 的实例
        GetRequest_Interface request = retrofit.create(GetRequest_Interface.class);

        // 步骤3：采用Observable<...>形式 对 2个网络请求 进行封装
        observable1 = request.getCall1().subscribeOn(Schedulers.io());
        observable2 = request.getCall2().subscribeOn(Schedulers.io());

        // 步骤4：通过使用Zip（）对两个网络请求进行合并再发送
        Observable.zip(observable1, observable2, new BiFunction<Translation1, Translation2, String>() {
            @Override
            public String apply(@NonNull Translation1 translation1, @NonNull Translation2 translation2) throws Exception {
                return translation1.toString() + "&" + translation2.toString();
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Log.d(TAG, "最终接收到的数据是：" + s);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d(TAG, "登录失败");
                    }
                });
    }

    /**
     * Merge（）例子 ：实现较为简单的从（网络 + 本地）获取数据 & 统一展示
     */
    private void mergeExample() {
        // 用于存放最终展示的数据
        final StringBuilder result = new StringBuilder();
        result.append("数据源来自 = ");
        /*
         * 设置第1个Observable：通过网络获取数据
         * 此处仅作网络请求的模拟
         **/
        Observable<String> network = Observable.just("网络");

        /*
         * 设置第2个Observable：通过本地文件获取数据
         * 此处仅作本地文件请求的模拟
         **/
        Observable<String> file = Observable.just("本地文件");

        /*
         * 通过merge（）合并事件 & 同时发送事件
         **/
        Observable.merge(network, file)
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull String s) {
                        Log.d(TAG, "数据源有： " + s);
                        result.append(s + "+");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d(TAG, "对Error事件作出响应");
                    }

                    @Override
                    public void onComplete() {

                        Log.d(TAG, "获取数据完成");
                        Log.d(TAG, result.toString());
                    }
                });
    }

}
