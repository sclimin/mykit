package com.sclimin.mykit.app;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.annotation.ArrayRes;
import android.support.annotation.BoolRes;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntegerRes;
import android.support.annotation.StringRes;
import android.util.DisplayMetrics;
import android.util.SparseArray;
import android.view.View;

/**
 * 作者：limin
 * <p>
 * 创建时间：2018/08/14
 */
public abstract class Item implements SupportResourceHelper {

    protected abstract int getItemLayout();

    protected abstract void onBind(Adapter adapter, Helper helper, int position);

    public static final class Helper {
        private final View mView;
        private final SparseArray<View> mCache;

        Helper(View view) {
            mView = view;
            mCache = new SparseArray<>();
        }

        public <T extends View> T findViewById(int resId) {
            View view = mCache.get(resId);
            if (view == null) {
                view = mView.findViewById(resId);
                mCache.put(resId, view);
            }
            return (T) view;
        }

        public View getItemView() {
            return mView;
        }
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
