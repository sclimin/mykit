package com.sclimin.mykit.widget.mask;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Xfermode;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import com.sclimin.mykit.R;

/**
 * 作者：limin
 * <p>
 * 创建时间：2018/03/26
 */

final class MaskDelegate {
    private final View mView;
    private final Drawable mMask;
    private final Paint mPaint;
    private final Xfermode mXfermode;
    private final boolean mShowEditMode;

    MaskDelegate(View view, AttributeSet set, int defStyleAttr) {
        final Context context = view.getContext();

        this.mView = view;
        view.setWillNotDraw(false);

        TypedArray ta = context.obtainStyledAttributes(set,
                R.styleable.MaskDelegate, defStyleAttr, 0);
        this.mMask = ta.getDrawable(R.styleable.MaskDelegate_mk_mask);
        int maskMode = ta.getInt(R.styleable.MaskDelegate_mk_mask_mode, -1);
        mShowEditMode = ta.getBoolean(R.styleable.MaskDelegate_mk_show_edit_mode, false);
        ta.recycle();

        switch (maskMode) {
            case 0:
                this.mXfermode = new PorterDuffXfermode(PorterDuff.Mode.DST);
                break;
            case 1:
                this.mXfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC);
                break;
            case 2:
                this.mXfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_IN);
                break;
            case 3:
                this.mXfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);
                break;
            case 4:
                this.mXfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_ATOP);
                break;
            case 5:
                this.mXfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP);
                break;
            case -1:
            default:
                this.mXfermode = null;
                break;
        }

        this.mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        this.mPaint.setFilterBitmap(true);
        this.mPaint.setAntiAlias(true);
        this.mPaint.setXfermode(mXfermode);
    }

    public void sizeChange(int w, int h) {
        if (mMask != null) {
            mMask.setBounds(0, 0, w, h);
        }
    }

    public void draw(Canvas canvas, DrawSuper drawSuper) {
        if (mMask != null && mXfermode != null && (!mView.isInEditMode() || mShowEditMode)) {
            final Rect bounds = mMask.getBounds();

            canvas.saveLayer(bounds.left, bounds.top, bounds.right, bounds.bottom,
                    null, Canvas.ALL_SAVE_FLAG);

            drawSuper.drawSuper(canvas);

            mPaint.setXfermode(mXfermode);

            canvas.saveLayer(bounds.left, bounds.top, bounds.right, bounds.bottom,
                    mPaint, Canvas.ALL_SAVE_FLAG);

            mMask.draw(canvas);

            canvas.restore();
            canvas.restore();
        }
        else {
            drawSuper.drawSuper(canvas);
        }
    }

    public void drawableState(int[] states) {
        if (mMask != null && mMask.setState(states)) {
            mMask.invalidateSelf();
            mView.invalidate();
        }
    }

    interface DrawSuper {
        void drawSuper(Canvas canvas);
    }
}
