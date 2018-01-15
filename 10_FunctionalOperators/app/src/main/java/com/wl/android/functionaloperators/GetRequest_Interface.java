package com.wl.android.functionaloperators;

import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * Created by D22397 on 2018/1/15.
 */

public interface GetRequest_Interface {

    @GET("ajax.php?a=fy&f=auto&t=auto&w=hi%20world")
    Observable<Translation> getCall();
}
