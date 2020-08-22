package com.sclimin.mykit.widget.mask;

import android.content.Context;
import android.graphics.Canvas;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * 作者：limin
 * <p>
 * 创建时间：2018/08/2
 */
public class MaskFrameLayout extends FrameLayout {

    private final MaskDelegate mMaskDelegate;

    public MaskFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MaskFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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
