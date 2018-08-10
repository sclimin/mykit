package com.sclimin.mykit.widget.corner;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

/**
 * 作者：limin
 * <p>
 * 创建时间：2018/08/10
 */
public class CornerImageView extends AppCompatImageView implements CornerView {

    private final CornerDelegate mDelegate;

    public CornerImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CornerImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mDelegate = new CornerDelegate(this, attrs, defStyleAttr);
    }

    @Override
    public void setCornerRadius(float cornerRadius) {
        mDelegate.setCornerRadius(cornerRadius);
    }

    @Override
    public void setCornerRadiusRes(int radiusRes) {
        mDelegate.setCornerRadius(getResources().getDimension(radiusRes));
    }

    @Override
    public void setShadowColor(int shadowColor) {
        mDelegate.setShadowColor(ColorStateList.valueOf(shadowColor));
    }

    @Override
    public void setShadowColorRes(int colorRes) {
        mDelegate.setShadowColor(getResources().getColorStateList(colorRes));
    }

    @Override
    public void setShadowSize(float shadowSize) {
        mDelegate.setShadowSize(shadowSize);
    }

    @Override
    public void setShadowSizeRes(int shadowSizeRes) {
        mDelegate.setShadowSize(getResources().getDimension(shadowSizeRes));
    }

    @Override
    public void draw(Canvas canvas) {
        mDelegate.draw(canvas, super::draw);
    }

    @Override
    protected void onSizeChanged(int w, int h, int ow, int oh) {
        super.onSizeChanged(w, h, ow, oh);
        mDelegate.sizeChanged(w, h);
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        mDelegate.drawableStateChanged();
    }
}
