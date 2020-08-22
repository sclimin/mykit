package com.sclimin.mykit.widget.mask;

import android.content.Context;
import android.graphics.Canvas;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.util.AttributeSet;

/**
 * 作者：limin
 * <p>
 * 创建时间：2018/08/8
 */
public class MaskConstraintLayout extends ConstraintLayout {

    private final MaskDelegate mMaskDelegate;

    public MaskConstraintLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MaskConstraintLayout(Context context, AttributeSet attrs, int defStyleAttr) {
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
