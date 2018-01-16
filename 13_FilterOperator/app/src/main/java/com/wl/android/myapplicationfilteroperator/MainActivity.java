package com.wl.android.myapplicationfilteroperator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "RxJava";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        ConditionFilter.filter();
//        ConditionFilter.ofType();
//        ConditionFilter.skipOrskipLast();
//        ConditionFilter.distinctOrdistinctUntilChanged();

//        EventNumberFilter.take();
//        EventNumberFilter.takeLast();

//        TimeFilter.throttleFirstOrthrottleLast();
//        TimeFilter.throttleWithTimeoutOrdebounce();

//        EventLocationFilter.firstElementOrlastElement();
//        EventLocationFilter.elementAt();
        EventLocationFilter.elementAtOrError();
    }
}
