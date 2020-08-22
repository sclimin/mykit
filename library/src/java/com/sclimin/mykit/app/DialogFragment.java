package com.sclimin.mykit.app;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.annotation.ArrayRes;
import androidx.annotation.BoolRes;
import androidx.annotation.ColorRes;
import androidx.annotation.DimenRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.IntegerRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.sclimin.mykit.R;

import static android.widget.FrameLayout.LayoutParams.MATCH_PARENT;

/**
 * 作者：limin
 * <p>
 * 创建时间：2018/03/19
 * <h2>对话框</h2>
 */
public abstract class DialogFragment extends androidx.fragment.app.DialogFragment implements
        SupportResourceHelper, MainThreadHelper {

    private FixDialog mDialog;
    private View mView;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    final public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        onWillCreateView(savedInstanceState);
        mView = inflater.inflate(getContentView(), container, false);
        return new FixDialogContainer(inflater.getContext(), mView);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        onDidCreateView(mView, savedInstanceState);
    }

    @SuppressWarnings({"unchecked"})
    public final <T extends View> T findViewById(int id) {
        if (mView == null) {
            return null;
        }
        return (T) mView.findViewById(id);
    }

    protected void onWillCreateView(Bundle savedInstanceState) {
    }

    protected void onDidCreateView(View view, Bundle savedInstanceState) {
    }

    protected abstract int getContentView();

    @Override
    public int getTheme() {
        return R.style.MK_Dialog;
    }

    @NonNull
    @Override
    final public Dialog onCreateDialog(Bundle savedInstanceState) {
        onWillCreateDialog(savedInstanceState);
        mDialog = new FixDialog(getActivity(), getTheme());
        mDialog.mInterceptBackPress = isModal();
        onDidCreateDialog(savedInstanceState);
        return mDialog;
    }

    protected boolean isModal() {
        return false;
    }

    /**
     * 对话框已经创建
     * @param savedInstanceState
     */
    protected void onDidCreateDialog(Bundle savedInstanceState) {
    }

    /**
     * 将要创建实际对话框
     * @param savedInstanceState
     */
    protected void onWillCreateDialog(Bundle savedInstanceState) {
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        // 通知当前Activity该Dialog消失
        FragmentActivity activity = getActivity();
        if (activity != null && (activity instanceof Activity)) {
            Activity activity1 = (Activity) activity;
            activity1.onDialogDismiss(this);
        }
    }

    @Override
    public Dialog getDialog() {
        return mDialog;
    }

    /**
     * <h2>修正对话框</h2>
     */
    private static class FixDialog extends Dialog {

        /**
         * 是否拦截返回响应
         */
        private boolean mInterceptBackPress;
        /**
         * 点击外部消失
         */
        private boolean mCanceledOnTouchOutside = true;
        /**
         * 可取消的
         */
        private boolean mCancelable = true;

        FixDialog(@NonNull Context context, int themeResId) {
            super(context, themeResId);
        }

        @SuppressWarnings("all")
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            Window w = getWindow();
            WindowManager.LayoutParams lp = w.getAttributes();
            lp.horizontalWeight = 1;
            lp.verticalWeight = 1;
            lp.gravity = Gravity.FILL;
            w.setAttributes(lp);

            w.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        @Override
        public void onBackPressed() {
            if (mInterceptBackPress) {
                return;
            }
            super.onBackPressed();
        }

        @Override
        public void setCanceledOnTouchOutside(boolean cancel) {
            super.setCanceledOnTouchOutside(cancel);
            mCanceledOnTouchOutside = cancel;
        }

        @Override
        public void setCancelable(boolean flag) {
            super.setCancelable(flag);
            mCancelable = flag;
        }
    }

    /**
     * <h2>对话框容器</h2>
     * 确定外部区域和内部区域，以便响应点击外部消失
     */
    private final class FixDialogContainer extends FrameLayout implements View.OnClickListener {

        private final View mRoot;
        /**
         * 有效区域
         */
        private final Rect mValidBounds;

        public FixDialogContainer(@NonNull Context context, View view) {
            super(context);
            mValidBounds = new Rect();
            mRoot = view;
            super.addView(mRoot, -1, new LayoutParams(MATCH_PARENT, MATCH_PARENT));
            super.setOnClickListener(this);
        }

        @Override
        public void addView(View child, int index, ViewGroup.LayoutParams params) {
        }

        @Override
        protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
            super.onLayout(changed, left, top, right, bottom);
            computeTouchBounds();
        }

        @Override
        public boolean onInterceptTouchEvent(MotionEvent event) {
            final int x = (int) event.getX();
            final int y = (int) event.getY();
            return !mValidBounds.contains(x, y) || super.onInterceptTouchEvent(event);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            final int x = (int) event.getX();
            final int y = (int) event.getY();
            return !mValidBounds.contains(x, y) && super.onTouchEvent(event);
        }

        @Override
        public void setOnClickListener(@Nullable OnClickListener l) {
        }

        private void computeTouchBounds() {
            if (mRoot instanceof ViewGroup) {
                ViewGroup layout = (ViewGroup) mRoot;
                final int count = layout.getChildCount();
                if (count != 0) {
                    boolean noVisibleChildren = true;
                    for (int i = 0; i < count; i++) {
                        View child = layout.getChildAt(i);
                        if (child.getVisibility() == VISIBLE) {
                            noVisibleChildren = false;
                            mValidBounds.union(child.getLeft(), child.getTop(), child.getRight(), child.getBottom());
                        }
                    }

                    if (!noVisibleChildren) {
                        return;
                    }
                }
            }
            mValidBounds.set(getLeft(), getTop(), getRight(), getBottom());
        }

        @Override
        public void onClick(View v) {
            if (mDialog.mCancelable && mDialog.mCanceledOnTouchOutside) {
                dismissAllowingStateLoss();
            }
        }
    }

    @Override
    public final void post(Runnable runnable) {
        Application.post(runnable);
    }

    @Override
    public final void postDelayed(Runnable runnable, long delayMillis) {
        Application.postDelayed(runnable, delayMillis);
    }

    @Override
    public final boolean isMainThread() {
        return Application.isMainThread();
    }

    public final Resources.Theme getSupportTheme() {
        return Application.supportResourceHelper.getSupportTheme();
    }

    public final Resources getSupportResources() {
        return Application.supportResourceHelper.getSupportResources();
    }

    public final float getSupportDimension(@DimenRes int id) {
        return Application.supportResourceHelper.getSupportDimension(id);
    }

    public final int getSupportDimensionPixelSize(@DimenRes int id) {
        return Application.supportResourceHelper.getSupportDimensionPixelSize(id);
    }

    public final int getSupportDimensionPixelOffset(@DimenRes int id) {
        return Application.supportResourceHelper.getSupportDimensionPixelOffset(id);
    }

    public final DisplayMetrics getSupportDisplayMetrics() {
        return Application.supportResourceHelper.getSupportDisplayMetrics();
    }

    public final Drawable getSupportDrawable(@DrawableRes int id) {
        return Application.supportResourceHelper.getSupportDrawable(id);
    }

    public final String getSupportString(@StringRes int id) {
        return Application.supportResourceHelper.getSupportString(id);
    }

    public final String getSupportString(@StringRes int id, Object ...formatArgs) {
        return Application.supportResourceHelper.getSupportString(id, formatArgs);
    }

    public final String[] getSupportStringArray(@ArrayRes int id) {
        return Application.supportResourceHelper.getSupportStringArray(id);
    }

    public final int getSupportColor(int id) {
        return Application.supportResourceHelper.getSupportColor(id);
    }

    public final ColorStateList getSupportColorStateList(@ColorRes int id) {
        return Application.supportResourceHelper.getSupportColorStateList(id);
    }

    public final boolean getSupportBoolean(@BoolRes int id) {
        return Application.supportResourceHelper.getSupportBoolean(id);
    }

    public final int getSupportInteger(@IntegerRes int id) {
        return Application.supportResourceHelper.getSupportInteger(id);
    }

    public final int[] getSupportIntArray(@ArrayRes int id) {
        return Application.supportResourceHelper.getSupportIntArray(id);
    }
}
