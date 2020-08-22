package com.sclimin.mykit.app;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.KeyEvent;
import android.view.MotionEvent;

import java.lang.ref.WeakReference;

/**
 * 作者：limin
 * <p>
 * 创建时间：2018/05/29
 */
public abstract class Launcher extends Activity {

    static ApplicationPrepare mPrepared;

    static Launcher sCurrentLauncher;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        synchronized (Application.createLock) {

            if (sCurrentLauncher != null && sCurrentLauncher != this) {
                sCurrentLauncher.finish();
                sCurrentLauncher = null;
            }
            sCurrentLauncher = this;

            if (!Application.isCreated) {
                if (mPrepared == null) {
                    mPrepared = new ApplicationPrepare();
                    mPrepared.mLauncher = new WeakReference<>(this);
                    mPrepared.start();
                }
                else {
                    mPrepared.mLauncher = new WeakReference<>(this);
                }
            }
            else {
                onNext();
            }
        }
    }

    @Override
    protected boolean onCheckApplicationInit() {
        return false;
    }

    protected abstract void onNext();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sCurrentLauncher = null;
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }

    private static class ApplicationPrepare extends Thread {

        private WeakReference<Launcher> mLauncher;

        ApplicationPrepare() {
            super("Application-prepare");
        }

        @Override
        public void run() {
            Sync willPrepare = new Sync(Application.application::onWillPrepare);
            Application.post(willPrepare);
            willPrepare.waitFinish();

            Application.application.onPrepare();

            Sync didPrepare = new Sync(Application.application::onDidPrepared);
            Application.post(didPrepare);
            didPrepare.waitFinish();

            synchronized (Application.createLock) {
                Application.isCreated = true;
                mPrepared = null;
                Launcher launcher = mLauncher.get();
                if (launcher != null) {
                    launcher.runOnUiThread(launcher::onNext);
                }
            }
        }
    }

    private static class Sync implements Runnable {

        private final Object mLock = new Object();
        private boolean mFinish;
        private final Runnable mRunnable;

        Sync(Runnable runnable) {
            mRunnable = runnable;
        }

        @Override
        public void run() {
            if (mRunnable != null) {
                mRunnable.run();
            }
            synchronized (mLock) {
                mFinish = true;
                mLock.notifyAll();
            }
        }

        void waitFinish() {
            synchronized (mLock) {
                while (!mFinish) {
                    try {
                        mLock.wait();
                    }
                    catch (InterruptedException ignored) {
                    }
                }
            }
        }
    }
}
