package com.sclimin.mykit.widget;

import android.content.Context;
import android.content.res.TypedArray;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.sclimin.mykit.R;

/**
 * 作者：limin
 * <p>
 * 创建时间：2018/04/10
 */
public class LoadingView extends View {

    private final int mInvalidateDuration;
    private Anim mCurrentAnim;

    public LoadingView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.LoadingView);
        mInvalidateDuration = ta.getInt(R.styleable.LoadingView_mk_invalidate_duration, 1000 / 60);
        ta.recycle();
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        if (visibility == VISIBLE) {
            restartAnim();
        }
    }

    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (changedView == this && visibility == VISIBLE) {
            restartAnim();
        }
    }

    private void restartAnim() {
        if(isInEditMode()) {
            return;
        }
        if (mCurrentAnim == null) {
            mCurrentAnim = new Anim();
            post(mCurrentAnim);
        }
    }

    protected void onAnimation() {
    }

    @Override
    protected void onSizeChanged(int w, int h, int ow, int oh) {
        super.onSizeChanged(w, h, ow, oh);
    }

    private class Anim implements Runnable {
        @Override
        public void run() {
            onAnimation();
            invalidate();
            if (getWindowVisibility() == VISIBLE && getVisibility() == VISIBLE
                    && mCurrentAnim == this) {
                postDelayed(this, mInvalidateDuration);
            }
            else {
                mCurrentAnim = null;
            }
        }
    }
}
