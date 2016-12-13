package version1.dishq.dishq.util;


import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.multidex.MultiDex;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by dishq on 13-12-2016.
 * Package name version1.dishq.dishq.
 */

public final class DishqApplication extends android.support.multidex.MultiDexApplication {

    public static DishqApplication application;

    public boolean wasInBackground = true;

    private Timer mActivityTransitionTimer;
    private TimerTask mActivityTransitionTimerTask;

    Application.ActivityLifecycleCallbacks activityCallbacks = new ActivityLifecycleCallbacks() {
        @Override
        public void onActivityCreated(Activity activity, Bundle bundle) {

        }

        @Override
        public void onActivityStarted(Activity activity) {

        }

        @Override
        public void onActivityResumed(Activity activity) {
            if (wasInBackground) {
                //Do app-wide came-here-from-background code
            }
            stopActivityTransitionTimer();
        }

        @Override
        public void onActivityPaused(Activity activity) {
            startActivityTransitionTimer();
        }

        @Override
        public void onActivityStopped(Activity activity) {

        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {

        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        registerActivityLifecycleCallbacks(activityCallbacks);
    }

    public static synchronized DishqApplication getInstance() {
        return application;
    }
    public static Context getContext() {
        return application;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(application);
    }

    public void startActivityTransitionTimer() {
        this.mActivityTransitionTimer = new Timer();
        this.mActivityTransitionTimerTask = new TimerTask() {
            public void run() {
                // Task is run when app is exited
                wasInBackground = true;
            }
        };

        long MAX_ACTIVITY_TRANSITION_TIME_MS = 2000;
        this.mActivityTransitionTimer.schedule(mActivityTransitionTimerTask,
                MAX_ACTIVITY_TRANSITION_TIME_MS);
    }

    public void stopActivityTransitionTimer() {
        if (this.mActivityTransitionTimerTask != null) {
            this.mActivityTransitionTimerTask.cancel();
        }

        if (this.mActivityTransitionTimer != null) {
            this.mActivityTransitionTimer.cancel();
        }

        this.wasInBackground = false;
    }

}
