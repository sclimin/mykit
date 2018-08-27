package com.sclimin.mykit.app;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * 作者：limin
 * <p>
 * 创建时间：2018/08/14
 */
public class Adapter extends RecyclerView.Adapter<Adapter.MKViewHolder> implements MainThreadHelper {

    private final List<? extends Item> mItems;

    public Adapter(List<? extends Item> items) {
        mItems = items;
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
