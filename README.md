# RxJavaStudyDemo
RxJava学习的Demo

RxJava用法总结
-------------------------------------------------------------------------------------------------------------------------
http://blog.csdn.net/wangliu1102/article/details/79067437

用的到的依赖项
-------------------------------------------------------------------------------------------------------------------------

// Android 支持 Rxjava

// 此处一定要注意使用RxJava2的版本

compile 'io.reactivex.rxjava2:rxjava:2.1.8'

compile 'io.reactivex.rxjava2:rxandroid:2.0.1'

// Android 支持 Retrofit

compile 'com.squareup.retrofit2:retrofit:2.3.0'

// 衔接 Retrofit & RxJava

compile 'com.squareup.retrofit2:adapter-rxjava2:2.3.0'

// 支持Gson解析

compile 'com.squareup.retrofit2:converter-gson:2.3.0'

// rxjava绑定Android的UI部件的API

compile 'com.jakewharton.rxbinding2:rxbinding:2.0.0'

-------------------------------------------------------------------------------------------------------------------------

RxJava学习系列的文章，可以参考Carson_Ho大神的RxJava系列文章：http://blog.csdn.net/carson_ho/article/category/7227390

一、RxJava入门 
-------------------------------------------------------------------------------------------------------------------------
  
   01_RxJavaTest （http://blog.csdn.net/carson_ho/article/details/78179340 ）

-------------------------------------------------------------------------------------------------------------------------

二、创建操作符 
-------------------------------------------------------------------------------------------------------------------------  
   02_CreateOperator 
            （http://blog.csdn.net/carson_ho/article/details/78246732 ）           
            
            
例1：网络请求轮询 --> 03_NetworkRequestPolling 
            （http://blog.csdn.net/carson_ho/article/details/78256466 ）
            
-------------------------------------------------------------------------------------------------------------------------

三、变换操作符
-------------------------------------------------------------------------------------------------------------------------
   04_TransformationOperator
            （http://blog.csdn.net/carson_ho/article/details/78315437 ）            
            
            
例1：网络请求嵌套回调 --> 05_NestedCallbackForNetworkRequests
            （http://blog.csdn.net/carson_ho/article/details/78315696 ）
             
-------------------------------------------------------------------------------------------------------------------------

四、组合/合并操作符 
-------------------------------------------------------------------------------------------------------------------------
   06_CombinedOrMergeOperator
            （http://blog.csdn.net/carson_ho/article/details/78455349 ）            
            
            
例1：从磁盘/内存缓存中获取缓存数据 --> 07_GetCachedDataFromDiskOrMemory（http://blog.csdn.net/carson_ho/article/details/78455449 ）


例2：合并数据源&同时展示数据 --> 08_MergeDataAndDisplay（http://blog.csdn.net/carson_ho/article/details/78455544 ）


例3：联合判断 --> 09_JointJudgment （http://blog.csdn.net/carson_ho/article/details/78455624 ）

-------------------------------------------------------------------------------------------------------------------------

五、功能性操作符 
-------------------------------------------------------------------------------------------------------------------------
   10_FunctionalOperators （http://blog.csdn.net/carson_ho/article/details/78537277 ）


例1：线程控制（切换/调度）--> （https://www.jianshu.com/p/5225b2baaecd ）


例2：有条件网络请求轮询 --> 11_ConditionNetworkRequestPolling （http://blog.csdn.net/carson_ho/article/details/78558790 ）


例3：网络请求出错重连 --> 12_NetworkRequestErrorReconnect （http://blog.csdn.net/carson_ho/article/details/78651602 ）

-------------------------------------------------------------------------------------------------------------------------

六、过滤操作符 
-------------------------------------------------------------------------------------------------------------------------

  13_FilterOperator （http://blog.csdn.net/carson_ho/article/details/78683064 ）

例1：联想搜索优化 --> 14_SearchOptimization （http://blog.csdn.net/carson_ho/article/details/78849661 ）


例2：功能防抖 --> 15_FunctionAntiShake （http://blog.csdn.net/carson_ho/article/details/78849689 ）

-------------------------------------------------------------------------------------------------------------------------

七、条件布尔操作符
-------------------------------------------------------------------------------------------------------------------------
  16_ConditionalBooleanOperator （http://blog.csdn.net/carson_ho/article/details/78949306 ）

-------------------------------------------------------------------------------------------------------------------------

八、背压策略
-------------------------------------------------------------------------------------------------------------------------
  17_BackpressureStrategy （https://www.jianshu.com/p/ceb48ed8719d ）

-------------------------------------------------------------------------------------------------------------------------



Android 教你一步步搭建MVP+Retrofit+RxJava网络请求框架


