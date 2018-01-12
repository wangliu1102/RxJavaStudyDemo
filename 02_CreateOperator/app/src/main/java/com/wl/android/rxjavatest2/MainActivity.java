package com.wl.android.rxjavatest2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "RxJavaTest2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        FastCreation.just();
//        FastCreation.fromArray();
//        FastCreation.fromIterable();
//        FastCreation.empty();
//        FastCreation.error();
//        FastCreation.never();

//        DelayCreation.defer();
//        DelayCreation.timer();
//        DelayCreation.interval();
//        DelayCreation.intervalRange();
        DelayCreation.range();
    }


}
