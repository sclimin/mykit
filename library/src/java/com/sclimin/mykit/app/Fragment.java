package com.sclimin.mykit.app;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

/**
 * 作者：limin
 * <p>
 * 创建时间：2018/03/19
 */

public abstract class Fragment extends android.support.v4.app.Fragment {

    static Resources mResources;
    static Resources.Theme mTheme;

    private View mView;

    private static Handler sHandler = new Handler(Looper.getMainLooper());

    @Nullable
    @Override
    public final View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        onWillCreateView(savedInstanceState);
        return inflater.inflate(getLayoutResource(), container, false);
    }

    protected abstract int getLayoutResource();

    @Override
    public final void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mView = view;
        onDidCreateView(view, savedInstanceState);
    }

    @SuppressWarnings({"unchecked"})
    public final <T extends View> T findViewById(int id) {
        if (mView == null) {
            return null;
        }
        return (T) mView.findViewById(id);
    }

    protected void onWillCreateView(Bundle savedInstanceState) {
    }

    protected void onDidCreateView(View view, Bundle savedInstanceState) {
    }

    public final @NonNull Resources getSupportResources() {
        return mResources;
    }

    public final String getSupportString(int id) {
        return mResources.getString(id);
    }

    public final Drawable getSupportDrawable(int id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return mResources.getDrawable(id, mTheme);
        }
        return mResources.getDrawable(id);
    }

    public void showToast(String message) {
        showToast(message, false);
    }

    public void showToast(String message, boolean isLong) {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            if (activity instanceof Activity) {
                ((Activity) activity).showToast(message, isLong);
            }
            else {
                activity.runOnUiThread(() -> Toast.makeText(activity, message,
                        isLong ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT).show());
            }
        }
    }

    public void post(Runnable runnable) {
        sHandler.post(runnable);
    }

    public void postDelayed(Runnable runnable, long delayMillis) {
        sHandler.postDelayed(runnable, delayMillis);
    }
}
