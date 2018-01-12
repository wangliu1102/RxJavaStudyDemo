package com.wl.android.rxjavatest5;

import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * Created by D22397 on 2018/1/11.
 */

public interface GetRequest_Interface {

    // 网络请求1
    @GET("ajax.php?a=fy&f=auto&t=auto&w=hi%20register")
    Observable<Translation1> getCall1();

    // 网络请求2
    @GET("ajax.php?a=fy&f=auto&t=auto&w=hi%20login")
    Observable<Translation2> getCall_2();
}
