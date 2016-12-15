package version1.dishq.dishq.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import java.io.IOException;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import version1.dishq.dishq.BaseActivity;
import version1.dishq.dishq.R;
import version1.dishq.dishq.server.Config;
import version1.dishq.dishq.server.Response.VersionCheckResponse;
import version1.dishq.dishq.server.RestApi;
import version1.dishq.dishq.util.Constants;
import version1.dishq.dishq.util.DishqApplication;
import version1.dishq.dishq.util.Util;

/**
 * Created by dishq on 13-12-2016.
 * Package name version1.dishq.dishq.
 */

public class SplashActivity extends BaseActivity {

    private String TAG = "SplashActivity";
    private boolean networkFailed;
    public String versionName;
    public int versionCode = 0;
    public String uniqueIdentifier;
    public int userId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //For collecting the Android_ID as a unique identifier for users
        uniqueIdentifier = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        DishqApplication.getPrefs().edit().putString(Constants.UNIQUE_IDENTIFIER, uniqueIdentifier).apply();
        DishqApplication.setUniqueId(uniqueIdentifier);
        Log.d(TAG, "Unique number: " + uniqueIdentifier);
        // The following helps in obtaining the current version number and version code
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            versionName = pInfo.versionName;
            versionCode = pInfo.versionCode;

            Log.e(TAG, pInfo.versionName + pInfo.versionCode);
        } catch (Exception e) {
            e.printStackTrace();
        }

        timer.start();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    //Timer set for the display time of the splashScreen
    Thread timer = new Thread() {
        public void run() {
            try {
                sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                if(networkFailed) {
                    Log.d(TAG, "Checking if there is an internet connection");
                    checkInternetConnection();
                }
                else {
                    checkVersion();
                }
            }
        }
    };

    //Method to check if the internet is connected or not
    private void checkInternetConnection() {
        SharedPreferences settings;
        final String PREFS_NAME = "MyPrefsFile";
        settings = getSharedPreferences(PREFS_NAME, 0);

        if (settings.getBoolean("android_M", true)) {
            //the app is being launched for first time, do something
            Log.d("Comments", " android_M");
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // only for gingerbread and newer versions
                checkNetwork();
            } else {
                checkNetwork();
            }
            settings.edit().putBoolean("android_M", false).apply();
        } else {
            checkNetwork();
        }
    }

    //Check for internet
    private void checkNetwork() {
        if (!Util.checkAndShowNetworkPopup(this)) {
            //Check for version
            Log.d(TAG, "Implementing a Version Check");
            checkVersion();

        } else {
            networkFailed = true;
        }
    }

    //Method to make an api call to check the version of the app
    private void checkVersion() {
        //Creating a service to make a restApi call
        RestApi restApi = Config.createService(RestApi.class);
        Call<VersionCheckResponse> request = restApi.checkVersion(versionCode, versionName, uniqueIdentifier, userId);
        request.enqueue(new Callback<VersionCheckResponse>() {
            @Override
            public void onResponse(Call<VersionCheckResponse> call, Response<VersionCheckResponse> response) {
                Log.d(TAG, "Success");
                try {
                    if(response.isSuccessful()) {
                        VersionCheckResponse.VersionCheckData body = response.body().versionCheckData;
                        if(body!=null) {
                            if(body.getShowUpdatePopup()) {
                                if(body.getDoForceUpdate()) {
                                    Log.d(TAG, "Update is required and is mandatory");
                                    showAlert("Update Dishq", "Update the app for best performance", true);
                                }else {
                                    Log.d(TAG, "Update is required but not mandatory");
                                    showAlert("Update Dishq", "Update the app for best performance", false);
                                }
                            }else {
                                //To check which activity to open after the splashScreen
                                checkWhereToGo();
                            }
                        }

                    } else {
                        String error = response.errorBody().string();
                        Log.d(TAG, "Error: "+error);
                    }
                }catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<VersionCheckResponse> call, Throwable t) {
                Log.d(TAG, "Failure");
                if(!Util.checkAndShowNetworkPopup(SplashActivity.this)) {
                    //To check the version of the app
                    checkVersion();
                }
            }
        });

    }

    //Method to find out which activity to open next
    private void checkWhereToGo() {
        if (!DishqApplication.getAccessToken().equals("null null")) {

            if(DishqApplication.getIsNewUser()) {
                //Intent to start OnBoarding
                Intent startHomeActivity = new Intent(SplashActivity.this, OnBoardingActivity.class);
                startHomeActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                finish();
                startActivity(startHomeActivity);
            }else{
                //Intent to start home page
                Intent startHomeActivity = new Intent(SplashActivity.this, HomeActivity.class);
                startHomeActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                finish();
                startActivity(startHomeActivity);
            }

        } else {
            //Intent to start the SignIn Activity after the splash screen
            Intent i = new Intent(SplashActivity.this, SignInActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            finish();
            startActivity(i);
        }
    }

    //Method to show an alert when an update of the app is required
    public void showAlert(String title, String message, boolean force) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message).setCancelable(false)
                .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())));
                    }
                });
        if(!force){
            //Update isn't mandatory
            builder.setNegativeButton("Not now", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    checkWhereToGo();
                }
            });
        }

        android.app.AlertDialog alert = builder.create();
        alert.show();

    }

}
