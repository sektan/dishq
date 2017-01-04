package version1.dishq.dishq.util;


import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import version1.dishq.dishq.server.Config;
import version1.dishq.dishq.server.RestApi;

/**
 * Created by dishq on 13-12-2016.
 * Package name version1.dishq.dishq.
 */

public final class DishqApplication extends android.support.multidex.MultiDexApplication {

    public static DishqApplication application;
    private static SharedPreferences prefs;
    private static String uniqueId;
    private static int userID;
    private static String accessToken;
    private static String tokenType;
    private static String facebookOrGoogle;
    private static Boolean IS_NEW_USER;
    private static Boolean ON_BOARDING_DONE;
    public boolean wasInBackground = true;
    private static String userName;

    private Timer mActivityTransitionTimer;
    private TimerTask mActivityTransitionTimerTask;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        uniqueId = getPrefs().getString(Constants.UNIQUE_IDENTIFIER, null);
        accessToken = getPrefs().getString(Constants.ACCESS_TOKEN, null);
        tokenType = getPrefs().getString(Constants.TOKEN_TYPE, null);
        Util.ACCESS_TOKEN = tokenType + " " + accessToken;
        userName = getPrefs().getString(Constants.USER_NAME, null);
        facebookOrGoogle = getPrefs().getString(Constants.FACEBOOK_OR_GOOGLE, null);
        IS_NEW_USER = getPrefs().getBoolean(Constants.IS_NEW_USER, false);
        userID = getPrefs().getInt(Constants.USER_ID, 0);
        ON_BOARDING_DONE = getPrefs().getBoolean(Constants.ON_BOARDING_DONE, false);
        registerActivityLifecycleCallbacks(activityCallbacks);
    }

    public static SharedPreferences getPrefs(){
        if(prefs == null){
            prefs = application.getSharedPreferences(Constants.DISHQ_APP_PREFS, MODE_PRIVATE);
        }
        return prefs;
    }

    public static void setAccessToken(String accessToken, String tokenType) {
        DishqApplication.accessToken = accessToken;
        DishqApplication.tokenType = tokenType;
        Util.ACCESS_TOKEN = tokenType + " " + accessToken;
    }

    public static String getAccessToken(){
        return Util.ACCESS_TOKEN;
    }

    public static String getUserName() {
        return DishqApplication.userName;
    }

    public static void setUserName(String userName) {
        DishqApplication.userName = userName;
    }

    public static void setFacebookOrGoogle(String facebookOrGoogle) {
        DishqApplication.facebookOrGoogle = facebookOrGoogle;
    }

    public static String getFacebookOrGoogle() {
        return DishqApplication.facebookOrGoogle;
    }


    public static Boolean getOnBoardingDone() {
        return DishqApplication.ON_BOARDING_DONE;
    }

    public static void setOnBoardingDone(Boolean onBoardingDone) {
        DishqApplication.ON_BOARDING_DONE = onBoardingDone;
    }



    public static Boolean getIsNewUser() {
        return DishqApplication.IS_NEW_USER;
    }

    public static void setIsNewUser(Boolean IS_NEW_USER) {
        DishqApplication.IS_NEW_USER = IS_NEW_USER;
    }

    public static void setUniqueId(String uniqueId) {
        DishqApplication.uniqueId = uniqueId;
    }

    public static String getUniqueID() {
        return DishqApplication.uniqueId;
    }

    public static int getUserID() {
        return DishqApplication.userID;
    }

    public static void setUserID(int userID) {
        DishqApplication.userID = userID;
    }
    Application.ActivityLifecycleCallbacks activityCallbacks = new ActivityLifecycleCallbacks() {
        @Override
        public void onActivityCreated(Activity activity, Bundle bundle) {

        }

        @Override
        public void onActivityStarted(Activity activity) {

        }

        @Override
        public void onActivityResumed(Activity activity) {
            stopActivityTransitionTimer();
        }

        @Override
        public void onActivityPaused(Activity activity) {
            startActivityTransitionTimer();
        }

        @Override
        public void onActivityStopped(Activity activity) {
            RestApi restApi = Config.createService(RestApi.class);
            Call<ResponseBody> request = restApi.appToBackground(DishqApplication.getUniqueID(), DishqApplication.getUserID());
            request.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    Log.d("DishqApplication", "Success");
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.d("DishqApplication", "Failure");
                }
            });
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {

            RestApi restApi = Config.createService(RestApi.class);
            Call<ResponseBody> request = restApi.appClose(DishqApplication.getUniqueID(), DishqApplication.getUserID());
            request.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    Log.d("DishqApplication", "Success");
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.d("DishqApplication", "Failure");
                }
            });
        }
    };

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
