package com.wl.android.functionaloperators;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.concurrent.TimeUnit;

import io.reactivex.Notification;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.BiPredicate;
import io.reactivex.functions.BooleanSupplier;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
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

//        threadschedule();

//        delay();

//        doLifeCycle();

//        ErrorHandle.onErrorReturn();
//        ErrorHandle.onErrorResumeNext();
//        ErrorHandle.onExceptionResumeNext();
//        ErrorHandle.retry();
//        ErrorHandle.retryUntil();
//        ErrorHandle.retryWhen();

//        repeat();
        repeatWhen();
    }

    /**
     * 重复发送:有条件地、重复发送 被观察者事件
     * 将原始 Observable 停止发送事件的标识（Complete（） / Error（））转换成1个 Object 类型数据
     * 传递给1个新被观察者（Observable），以此决定是否重新订阅 & 发送原来的 Observable
     * 1.若新被观察者（Observable）返回1个Complete / Error事件，则不重新订阅 & 发送原来的 Observable
     * 2.若新被观察者（Observable）返回其余事件时，则重新订阅 & 发送原来的 Observable
     */
    private void repeatWhen() {
        Observable.just(1, 2, 3)
                .repeatWhen(new Function<Observable<Object>, ObservableSource<?>>() {
                    @Override
                    public ObservableSource<?> apply(@NonNull Observable<Object> objectObservable) throws Exception {
                        // 将原始 Observable 停止发送事件的标识（Complete（） /  Error（））转换成1个 Object 类型数据传递给1个新被观察者（Observable）
                        // 以此决定是否重新订阅 & 发送原来的 Observable
                        // 此处有2种情况：
                        // 1. 若新被观察者（Observable）返回1个Complete（） /  Error（）事件，则不重新订阅 & 发送原来的 Observable
                        // 2. 若新被观察者（Observable）返回其余事件，则重新订阅 & 发送原来的 Observable
                        return objectObservable.flatMap(new Function<Object, ObservableSource<?>>() {
                            @Override
                            public ObservableSource<?> apply(@NonNull Object o) throws Exception {
                                // 情况1：若新被观察者（Observable）返回1个Complete（） /  Error（）事件，则不重新订阅 & 发送原来的 Observable
//                                return Observable.empty();
                                // Observable.empty() = 发送Complete事件，但不会回调观察者的onComplete（）

//                                 return Observable.error(new Throwable("不再重新订阅事件"));
                                // 返回Error事件 = 回调onError（）事件，并接收传过去的错误信息。

                                // 情况2：若新被观察者（Observable）返回其余事件，则重新订阅 & 发送原来的 Observable
                                return Observable.just(1);
                                // 仅仅是作为1个触发重新订阅被观察者的通知，发送的是什么数据并不重要，只要不是Complete（） /  Error（）事件
                            }
                        });
                    }
                }).subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "开始采用subscribe连接");
            }

            @Override
            public void onNext(Integer value) {
                Log.d(TAG, "接收到了事件" + value);
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "对Error事件作出响应：" + e.toString());
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "对Complete事件作出响应");
            }

        });
    }

    /**
     * 重复发送:无条件地、重复发送 被观察者事件,具备重载方法，可设置重复创建次数
     */
    private void repeat() {
        // 1. 接收到.onCompleted()事件后，触发重新订阅 & 发送
        // 2. 默认运行在一个新的线程上

        // 具体使用
        Observable.just(1, 2, 3, 4)
                .repeat(3) // 重复创建次数 =- 3次
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "开始采用subscribe连接");
                    }

                    @Override
                    public void onNext(Integer value) {
                        Log.d(TAG, "接收到了事件" + value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "对Error事件作出响应");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "对Complete事件作出响应");
                    }

                });
    }

    /**
     * 错误处理：发送事件过程中，遇到错误时的处理机制
     */
    private static class ErrorHandle {

        /**
         * 遇到错误时，发送1个特殊事件 & 正常终止
         * 可捕获在它之前发生的异常
         */
        static void onErrorReturn() {
            Observable.create(new ObservableOnSubscribe<Integer>() {
                @Override
                public void subscribe(@NonNull ObservableEmitter<Integer> emitter) throws Exception {
                    emitter.onNext(1);
                    emitter.onNext(2);
                    emitter.onError(new Throwable("发生错误了"));
                }
            }).onErrorReturn(new Function<Throwable, Integer>() {
                @Override
                public Integer apply(@NonNull Throwable throwable) throws Exception {
                    // 捕捉错误异常
                    Log.e(TAG, "在onErrorReturn处理了错误: " + throwable.toString());

                    return 666;
                    // 发生错误事件后，发送一个"666"事件，最终正常结束
                }
            }).subscribe(new Observer<Integer>() {
                @Override
                public void onSubscribe(@NonNull Disposable d) {

                }

                @Override
                public void onNext(@NonNull Integer integer) {
                    Log.d(TAG, "接收到了事件" + integer);
                }

                @Override
                public void onError(@NonNull Throwable e) {
                    Log.d(TAG, "对Error事件作出响应");
                }

                @Override
                public void onComplete() {
                    Log.d(TAG, "对Complete事件作出响应");
                }
            });
        }

        /**
         * 遇到错误时，发送1个新的Observable
         * 1.onErrorResumeNext（）拦截的错误 = Throwable；若需拦截Exception请用onExceptionResumeNext（）
         * 2.若onErrorResumeNext（）拦截的错误 = Exception，则会将错误传递给观察者的onError方法
         */
        static void onErrorResumeNext() {
            Observable.create(new ObservableOnSubscribe<Integer>() {
                @Override
                public void subscribe(@NonNull ObservableEmitter<Integer> emitter) throws Exception {
                    emitter.onNext(1);
                    emitter.onNext(2);
                    emitter.onError(new Throwable("发生错误了"));
                }
            }).onErrorResumeNext(new Function<Throwable, ObservableSource<? extends Integer>>() {
                @Override
                public ObservableSource<? extends Integer> apply(@NonNull Throwable throwable) throws Exception {
                    // 1. 捕捉错误异常
                    Log.e(TAG, "在onErrorResumeNext处理了错误: " + throwable.toString());

                    // 2. 发生错误事件后，发送一个新的被观察者 & 发送事件序列
                    return Observable.just(11, 22);

                }
            }).subscribe(new Observer<Integer>() {
                @Override
                public void onSubscribe(Disposable d) {

                }

                @Override
                public void onNext(Integer value) {
                    Log.d(TAG, "接收到了事件" + value);
                }

                @Override
                public void onError(Throwable e) {
                    Log.d(TAG, "对Error事件作出响应");
                }

                @Override
                public void onComplete() {
                    Log.d(TAG, "对Complete事件作出响应");
                }
            });
        }

        /**
         * 遇到错误时，发送1个新的Observable
         * 1.onExceptionResumeNext（）拦截的错误 = Exception；若需拦截Throwable请用onErrorResumeNext（）
         * 2.若onExceptionResumeNext（）拦截的错误 = Throwable，则会将错误传递给观察者的onError方法
         */
        static void onExceptionResumeNext() {
            Observable.create(new ObservableOnSubscribe<Integer>() {
                @Override
                public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                    e.onNext(1);
                    e.onNext(2);
                    e.onError(new Exception("发生错误了"));
                }
            })
                    .onExceptionResumeNext(new Observable<Integer>() {
                        @Override
                        protected void subscribeActual(Observer<? super Integer> observer) {
                            observer.onNext(11);
                            observer.onNext(22);
                            observer.onComplete();
                        }
                    })
                    .subscribe(new Observer<Integer>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(Integer value) {
                            Log.d(TAG, "接收到了事件" + value);
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.d(TAG, "对Error事件作出响应");
                        }

                        @Override
                        public void onComplete() {
                            Log.d(TAG, "对Complete事件作出响应");
                        }
                    });
        }

        /**
         * 重试，即当出现错误时，让被观察者（Observable）重新发送数据
         * 1.接收到 onError（）时，重新订阅 & 发送事件
         * 2.Throwable 和 Exception都可拦截
         */
        static void retry() {
            // 1. retry()
            // 作用：出现错误时，让被观察者重新发送数据
            // 注：若一直错误，则一直重新发送
            Log.d(TAG, "retry（）----------------------------------------");
            Observable.create(new ObservableOnSubscribe<Integer>() {
                @Override
                public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                    e.onNext(1);
                    e.onNext(2);
                    e.onError(new Exception("发生错误了"));
                    e.onNext(3);
                }
            })
                    .retry() // 遇到错误时，让被观察者重新发射数据（若一直错误，则一直重新发送）
                    .subscribe(new Observer<Integer>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(Integer value) {
                            Log.d(TAG, "接收到了事件" + value);
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.d(TAG, "对Error事件作出响应");
                        }

                        @Override
                        public void onComplete() {
                            Log.d(TAG, "对Complete事件作出响应");
                        }
                    });

            // 2. retry（long time）
            // 作用：出现错误时，让被观察者重新发送数据（具备重试次数限制
            // 参数 = 重试次数
            Log.d(TAG, "retry(long time)----------------------------------------");
            Observable.create(new ObservableOnSubscribe<Integer>() {
                @Override
                public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                    e.onNext(1);
                    e.onNext(2);
                    e.onError(new Exception("发生错误了"));
                    e.onNext(3);
                }
            })
                    .retry(3) // 设置重试次数 = 3次
                    .subscribe(new Observer<Integer>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(Integer value) {
                            Log.d(TAG, "接收到了事件" + value);
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.d(TAG, "对Error事件作出响应");
                        }

                        @Override
                        public void onComplete() {
                            Log.d(TAG, "对Complete事件作出响应");
                        }
                    });

            // 3. retry（Predicate predicate）
            // 作用：出现错误后，判断是否需要重新发送数据（若需要重新发送& 持续遇到错误，则持续重试）
            // 参数 = 判断逻辑
            Log.d(TAG, "retry（Predicate predicate）---------------------------------------");
            Observable.create(new ObservableOnSubscribe<Integer>() {
                @Override
                public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                    e.onNext(1);
                    e.onNext(2);
                    e.onError(new Exception("发生错误了"));
                    e.onNext(3);
                }
            })
                    // 拦截错误后，判断是否需要重新发送请求
                    .retry(new Predicate<Throwable>() {
                        @Override
                        public boolean test(@NonNull Throwable throwable) throws Exception {
                            // 捕获异常
                            Log.e(TAG, "retry错误: " + throwable.toString());
                            //返回false = 不重新重新发送数据 & 调用观察者的onError结束
                            //返回true = 重新发送请求（若持续遇到错误，就持续重新发送）
                            return false;
                        }
                    }).subscribe(new Observer<Integer>() {
                @Override
                public void onSubscribe(Disposable d) {

                }

                @Override
                public void onNext(Integer value) {
                    Log.d(TAG, "接收到了事件" + value);
                }

                @Override
                public void onError(Throwable e) {
                    Log.d(TAG, "对Error事件作出响应");
                }

                @Override
                public void onComplete() {
                    Log.d(TAG, "对Complete事件作出响应");
                }
            });

            // 4. retry（new BiPredicate<Integer, Throwable>）
            // 作用：出现错误后，判断是否需要重新发送数据（若需要重新发送 & 持续遇到错误，则持续重试
            // 参数 =  判断逻辑（传入当前重试次数 & 异常错误信息）
            Log.d(TAG, "retry（new BiPredicate<Integer, Throwable>）----------------------------------");
            Observable.create(new ObservableOnSubscribe<Integer>() {
                @Override
                public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                    e.onNext(1);
                    e.onNext(2);
                    e.onError(new Exception("发生错误了"));
                    e.onNext(3);
                }
            })
                    // 拦截错误后，判断是否需要重新发送请求
                    .retry(new BiPredicate<Integer, Throwable>() {
                        @Override
                        public boolean test(@NonNull Integer integer, @NonNull Throwable throwable) throws Exception {
                            // 捕获异常
                            Log.e(TAG, "异常错误 =  " + throwable.toString());

                            // 获取当前重试次数
                            Log.e(TAG, "当前重试次数 =  " + integer);

                            //返回false = 不重新重新发送数据 & 调用观察者的onError结束
                            //返回true = 重新发送请求（若持续遇到错误，就持续重新发送）
                            return false;
                        }
                    }).subscribe(new Observer<Integer>() {
                @Override
                public void onSubscribe(Disposable d) {

                }

                @Override
                public void onNext(Integer value) {
                    Log.d(TAG, "接收到了事件" + value);
                }

                @Override
                public void onError(Throwable e) {
                    Log.d(TAG, "对Error事件作出响应");
                }

                @Override
                public void onComplete() {
                    Log.d(TAG, "对Complete事件作出响应");
                }
            });

            // 5. retry（long time,Predicate predicate）
            // 作用：出现错误后，判断是否需要重新发送数据（具备重试次数限制
            // 参数 = 设置重试次数 & 判断逻辑
            Log.d(TAG, " retry（long time,Predicate predicate）----------------------------------");
            Observable.create(new ObservableOnSubscribe<Integer>() {
                @Override
                public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                    e.onNext(1);
                    e.onNext(2);
                    e.onError(new Exception("发生错误了"));
                    e.onNext(3);
                }
            })
                    // 拦截错误后，判断是否需要重新发送请求
                    .retry(3, new Predicate<Throwable>() {
                        @Override
                        public boolean test(@NonNull Throwable throwable) throws Exception {
                            // 捕获异常
                            Log.e(TAG, "retry错误: " + throwable.toString());

                            //返回false = 不重新重新发送数据 & 调用观察者的onError（）结束
                            //返回true = 重新发送请求（最多重新发送3次）
                            return true;
                        }
                    }).subscribe(new Observer<Integer>() {
                @Override
                public void onSubscribe(Disposable d) {

                }

                @Override
                public void onNext(Integer value) {
                    Log.d(TAG, "接收到了事件" + value);
                }

                @Override
                public void onError(Throwable e) {
                    Log.d(TAG, "对Error事件作出响应");
                }

                @Override
                public void onComplete() {
                    Log.d(TAG, "对Complete事件作出响应");
                }
            });
        }

        /**
         * 出现错误后，判断是否需要重新发送数据
         * 1.若需要重新发送 & 持续遇到错误，则持续重试
         * 2.作用类似于retry（Predicate predicate）,唯一区别：返回 true 则不重新发送数据事件。
         */
        static void retryUntil() {
            Observable.create(new ObservableOnSubscribe<Integer>() {
                @Override
                public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                    e.onNext(1);
                    e.onNext(2);
                    e.onError(new Exception("发生错误了"));
                    e.onNext(3);
                }
            }).retryUntil(new BooleanSupplier() {
                @Override
                public boolean getAsBoolean() throws Exception {
                    // 返回 true 则不重新发送数据事件。
                    // 返回 false 则重新发送数据事件。
                    return true;
                }
            }).subscribe(new Observer<Integer>() {
                @Override
                public void onSubscribe(Disposable d) {

                }

                @Override
                public void onNext(Integer value) {
                    Log.d(TAG, "接收到了事件" + value);
                }

                @Override
                public void onError(Throwable e) {
                    Log.d(TAG, "对Error事件作出响应");
                }

                @Override
                public void onComplete() {
                    Log.d(TAG, "对Complete事件作出响应");
                }
            });
        }

        /**
         * 遇到错误时，将发生的错误传递给一个新的被观察者（Observable），
         * 并决定是否需要重新订阅原始被观察者（Observable）& 发送事件
         */
        static void retryWhen() {
            Observable.create(new ObservableOnSubscribe<Integer>() {
                @Override
                public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                    e.onNext(1);
                    e.onNext(2);
                    e.onError(new Exception("发生错误了"));
                    e.onNext(3);
                }
            })
                    // 遇到error事件才会回调
                    .retryWhen(new Function<Observable<Throwable>, ObservableSource<?>>() {
                        @Override
                        public ObservableSource<?> apply(@NonNull Observable<Throwable> throwableObservable) throws Exception {
                            // 参数Observable<Throwable>中的泛型 = 上游操作符抛出的异常，可通过该条件来判断异常的类型
                            // 返回Observable<?> = 新的被观察者 Observable（任意类型）

                            // 此处有两种情况：
                            // 1. 若 新的被观察者 Observable发送的事件 = Error事件，那么 原始Observable则不重新发送事件：
                            // 2. 若 新的被观察者 Observable发送的事件 = Next事件 ，那么原始的Observable则重新发送事件：
                            return throwableObservable.flatMap(new Function<Throwable, ObservableSource<?>>() {
                                @Override
                                public ObservableSource<?> apply(@NonNull Throwable throwable) throws Exception {
                                    // 1. 若返回的Observable发送的事件 = Error事件，则原始的Observable不重新发送事件
                                    // 该异常错误信息可在观察者中的onError（）中获得
//                                    return Observable.error(new Throwable("retryWhen终止啦"));

                                    // 2. 若返回的Observable发送的事件 = Next事件，则原始的Observable重新发送事件（若持续遇到错误，则持续重试）
                                    return Observable.just(1);
                                }
                            });
                        }
                    }).subscribe(new Observer<Integer>() {
                @Override
                public void onSubscribe(Disposable d) {

                }

                @Override
                public void onNext(Integer value) {
                    Log.d(TAG, "接收到了事件" + value);
                }

                @Override
                public void onError(Throwable e) {
                    Log.d(TAG, "对Error事件作出响应" + e.toString());
                    // 获取异常错误信息
                }

                @Override
                public void onComplete() {
                    Log.d(TAG, "对Complete事件作出响应");
                }
            });
        }

    }

    /**
     * 在事件的生命周期中操作：在某个事件的生命周期中调用
     */
    private void doLifeCycle() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onNext(3);
                emitter.onError(new Throwable("发生错误了"));
            }
        })      // 1. 当Observable每发送1次数据事件就会调用1次
                .doOnEach(new Consumer<Notification<Integer>>() {
                    @Override
                    public void accept(Notification<Integer> integerNotification) throws Exception {
                        Log.d(TAG, "doOnEach: " + integerNotification.getValue());
                    }
                })
                // 2. 执行Next事件前调用
                .doOnNext(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "doOnNext: " + integer);
                    }
                })
                // 3. 执行Next事件后调用
                .doAfterNext(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "doAfterNext: " + integer);
                    }
                })
                // 4. Observable正常发送事件完毕后调用
                .doOnComplete(new Action() {
                    @Override
                    public void run() throws Exception {
                        Log.e(TAG, "doOnComplete: ");
                    }
                })
                // 5. Observable发送错误事件时调用
                .doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d(TAG, "doOnError: " + throwable.getMessage());
                    }
                })
                // 6. 观察者订阅时调用
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        Log.e(TAG, "doOnSubscribe: ");
                    }
                })
                // 7. Observable发送事件完毕后调用，无论正常发送完毕 / 异常终止
                .doAfterTerminate(new Action() {
                    @Override
                    public void run() throws Exception {
                        Log.e(TAG, "doAfterTerminate: ");
                    }
                })
                // 8. 最后执行
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        Log.e(TAG, "doFinally: ");
                    }
                })
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.d(TAG, "开始采用subscribe连接");
                    }

                    @Override
                    public void onNext(@NonNull Integer integer) {
                        Log.d(TAG, "接收到了事件" + integer);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d(TAG, "对Error事件作出响应");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "对Complete事件作出响应");
                    }
                });
    }

    /**
     * 延迟操作：使得被观察者延迟一段时间再发送事件
     */
    private void delay() {
        // 1. 指定延迟时间
        // 参数1 = 时间；参数2 = 时间单位
        // delay(long delay,TimeUnit unit)

        // 2. 指定延迟时间 & 调度器
        // 参数1 = 时间；参数2 = 时间单位；参数3 = 线程调度器
        // delay(long delay,TimeUnit unit,mScheduler scheduler)

        // 3. 指定延迟时间  & 错误延迟
        // 错误延迟，即：若存在Error事件，则如常执行，执行后再抛出错误异常
        // 参数1 = 时间；参数2 = 时间单位；参数3 = 错误延迟参数
        // delay(long delay,TimeUnit unit,boolean delayError)

        // 4. 指定延迟时间 & 调度器 & 错误延迟
        // 参数1 = 时间；参数2 = 时间单位；参数3 = 线程调度器；参数4 = 错误延迟参数
        // delay(long delay,TimeUnit unit,mScheduler scheduler,boolean delayError):
        // 指定延迟多长时间并添加调度器，错误通知可以设置是否延迟

        Observable.just(1, 2, 3)
                .delay(3, TimeUnit.SECONDS) // 延迟3s再发送，由于使用类似，所以此处不作全部展示
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Integer value) {
                        Log.d(TAG, "接收到了事件" + value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "对Error事件作出响应");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "对Complete事件作出响应");
                    }
                });
    }

    /**
     * 线程调度
     */
    private void threadschedule() {
        //步骤4：创建Retrofit对象
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://fy.iciba.com/") // 设置 网络请求 Url
                .addConverterFactory(GsonConverterFactory.create()) //设置使用Gson解析(记得加入依赖)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()) // 支持RxJava
                .build();

        // 步骤5：创建 网络请求接口 的实例
        GetRequest_Interface request = retrofit.create(GetRequest_Interface.class);

        // 步骤6：采用Observable<...>形式 对 网络请求 进行封装
        Observable<Translation> observable = request.getCall();

        // 步骤7：发送网络请求
        observable.subscribeOn(Schedulers.io())               // 在IO线程进行网络请求
                .observeOn(AndroidSchedulers.mainThread())  // 回到主线程 处理请求结果
                .subscribe(new Observer<Translation>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "开始采用subscribe连接");
                    }

                    @Override
                    public void onNext(Translation result) {
                        // 步骤8：对返回的数据进行处理
                        Log.d(TAG, result.toString());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "请求失败");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "请求成功");
                    }
                });

    }


}
