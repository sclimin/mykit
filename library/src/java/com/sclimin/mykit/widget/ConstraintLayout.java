package com.sclimin.mykit.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

/**
 * 作者：limin
 * <p>
 * 创建时间：2018/08/8
 */
public class ConstraintLayout extends android.support.constraint.ConstraintLayout {

    private final MaskLayerHelper mMaskLayerHelper;

    public ConstraintLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ConstraintLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mMaskLayerHelper = new MaskLayerHelper(this, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int ow, int oh) {
        super.onSizeChanged(w, h, ow, oh);
        mMaskLayerHelper.sizeChange(w, h);
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        if(mMaskLayerHelper != null) {
            mMaskLayerHelper.drawableState(getDrawableState());
        }
    }

    @Override
    public void draw(Canvas canvas) {
        mMaskLayerHelper.draw(canvas, super::draw);
    }
}
