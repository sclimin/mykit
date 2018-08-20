package com.sclimin.mykit.app;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ArrayRes;
import android.support.annotation.BoolRes;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntegerRes;
import android.support.annotation.StringRes;
import android.support.multidex.MultiDexApplication;
import android.util.DisplayMetrics;

import java.util.LinkedList;
import java.util.List;

/**
 * 作者：limin
 * <p>
 * 创建时间：2018/03/16
 */
public class Application extends MultiDexApplication {

    protected static final String TAG = "Application";

    static Application application;
    static SupportResourceHelper supportResourceHelper;
    static boolean isCreated;
    static final Object LOCKER = new Object();

    private ActivityManager activityManager;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        supportResourceHelper = new AppSupportResourceHelper(getResources(), getTheme());
        activityManager = new ActivityManager();
        registerActivityLifecycleCallbacks(activityManager);
    }

    protected static Application getApplication() {
        return application;
    }

    protected void onPrepare() {
    }

    ActivityManager getActivityManager() {
        return activityManager;
    }

    public static abstract class SimpleActivityLifecycleCallbacks implements ActivityLifecycleCallbacks {
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) { }
        public void onActivityStarted(Activity activity) { }
        public void onActivityResumed(Activity activity) { }
        public void onActivityPaused(Activity activity) { }
        public void onActivityStopped(Activity activity) { }
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) { }
        public void onActivityDestroyed(Activity activity) { }
    }

    static class ActivityManager extends SimpleActivityLifecycleCallbacks {

        private final LinkedList<Activity> mActivities = new LinkedList<>();

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            mActivities.addFirst(activity);
        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            mActivities.remove(activity);
        }

        @Override
        public void onActivityResumed(Activity activity) {
            super.onActivityResumed(activity);
        }

        public void restart() {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.setPackage(application.getPackageName());
            List<ResolveInfo> resolveInfos = application.getPackageManager()
                    .queryIntentActivities(intent, 0);
            if (resolveInfos == null || resolveInfos.isEmpty()) {
                throw new Error("");
            }

            String name = resolveInfos.get(0).activityInfo.name;
            try {
                Class<?> cls = Class.forName(name);
                intent.setClass(application, cls);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                for (Activity activity : mActivities) {
                    activity.finish();
                    activity.overridePendingTransition(0, 0);
                }
                application.startActivity(intent);
            }
            catch (ClassNotFoundException e) {
                throw new Error(e);
            }
        }

        public void clear(Activity skip) {
            for (Activity activity : mActivities) {
                if (activity == skip) {
                    continue;
                }
                activity.finish();
                activity.overridePendingTransition(0, 0);
            }
        }
    }

    private static class AppSupportResourceHelper implements SupportResourceHelper {

        private final Resources mResources;
        private final Resources.Theme mTheme;

        private AppSupportResourceHelper(Resources resources, Resources.Theme theme) {
            this.mResources = resources;
            this.mTheme = theme;
        }

        @Override
        public Resources.Theme getSupportTheme() {
            return mTheme;
        }

        @Override
        public Resources getSupportResources() {
            return mResources;
        }

        @Override
        public float getSupportDimension(@DimenRes int id) {
            return mResources.getDimension(id);
        }

        @Override
        public int getSupportDimensionPixelSize(@DimenRes int id) {
            return mResources.getDimensionPixelSize(id);
        }

        @Override
        public int getSupportDimensionPixelOffset(@DimenRes int id) {
            return mResources.getDimensionPixelOffset(id);
        }

        @Override
        public DisplayMetrics getSupportDisplayMetrics() {
            return mResources.getDisplayMetrics();
        }

        @Override
        public Drawable getSupportDrawable(@DrawableRes int id) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                return mResources.getDrawable(id, mTheme);
            }
            else {
                return mResources.getDrawable(id);
            }
        }

        @Override
        public String getSupportString(@StringRes int id) {
            return mResources.getString(id);
        }

        @Override
        public String getSupportString(@StringRes int id, Object... formatArgs) {
            return mResources.getString(id, formatArgs);
        }

        @Override
        public String[] getSupportStringArray(@ArrayRes int id) {
            return mResources.getStringArray(id);
        }

        @Override
        public int getSupportColor(@ColorRes int id) {
            return mResources.getColor(id);
        }

        @Override
        public ColorStateList getSupportColorStateList(@ColorRes int id) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return mResources.getColorStateList(id, mTheme);
            }
            else {
                return mResources.getColorStateList(id);
            }
        }

        @Override
        public boolean getSupportBoolean(@BoolRes int id) {
            return mResources.getBoolean(id);
        }

        @Override
        public int getSupportInteger(@IntegerRes int id) {
            return mResources.getInteger(id);
        }

        @Override
        public int[] getSupportIntArray(@ArrayRes int id) {
            return mResources.getIntArray(id);
        }
    }
}
