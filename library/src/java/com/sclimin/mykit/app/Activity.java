package com.sclimin.mykit.app;

import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.sclimin.mykit.R;

/**
 * 作者：limin
 * <p>
 * 创建时间：2018/03/16
 */

public abstract class Activity extends AppCompatActivity implements MainThreadHelper {

    private static final String TAG_BAR = "TAG_TOOL_BAR";

    private FrameLayout mToolbarContainer;
    private NavigationBarHelper mHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Window window = getWindow();
        onConfigurationWindow(window);

        if (Build.VERSION.SDK_INT >= 21) {
            mHelper = new NavigationBarHelper(window);

            if (mHelper.update()) {
                onAdjustNavigationBar(mHelper);
            }
        }

        synchronized (Application.createLock) {
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

    protected void onAdjustNavigationBar(NavigationBarHelper helper) {
    }

    protected abstract int getLayoutResource();

    protected void onConfigurationWindow(Window window) {
    }

    protected boolean onCheckApplicationInit() {
        return false;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (mHelper != null && Build.VERSION.SDK_INT >= 21) {
            if (mHelper.update()) {
                onAdjustNavigationBar(mHelper);
            }
        }
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

    @Override
    public final void post(Runnable runnable) {
        Application.post(runnable);
    }

    @Override
    public final void postDelayed(Runnable runnable, long delayMillis) {
        Application.postDelayed(runnable, delayMillis);
    }

    @Override
    public final boolean isMainThread() {
        return Application.isMainThread();
    }

    public static final class NavigationBarHelper {
        private final Window mWindow;
        private final DisplayMetrics mRealMetrics;
        private final Rect mRect;
        private final Rect mTempRect;

        private int mNavigationBarSize;
        private Direction mDirection;

        private NavigationBarHelper(Window window) {
            this.mWindow = window;
            this.mRealMetrics = new DisplayMetrics();
            this.mRect = new Rect();
            this.mTempRect = new Rect();
        }

        @RequiresApi(21)
        boolean update() {
            Display display = mWindow.getWindowManager().getDefaultDisplay();
            display.getRectSize(mTempRect);
            display.getRealMetrics(mRealMetrics);

            boolean isPortrait = mTempRect.width() <= mTempRect.height();

            Direction direction;

            if (mTempRect.width() != mRealMetrics.widthPixels || mTempRect.height() != mRealMetrics.heightPixels) {
                if (mTempRect.width() != mRealMetrics.widthPixels) {
                    // 水平方向上显示
                    mNavigationBarSize = mRealMetrics.widthPixels - mTempRect.width();
                    direction = mTempRect.left != 0 ? Direction.Left : Direction.Right;
                }
                else {
                    // 垂直方向上显示
                    mNavigationBarSize = mRealMetrics.heightPixels - mTempRect.height();
                    direction = mTempRect.top != 0 ? Direction.Top : Direction.Bottom;
                }
            }
            else {
                mNavigationBarSize = 0;
                direction = null;
            }

            boolean shouldUpdate = mDirection != direction || mRect.isEmpty();

            mDirection = direction;
            mRect.set(mTempRect);

            return shouldUpdate;
        }

        public boolean isNavigationBarShowing() {
            return mNavigationBarSize != 0;
        }

        public int getNavigationBarSize() {
            return mNavigationBarSize;
        }

        public Direction getDirection() {
            return mDirection;
        }

        public enum Direction {
            Left(Gravity.LEFT),
            Right(Gravity.RIGHT),
            Top(Gravity.TOP),
            Bottom(Gravity.BOTTOM);

            private final int value;

            Direction(int value) {
                this.value = value;
            }

            public int gravity() {
                return value;
            }
        }
    }
}
