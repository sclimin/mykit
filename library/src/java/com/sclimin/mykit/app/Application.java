package com.sclimin.mykit.app;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.multidex.MultiDexApplication;

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
    static boolean isCreated;
    static final Object LOCKER = new Object();

    private ActivityManager activityManager;

    @Override
    public void onCreate() {
        super.onCreate();
        Fragment.mResources = getResources();
        Fragment.mTheme = getTheme();
        application = this;
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
}
