package com.sclimin.sample;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import androidx.annotation.Nullable;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;
import android.util.AttributeSet;
import android.view.View;

/**
 * 作者：limin
 * <p>
 * 创建时间：2018/08/14
 */
public class ShadowView extends View {

    private final Paint mPaint;

    private final RectF mBounds = new RectF();
    private final Rect mTemp = new Rect();
    private final float mDensity;

    private final Drawable mDrawable;
    private final Drawable mDrawable2;

    private int mFlag;

    private Runnable mAnim = new Runnable() {
        @Override
        public void run() {
            if (mFlag == 0) {
                mFlag = 0xFFF000;
            }
            mFlag >>= 1;
            postInvalidate();
            postDelayed(this, 80);
        }
    };

    public ShadowView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShadowView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mDensity = context.getResources().getDisplayMetrics().density;
        mPaint = new Paint();
        setLayerType(LAYER_TYPE_SOFTWARE, mPaint);
        mDrawable = VectorDrawableCompat.create(getResources(), R.drawable.loading_one, context.getTheme());
        mDrawable2 = VectorDrawableCompat.create(getResources(), R.drawable.loading_two, context.getTheme());

        post(mAnim);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mBounds.set(0, 0, w, h);
        mBounds.inset(mDensity * 50, mDensity * 50);
        mDrawable.setBounds(0, 0, w, h);

        float dw = mDrawable.getIntrinsicWidth();
        float dh = mDrawable.getIntrinsicHeight();

        float hh = (113.f / 199) * h * 0.5f;
        float ww = dw / dh * hh;

        mTemp.set(0, 0, (int) ww, (int) hh);
        mTemp.offset(w / 2 - mTemp.centerX(), 0);

        mDrawable.setBounds(mTemp);
        mDrawable2.setBounds(mTemp);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.getClipBounds(mTemp);
        for (int i = 0; i < 12; i++) {
            canvas.save();
            canvas.rotate(30 * i, mTemp.centerX(), mTemp.centerY());
            if (((1 << (12 - i)) & mFlag) != 0) {
                mDrawable2.draw(canvas);
            }
            else {
                mDrawable.draw(canvas);
            }
            canvas.restore();
        }
    }
}
