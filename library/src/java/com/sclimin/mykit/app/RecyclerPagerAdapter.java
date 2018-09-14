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
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.view.PagerAdapter;
import android.util.DisplayMetrics;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * 作者：limin
 * <p>
 * 创建时间：2018/09/14
 */
public class RecyclerPagerAdapter extends PagerAdapter implements Adapter {

    private final SparseArray<Stack<ViewHolder>> mRecyclerCache = new SparseArray<>();

    private final List<? extends Item> mItems;

    public RecyclerPagerAdapter(List<? extends Item> items) {
        this.mItems = items;
    }

    public RecyclerPagerAdapter() {
        this.mItems = new ArrayList<>();
    }

    public final List<? extends Item> getItems() {
        return mItems;
    }

    @NonNull
    @Override
    public final Object instantiateItem(@NonNull ViewGroup container, int position) {
        Item item = mItems.get(position);
        int resId = item.getItemLayout();

        Stack<ViewHolder> cache = mRecyclerCache.get(resId);
        if (cache == null) {
            cache = new Stack<>();
            mRecyclerCache.append(resId, cache);
        }

        ViewHolder viewHolder = !cache.isEmpty() ? cache.pop() :
                createViewHolder(container, resId, item);
        viewHolder.mItem.onBind(this, viewHolder.mHelper, position);
        container.addView(viewHolder.mView);
        return viewHolder;
    }

    private ViewHolder createViewHolder(ViewGroup container, int resId, Item item) {
        LayoutInflater inflater = LayoutInflater.from(container.getContext());
        return new ViewHolder(inflater.inflate(resId, container, false), resId, item);
    }

    @Override
    public final int getCount() {
        return mItems.size();
    }

    @Override
    public final boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return object instanceof ViewHolder && ((ViewHolder) object).mView == view;
    }

    @Override
    public final void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        if (object instanceof ViewHolder) {
            ViewHolder viewHolder = (ViewHolder) object;

            container.removeView(viewHolder.mView);

            Stack<ViewHolder> cache = mRecyclerCache.get(viewHolder.mResId);
            if (cache == null) {
                cache = new Stack<>();
                mRecyclerCache.put(viewHolder.mResId, cache);
            }
            cache.push(viewHolder);
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

    @Override
    public final Resources.Theme getSupportTheme() {
        return Application.supportResourceHelper.getSupportTheme();
    }

    @Override
    public final Resources getSupportResources() {
        return Application.supportResourceHelper.getSupportResources();
    }

    @Override
    public final float getSupportDimension(@DimenRes int id) {
        return Application.supportResourceHelper.getSupportDimension(id);
    }

    @Override
    public final int getSupportDimensionPixelSize(@DimenRes int id) {
        return Application.supportResourceHelper.getSupportDimensionPixelSize(id);
    }

    @Override
    public final int getSupportDimensionPixelOffset(@DimenRes int id) {
        return Application.supportResourceHelper.getSupportDimensionPixelOffset(id);
    }

    @Override
    public final DisplayMetrics getSupportDisplayMetrics() {
        return Application.supportResourceHelper.getSupportDisplayMetrics();
    }

    @Override
    public final Drawable getSupportDrawable(@DrawableRes int id) {
        return Application.supportResourceHelper.getSupportDrawable(id);
    }

    @Override
    public final String getSupportString(@StringRes int id) {
        return Application.supportResourceHelper.getSupportString(id);
    }

    @Override
    public final String getSupportString(@StringRes int id, Object... formatArgs) {
        return Application.supportResourceHelper.getSupportString(id, formatArgs);
    }

    @Override
    public final String[] getSupportStringArray(@ArrayRes int id) {
        return Application.supportResourceHelper.getSupportStringArray(id);
    }

    @Override
    public final int getSupportColor(int id) {
        return Application.supportResourceHelper.getSupportColor(id);
    }

    @Override
    public final ColorStateList getSupportColorStateList(@ColorRes int id) {
        return Application.supportResourceHelper.getSupportColorStateList(id);
    }

    @Override
    public final boolean getSupportBoolean(@BoolRes int id) {
        return Application.supportResourceHelper.getSupportBoolean(id);
    }

    @Override
    public final int getSupportInteger(@IntegerRes int id) {
        return Application.supportResourceHelper.getSupportInteger(id);
    }

    @Override
    public final int[] getSupportIntArray(@ArrayRes int id) {
        return Application.supportResourceHelper.getSupportIntArray(id);
    }

    private static class ViewHolder {
        final View mView;
        final Item mItem;
        final int mResId;
        final Item.Helper mHelper;

        private ViewHolder(View view, int resId, Item item) {
            this.mView = view;
            this.mItem = item;
            this.mResId = resId;
            this.mHelper = new Item.Helper(mView);
        }
    }
}
