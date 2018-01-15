package com.wl.android.jointjudgment;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.jakewharton.rxbinding2.widget.RxTextView;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function3;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "RxJava";
    /*
         * 步骤1：设置控件变量 & 绑定
         **/
    private EditText mNameEditText;
    private EditText mAgeEditText;
    private EditText mJobEditText;
    private Button mCommitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mNameEditText = (EditText) findViewById(R.id.name);
        mAgeEditText = (EditText) findViewById(R.id.age);
        mJobEditText = (EditText) findViewById(R.id.job);
        mCommitButton = (Button) findViewById(R.id.commit);

        /*
         * 步骤2：为每个EditText设置被观察者，用于发送监听事件
         * 说明：
         * 1. 此处采用了RxBinding：RxTextView.textChanges(name) = 对对控件数据变更进行监听（功能类似TextWatcher），
         * 需要引入依赖：compile 'com.jakewharton.rxbinding2:rxbinding:2.0.0'
         * 2. 传入EditText控件，点击任1个EditText撰写时，都会发送数据事件 = Function3（）的返回值（下面会详细说明）
         * 3. 采用skip(1)原因：跳过 一开始EditText无任何输入时的空值
         **/
        Observable<CharSequence> nameObservable = RxTextView.textChanges(mNameEditText).skip(1);
        Observable<CharSequence> ageObservable = RxTextView.textChanges(mAgeEditText).skip(1);
        Observable<CharSequence> jobObservable = RxTextView.textChanges(mJobEditText).skip(1);

        /*
         * 步骤3：通过combineLatest（）合并事件 & 联合判断
         **/
        Observable.combineLatest(nameObservable, ageObservable, jobObservable,
                new Function3<CharSequence, CharSequence, CharSequence, Boolean>() {
                    @Override
                    public Boolean apply(@NonNull CharSequence charSequence, @NonNull CharSequence
                            charSequence2, @NonNull CharSequence charSequence3) throws Exception {
                        /*
                         * 步骤4：规定表单信息输入不能为空
                         **/
                        boolean isUserNameValid = !TextUtils.isEmpty(mNameEditText.getText());
                        boolean isUserAgeValid = !TextUtils.isEmpty(mAgeEditText.getText());
                        boolean isUserJobValid = !TextUtils.isEmpty(mJobEditText.getText());
                        /*
                         * 步骤5：返回信息 = 联合判断，即3个信息同时已填写，"提交按钮"才可点击
                         **/
                        return isUserNameValid && isUserAgeValid && isUserJobValid;
                    }
                }).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                /*
                 * 步骤6：返回结果 & 设置按钮可点击样式
                 **/
                Log.e(TAG, "提交按钮是否可点击： " + aBoolean);
                mCommitButton.setEnabled(aBoolean);
            }
        });
    }
}
