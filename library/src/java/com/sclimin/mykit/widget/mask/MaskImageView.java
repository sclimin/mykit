package com.sclimin.mykit.widget.mask;

import android.content.Context;
import android.graphics.Canvas;
import androidx.appcompat.widget.AppCompatImageView;
import android.util.AttributeSet;

/**
 * 作者：limin
 * <p>
 * 创建时间：2018/08/8
 */
public class MaskImageView extends AppCompatImageView {

    private final MaskDelegate mMaskHelper;

    public MaskImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MaskImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mMaskHelper = new MaskDelegate(this, attrs, defStyleAttr);
        mMaskHelper.drawableState(getDrawableState());
    }

    @Override
    protected void onSizeChanged(int w, int h, int ow, int oh) {
        super.onSizeChanged(w, h, ow, oh);
        mMaskHelper.sizeChange(w, h);
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        mMaskHelper.drawableState(getDrawableState());
    }

    @Override
    public void draw(Canvas canvas) {
        mMaskHelper.draw(canvas, super::draw);
    }
}
