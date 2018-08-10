package com.sclimin.mykit.widget.mask;

import android.content.Context;
import android.graphics.Canvas;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.AttributeSet;

/**
 * 作者：limin
 * <p>
 * 创建时间：2018/08/8
 */
public class MaskLinearLayout extends LinearLayoutCompat {

    private final MaskDelegate mMaskDelegate;

    public MaskLinearLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MaskLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mMaskDelegate = new MaskDelegate(this, attrs, defStyleAttr);
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
