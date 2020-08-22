package com.sclimin.mykit.app;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import androidx.annotation.ArrayRes;
import androidx.annotation.BoolRes;
import androidx.annotation.ColorRes;
import androidx.annotation.DimenRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.IntegerRes;
import androidx.annotation.StringRes;
import android.util.DisplayMetrics;

/**
 * 作者：limin
 * <p>
 * 创建时间：2018/08/20
 */
interface SupportResourceHelper {

    Resources.Theme getSupportTheme();

    Resources getSupportResources();

    float getSupportDimension(@DimenRes int id);

    int getSupportDimensionPixelSize(@DimenRes int id);

    int getSupportDimensionPixelOffset(@DimenRes int id);

    DisplayMetrics getSupportDisplayMetrics();

    Drawable getSupportDrawable(@DrawableRes int id);

    String getSupportString(@StringRes int id);

    String getSupportString(@StringRes int id, Object ...formatArgs);

    String[] getSupportStringArray(@ArrayRes int id);

    int getSupportColor(int id);

    ColorStateList getSupportColorStateList(@ColorRes int id);

    boolean getSupportBoolean(@BoolRes int id);

    int getSupportInteger(@IntegerRes int id);

    int[] getSupportIntArray(@ArrayRes int id);
}
