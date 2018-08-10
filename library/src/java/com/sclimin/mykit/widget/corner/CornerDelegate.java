package com.sclimin.mykit.widget.corner;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Xfermode;
import android.util.AttributeSet;
import android.view.View;

import com.sclimin.mykit.R;

/**
 * 作者：limin
 * <p>
 * 创建时间：2018/08/10
 */
final class CornerDelegate {

    private final View mView;

    /**
     * 圆角半径
     */
    private float mCornerRadius;

    /**
     * 修正后的圆角半径
     */
    private float mFixCornerRadius;

    /**
     * 阴影颜色
     */
    private ColorStateList mShadowColor;

    /**
     * 阴影大小
     */
    private float mShadowSize;

    /**
     * 边界
     */
    private final Rect mBounds;
    private final RectF mBoundsF;

    private final Paint mMaskPaint;
    private final Xfermode mXfermode;

    private final Paint mEdgePaint;
    private final Paint mCornerPaint;

    private final Path mShadowCornerPath;

    private float mX0, mY0;
    private float mX1, mY1;
    private float mXc, mYc;

    private final float[] mGradientPositions = new float[4];
    private final int[] mGradientColors = new int[4];

    @SuppressLint("ResourceType")
    CornerDelegate(View view, AttributeSet set, int defStyleAttr) {
        mView = view;
        mView.setWillNotDraw(false);

        mBounds = new Rect();
        mBoundsF = new RectF();

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);

        mMaskPaint = new Paint(paint);
        mEdgePaint = new Paint(paint);
        mCornerPaint = new Paint(paint);

        mShadowCornerPath = new Path();

        mXfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);

        final int[] attrs = {
                R.attr.mk_corner_radius,
                R.attr.mk_shadow_color,
                R.attr.mk_shadow_size
        };
        TypedArray ta = view.getContext().obtainStyledAttributes(set, attrs, defStyleAttr, 0);
        mCornerRadius = ta.getDimension(0, 0);
        setShadowColor(ta.hasValue(1) ? ta.getColorStateList(1)
                : ColorStateList.valueOf(Color.BLACK));
        mShadowSize = ta.getDimension(2, 0);
        ta.recycle();
    }

    public void setCornerRadius(float cornerRadius) {
        if (cornerRadius >= 0) {
            mCornerRadius = cornerRadius;
            adjustDrawParamChange();
        }
    }

    public void sizeChanged(int w, int h) {
        mBounds.set(0, 0, w, h);
        mBoundsF.set(mBounds);
        adjustDrawParamChange();
    }

    public void setShadowSize(float shadowSize) {
        if (shadowSize >= 0) {
            mShadowSize = shadowSize;
            adjustDrawParamChange();
        }
    }

    public void setShadowColor(ColorStateList shadowColor) {
        mShadowColor = shadowColor == null ? ColorStateList.valueOf(Color.BLACK) : shadowColor;
        adjustShadowPaint();
    }

    private void adjustDrawParamChange() {
        // 修正圆角半径
        float maxRadius = Math.min(mBoundsF.width() * 0.5f, mBounds.height() * 0.5f);
        mFixCornerRadius = mCornerRadius < maxRadius ? mCornerRadius : maxRadius;

        adjustShadowPath();
    }

    private void adjustShadowPath() {
        mShadowCornerPath.reset();

        if (mShadowSize == 0) {
            return;
        }

        final float sw = (mShadowSize + mFixCornerRadius) * 2;

        RectF outBounds = new RectF(
                -mShadowSize,
                -mShadowSize,
                sw -mShadowSize,
                sw -mShadowSize);
        RectF inBounds = new RectF(outBounds);
        inBounds.inset(mShadowSize, mShadowSize);

        mShadowCornerPath.reset();
        mShadowCornerPath.setFillType(Path.FillType.EVEN_ODD);
        mShadowCornerPath.moveTo(0, mFixCornerRadius);
        mShadowCornerPath.rLineTo(-mShadowSize, 0);
        mShadowCornerPath.arcTo(outBounds, 180, 90, false);
        mShadowCornerPath.arcTo(inBounds, 270, -90, false);
        mShadowCornerPath.close();

        mXc = outBounds.centerX();
        mYc = outBounds.centerY();

        mX0 = 0;
        mY0 = mFixCornerRadius;
        mX1 = 0;
        mY1 = -mShadowSize;

        mGradientPositions[0] = 0;
        mGradientPositions[1] = mFixCornerRadius / (mFixCornerRadius + mShadowSize);
        mGradientPositions[2] = (1 + mGradientPositions[1]) * 0.5f;
        mGradientPositions[3] = 1;

        adjustShadowPaint();
    }

    private void adjustShadowPaint() {

        int shadowColor = mShadowColor.getColorForState(mView.getDrawableState(), Color.BLACK);

        int alpha = Color.alpha(shadowColor);
        int color = (0xFFFFFF & shadowColor);

        int as = (int) (alpha * 0.2);
        int am = (int) (alpha * 0.06);

        mGradientColors[0] = (as << 24) | color;
        mGradientColors[1] = mGradientColors[0];
        mGradientColors[2] = (am << 24) | color;
        mGradientColors[3] = 0;

        if ((mFixCornerRadius + mShadowSize) > 0) {
            mCornerPaint.setShader(new RadialGradient(
                    mXc,
                    mYc,
                    mFixCornerRadius + mShadowSize,
                    mGradientColors,
                    mGradientPositions,
                    Shader.TileMode.CLAMP
            ));

            mEdgePaint.setShader(new LinearGradient(
                    mX0, mY0,
                    mX1, mY1,
                    mGradientColors,
                    mGradientPositions,
                    Shader.TileMode.CLAMP
            ));
            mEdgePaint.setAntiAlias(false);
        }
        else {
            mCornerPaint.setShader(null);
            mEdgePaint.setShader(null);
            mEdgePaint.setAntiAlias(false);
        }
    }

    private void drawShadow(Canvas canvas) {
        canvas.save();
        canvas.drawPath(mShadowCornerPath, mCornerPaint);
        canvas.drawRect(
                mFixCornerRadius,
                -mShadowSize,
                mBoundsF.width() - mFixCornerRadius,
                0,
                mEdgePaint);
        canvas.restore();

        canvas.save();
        canvas.rotate(90);
        canvas.translate(0, -mBoundsF.width());
        canvas.drawPath(mShadowCornerPath, mCornerPaint);
        canvas.drawRect(
                mFixCornerRadius,
                -mShadowSize,
                mBoundsF.height() - mFixCornerRadius,
                0,
                mEdgePaint);
        canvas.restore();

        canvas.save();
        canvas.rotate(180);
        canvas.translate(-mBoundsF.width(), -mBoundsF.height());
        canvas.drawPath(mShadowCornerPath, mCornerPaint);
        canvas.drawRect(
                mFixCornerRadius,
                -mShadowSize,
                mBoundsF.width() - mFixCornerRadius,
                0,
                mEdgePaint);
        canvas.restore();

        canvas.save();
        canvas.rotate(270);
        canvas.translate(-mBoundsF.height(), 0);
        canvas.drawPath(mShadowCornerPath, mCornerPaint);
        canvas.drawRect(
                mFixCornerRadius,
                -mShadowSize,
                mBoundsF.height() - mFixCornerRadius,
                0,
                mEdgePaint);
        canvas.restore();
    }

    private void drawMask(Canvas canvas) {
        canvas.save();
        canvas.drawRoundRect(
                mBoundsF,
                mFixCornerRadius,
                mFixCornerRadius,
                mMaskPaint
        );
        canvas.restore();
    }

    public void draw(Canvas canvas, DrawSuper drawSuper) {

        if (mShadowSize != 0) {
            drawShadow(canvas);
        }

        if (mCornerRadius != 0) {
            mMaskPaint.setXfermode(null);
            mMaskPaint.setColor(Color.BLACK);
            int baseCount = canvas.saveLayer(
                    mBounds.left,
                    mBounds.top,
                    mBounds.right,
                    mBounds.bottom,
                    mMaskPaint,
                    Canvas.ALL_SAVE_FLAG);

            // 绘制形状
            drawMask(canvas);

            mMaskPaint.setXfermode(mXfermode);
            int maskCount = canvas.saveLayer(
                    mBounds.left,
                    mBounds.top,
                    mBounds.right,
                    mBounds.bottom,
                    mMaskPaint,
                    Canvas.ALL_SAVE_FLAG);

            // 绘制super
            drawSuper.draw(canvas);

            canvas.restoreToCount(maskCount);
            canvas.restoreToCount(baseCount);
        }
        else {
            drawSuper.draw(canvas);
        }
    }

    public void drawableStateChanged() {
        adjustShadowPaint();
    }

    interface DrawSuper {
        void draw(Canvas canvas);
    }
}
