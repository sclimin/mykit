package com.sclimin.mykit.app;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.annotation.ArrayRes;
import androidx.annotation.BoolRes;
import androidx.annotation.ColorRes;
import androidx.annotation.DimenRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.IntegerRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

/**
 * 作者：limin
 * <p>
 * 创建时间：2018/03/19
 */

public abstract class Fragment extends androidx.fragment.app.Fragment implements SupportResourceHelper, MainThreadHelper {

    private View mView;

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

    public final Resources.Theme getSupportTheme() {
        return Application.supportResourceHelper.getSupportTheme();
    }

    public final Resources getSupportResources() {
        return Application.supportResourceHelper.getSupportResources();
    }

    public final float getSupportDimension(@DimenRes int id) {
        return Application.supportResourceHelper.getSupportDimension(id);
    }

    public final int getSupportDimensionPixelSize(@DimenRes int id) {
        return Application.supportResourceHelper.getSupportDimensionPixelSize(id);
    }

    public final int getSupportDimensionPixelOffset(@DimenRes int id) {
        return Application.supportResourceHelper.getSupportDimensionPixelOffset(id);
    }

    public final DisplayMetrics getSupportDisplayMetrics() {
        return Application.supportResourceHelper.getSupportDisplayMetrics();
    }

    public final Drawable getSupportDrawable(@DrawableRes int id) {
        return Application.supportResourceHelper.getSupportDrawable(id);
    }

    public final String getSupportString(@StringRes int id) {
        return Application.supportResourceHelper.getSupportString(id);
    }

    public final String getSupportString(@StringRes int id, Object ...formatArgs) {
        return Application.supportResourceHelper.getSupportString(id, formatArgs);
    }

    public final String[] getSupportStringArray(@ArrayRes int id) {
        return Application.supportResourceHelper.getSupportStringArray(id);
    }

    public final int getSupportColor(int id) {
        return Application.supportResourceHelper.getSupportColor(id);
    }

    public final ColorStateList getSupportColorStateList(@ColorRes int id) {
        return Application.supportResourceHelper.getSupportColorStateList(id);
    }

    public final boolean getSupportBoolean(@BoolRes int id) {
        return Application.supportResourceHelper.getSupportBoolean(id);
    }

    public final int getSupportInteger(@IntegerRes int id) {
        return Application.supportResourceHelper.getSupportInteger(id);
    }

    public final int[] getSupportIntArray(@ArrayRes int id) {
        return Application.supportResourceHelper.getSupportIntArray(id);
    }
}
