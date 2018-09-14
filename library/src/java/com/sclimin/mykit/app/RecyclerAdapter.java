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
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：limin
 * <p>
 * 创建时间：2018/08/14
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MKViewHolder> implements Adapter {

    private final List<? extends Item> mItems;

    public RecyclerAdapter(List<? extends Item> items) {
        mItems = items;
    }

    public RecyclerAdapter() {
        mItems = new ArrayList<>();
    }

    public final List<? extends Item> getItems() {
        return mItems;
    }

    @NonNull
    @Override
    public final MKViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(viewType, parent, false);
        return new MKViewHolder(view);
    }

    @Override
    public final void onBindViewHolder(@NonNull MKViewHolder holder, int position) {
        Item item = mItems.get(position);
        item.onBind(this, holder.getHelper(), position);
    }

    @Override
    public final int getItemCount() {
        return mItems.size();
    }

    @Override
    public final int getItemViewType(int position) {
        return mItems.get(position).getItemLayout();
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
    public final String getSupportString(@StringRes int id, Object ...formatArgs) {
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

    static final class MKViewHolder extends RecyclerView.ViewHolder {

        private final Item.Helper mHelper;

        MKViewHolder(View itemView) {
            super(itemView);
            mHelper = new Item.Helper(itemView);
        }

        Item.Helper getHelper() {
            return mHelper;
        }
    }
}
