package com.sclimin.mykit.app;

/**
 * 作者：limin
 * <p>
 * 创建时间：2018/08/23
 */
interface MainThreadHelper {
    void post(Runnable runnable);
    void postDelayed(Runnable runnable, long delayMillis);
    boolean isMainThread();
}
