package com.sclimin.mykit.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.sclimin.mykit.R;

/**
 * 作者：limin
 * <p>
 * 创建时间：2018/03/16
 */

public abstract class Activity extends AppCompatActivity {

    private static final String TAG_BAR = "TAG_TOOL_BAR";

    private FrameLayout mToolbarContainer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Window window = getWindow();
        onConfigurationWindow(window);

        synchronized (Application.LOCKER) {
            if (!Application.isCreated && onCheckApplicationInit()) {
                Application.application.getActivityManager().restart();
                return;
            }
        }

        int res = getLayoutResource();
        if (res > 0) {
            setContentView(res);
        }

        // 配置Bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setDisplayShowHomeEnabled(false);
        }

        mToolbarContainer = findViewById(R.id.toolbar_container);
    }

    protected abstract int getLayoutResource();

    protected void onConfigurationWindow(Window window) {
    }

    protected boolean onCheckApplicationInit() {
        return false;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    protected final boolean hasToolbarContainer() {
        return mToolbarContainer != null;
    }

    public void showBar(Bar bar) {
        if (hasToolbarContainer()) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.toolbar_container, bar, TAG_BAR)
                    .commitAllowingStateLoss();
        }
    }

    public Bar currBar() {
        android.support.v4.app.Fragment fragment = getSupportFragmentManager().findFragmentByTag(TAG_BAR);
        if (fragment != null && (fragment instanceof Bar)) {
            return (Bar) fragment;
        }
        return null;
    }

    public void clearBar() {
        android.support.v4.app.Fragment fragment = getSupportFragmentManager().findFragmentByTag(TAG_BAR);
        if (fragment != null && (fragment instanceof Bar)) {
            getSupportFragmentManager().beginTransaction()
                    .remove(fragment)
                    .commitAllowingStateLoss();
        }
    }

    public void showToast(String message) {
        showToast(message, false);
    }

    public void showToast(String message, boolean isLong) {
        runOnUiThread(() -> Toast.makeText(this, message,
                isLong ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT).show());

    }

    protected void onDialogDismiss(DialogFragment dialog) {
    }
}
