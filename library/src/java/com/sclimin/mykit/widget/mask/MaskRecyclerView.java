package com.sclimin.mykit.widget.mask;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * 作者：limin
 * <p>
 * 创建时间：2018/08/8
 */
public class MaskRecyclerView extends RecyclerView {

    private final MaskDelegate mMaskDelegate;

    public MaskRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MaskRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mMaskDelegate = new MaskDelegate(this, attrs, defStyle);
    }

    @Override
    protected void onSizeChanged(int w, int h, int ow, int oh) {
        super.onSizeChanged(w, h, ow, oh);
        mMaskDelegate.sizeChange(w, h);
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        if(mMaskDelegate != null) {
            mMaskDelegate.drawableState(getDrawableState());
        }
    }

    @Override
    public void draw(Canvas canvas) {
        mMaskDelegate.draw(canvas, super::draw);
    }
}
