package com.sclimin.sample;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.util.AttributeSet;

import com.sclimin.mykit.widget.LoadingView;

/**
 * 作者：limin
 * <p>
 * 创建时间：2018/08/14
 */
public class Loading extends LoadingView {

    private final Paint mPaint;

    private final Rect mTemp = new Rect();

    private final Drawable mDrawable, mSecondDrawable;

    private int mFlag;

    public Loading(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Loading(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mDrawable = VectorDrawableCompat.create(getResources(), R.drawable.loading_two, context.getTheme());
        mSecondDrawable = VectorDrawableCompat.create(getResources(), R.drawable.loading_one, context.getTheme());
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    @Override
    protected void onAnimation() {
        if (mFlag == 0) {
            mFlag = 0xFFF000;
        }
        mFlag >>= 1;
    }

    @Override
    protected void onSizeChanged(int w, int h, int ow, int oh) {
        super.onSizeChanged(w, h, ow, oh);
        mDrawable.setBounds(0, 0, w, h);

        float dw = mDrawable.getIntrinsicWidth();
        float dh = mDrawable.getIntrinsicHeight();

        float hh = (113.f / 199) * h * 0.5f;
        float ww = dw / dh * hh;

        mTemp.set(0, 0, (int) ww, (int) hh);
        mTemp.offset(w / 2 - mTemp.centerX(), 0);

        mDrawable.setBounds(mTemp);
        mSecondDrawable.setBounds(mTemp);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.getClipBounds(mTemp);
        for (int i = 0; i < 12; i++) {
            canvas.save();
            canvas.rotate(30 * i, mTemp.centerX(), mTemp.centerY());
            if (((1 << (12 - i)) & mFlag) != 0) {
                mSecondDrawable.draw(canvas);
            }
            else {
                mDrawable.draw(canvas);
            }
            canvas.restore();
        }
    }


}
