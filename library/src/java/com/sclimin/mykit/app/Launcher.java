package com.sclimin.mykit.app;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.MotionEvent;

import static android.content.pm.PackageManager.GET_META_DATA;

/**
 * 作者：limin
 * <p>
 * 创建时间：2018/05/29
 */
public abstract class Launcher extends Activity {

    static ApplicationPrepare mPrepared;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        doContinue();
    }

    @Override
    protected boolean onCheckApplicationInit() {
        return false;
    }

    private void doContinue() {
        synchronized (Application.LOCKER) {
            if (!Application.isCreated) {
                if (mPrepared == null) {
                    mPrepared = new ApplicationPrepare(this);
                    mPrepared.start();
                }
            }
            else {
                gotoMainActivity(true);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0xF && resultCode == RESULT_OK) {
            synchronized (Application.LOCKER) {
                if (!Application.isCreated) {
                    if (mPrepared == null) {
                        mPrepared = new ApplicationPrepare(this);
                        mPrepared.start();
                    }
                }
                else {
                    gotoMainActivity(true);
                }
            }
        }
    }

    private void gotoMainActivity(boolean im) {
        Class<?> cls = getMainActivity();
        if (cls == null) {
            throw new Error("");
        }
        Intent intent = new Intent(Application.application, cls);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
        if (!im) {
            onOverridePendingTransition();
        }
    }

    protected void onOverridePendingTransition() {
    }

    private Class<?> getMainActivity() {
        try {
            ActivityInfo info = getPackageManager().getActivityInfo(getComponentName(), GET_META_DATA);
            if (info.metaData != null) {
                String activity = info.metaData.getString("Main");
                if (activity != null) {
                    return Class.forName(activity);
                }
            }
        }
        catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onBackPressed() {
    }

    private void onPrepareFinish() {
        Application.application.getActivityManager().clear(this);
        gotoMainActivity(false);
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
        private final Launcher mLauncher;

        ApplicationPrepare(Launcher launcher) {
            super("Application-prepare");
            mLauncher = launcher;
        }

        @Override
        public void run() {
            Application.application.onPrepare();
            synchronized (Application.LOCKER) {
                Application.isCreated = true;
                mPrepared = null;
            }
            mLauncher.runOnUiThread(mLauncher::onPrepareFinish);
        }
    }
}
