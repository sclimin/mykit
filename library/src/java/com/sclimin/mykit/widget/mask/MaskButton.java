package com.sclimin.mykit.widget.mask;

import android.content.Context;
import android.graphics.Canvas;
import androidx.appcompat.widget.AppCompatButton;
import android.util.AttributeSet;

/**
 * 作者：limin
 * <p>
 * 创建时间：2018/03/28
 */

public class MaskButton extends AppCompatButton {

    private final MaskDelegate mMaskDelegate;

    public MaskButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MaskButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mMaskDelegate = new MaskDelegate(this, attrs, defStyleAttr);
        mMaskDelegate.drawableState(getDrawableState());
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        mMaskDelegate.drawableState(getDrawableState());
    }

    @Override
    protected void onSizeChanged(int w, int h, int ow, int oh) {
        super.onSizeChanged(w, h, ow, oh);
        mMaskDelegate.sizeChange(w, h);
    }

    @Override
    public void draw(Canvas canvas) {
        mMaskDelegate.draw(canvas, super::draw);
    }
}
