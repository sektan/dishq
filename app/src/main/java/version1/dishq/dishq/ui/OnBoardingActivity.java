package version1.dishq.dishq.ui;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import version1.dishq.dishq.BaseActivity;
import version1.dishq.dishq.R;
import version1.dishq.dishq.adapters.CustomViewPager;
import version1.dishq.dishq.fragments.tastePrefFragments.TastePrefFragment1;
import version1.dishq.dishq.fragments.tastePrefFragments.TastePrefFragment2;
import version1.dishq.dishq.fragments.tastePrefFragments.TastePrefFragment3;
import version1.dishq.dishq.fragments.tastePrefFragments.TastePrefFragment4;
import version1.dishq.dishq.modals.AllergyModal;
import version1.dishq.dishq.modals.FavCuisinesModal;
import version1.dishq.dishq.modals.FoodChoicesModal;
import version1.dishq.dishq.modals.HomeCuisinesModal;
import version1.dishq.dishq.server.Config;
import version1.dishq.dishq.server.Request.UserPrefRequest;
import version1.dishq.dishq.server.Response.TastePrefData;
import version1.dishq.dishq.server.RestApi;
import version1.dishq.dishq.util.Constants;
import version1.dishq.dishq.util.DishqApplication;
import version1.dishq.dishq.util.Util;

/**
 * Created by dishq on 15-12-2016.
 * Package name version1.dishq.dishq.
 */

public class OnBoardingActivity extends BaseActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private String TAG = "OnBoardingActivity";
    public static CustomViewPager pager;
    private Button doneButton;
    private GoogleApiClient googleApiClient;
    LocationRequest mLocationRequest;
    private static final long INTERVAL = 1000 * 10;
    private static final long FASTEST_INTERVAL = 1000 * 5;
    private Location mLastLocation;
    protected static final int REQUEST_CHECK_SETTINGS = 1000;
    final int MY_PERMISSIONS_REQUEST_GPS_ACCESS = 0;
    private static String lat = "0.0";
    private static String lang = "0.0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final int playServicesStatus = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);
        if (playServicesStatus != ConnectionResult.SUCCESS) {
            //If google play services in not available show an error dialog and return
            final Dialog errorDialog = GoogleApiAvailability.getInstance().getErrorDialog(this, playServicesStatus, 0, null);
            errorDialog.show();
            return;
        }
        setContentView(R.layout.activity_onboarding);
        setTags();
        fetchTastePref();
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideSoftKeyboard();
    }

    @Override
    protected void onPause() {
        super.onPause();
        hideSoftKeyboard();
    }

    protected void setTags() {
        doneButton = (Button) findViewById(R.id.done);
        pager = (CustomViewPager) findViewById(R.id.customViewPager);
        pager.setScrollDurationFactor(5);
        pager.setPagingEnabled(CustomViewPager.SwipeDirection.none);
        PageListener pageListener = new PageListener();
        pager.setOnPageChangeListener(pageListener);
    }

    public void checkGPS() {
        createLocationRequest();
        if (ContextCompat.checkSelfPermission(OnBoardingActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) { // first check
            getTheLocale();

        } else if (ContextCompat.checkSelfPermission(OnBoardingActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
            selfPermission();
        }
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void getTheLocale() {

        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API).addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this).build();
            googleApiClient.connect();

            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(30 * 1000);
            locationRequest.setFastestInterval(5 * 1000);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest);

            // **************************
            builder.setAlwaysShow(true); // this is the key ingredient
            // **************************

            PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi
                    .checkLocationSettings(googleApiClient, builder.build());

            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                @Override
                public void onResult(@NonNull LocationSettingsResult result) {
                    final Status status = result.getStatus();
                    switch (status.getStatusCode()) {

                        case LocationSettingsStatusCodes.SUCCESS:
                            // All location settings are satisfied. The client can
                            // initialize location
                            // requests here.
                            getLocation();
                            Log.d(TAG, "VALUES--1");
                            break;
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            // Location settings are not satisfied. But could be
                            // fixed by showing the user
                            // a dialog.
                            Log.d(TAG, "VALUES--2");
                            try {
                                // Show the dialog by calling
                                // startResolutionForResult(),
                                // and check the result in onActivityResult().
                                status.startResolutionForResult(OnBoardingActivity.this, REQUEST_CHECK_SETTINGS);
                            } catch (IntentSender.SendIntentException e) {
                                // Ignore the error.
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            // Location settings are not satisfied. However, we have
                            // no way to fix the
                            // settings so we won't show the dialog.
                            alertNoForward(OnBoardingActivity.this);
                            Log.d(TAG, "VALUES--3");
                            break;

                        case LocationSettingsStatusCodes.CANCELED:
                            // Location settings are not satisfied. However, we have
                            // no way to fix the
                            // settings so we won't show the dialog.

                            Log.d(TAG, "VALUES--CC");
                            break;
                    }
                }
            });
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        startLocationUpdates();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                googleApiClient);
        if (mLastLocation != null) {
            Log.d(TAG, "VALUES--6" + mLastLocation.getLatitude() + mLastLocation.getLongitude());
            lat = "" + mLastLocation.getLatitude();
            lang = "" + mLastLocation.getLongitude();
            Util.setLongitude(lang);
            Util.setLatitude(lat);

        } else {
            Log.d(TAG, "VALUES--6---");
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, mLocationRequest, this);

        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        getTheLocale();
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1000:
                switch (resultCode) {
                    case Activity.RESULT_OK: {
                        Log.d(TAG, "VALUES--OK");
                        // All required changes were successfully made
                        getLocation();
                        break;
                    }
                    case Activity.RESULT_CANCELED: {
                        alertNoForward(OnBoardingActivity.this);
                        // The user was asked to change settings, but chose not to
                        break;
                    }
                    default: {
                        break;
                    }
                }
                break;
        }
    }

    public void getLocation() {
        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            mLastLocation = LocationServices.FusedLocationApi
                    .getLastLocation(googleApiClient);

            if (mLastLocation != null) {

                lat = "" + mLastLocation.getLatitude();
                lang = "" + mLastLocation.getLongitude();
                Util.setLongitude(lang);
                Util.setLatitude(lat);
                Log.d("LOCATION", "LOCATION" + mLastLocation.getLatitude());
                Intent startHomeActivity = new Intent(OnBoardingActivity.this, HomeActivity.class);
                startHomeActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                finish();
                startActivity(startHomeActivity);

            } else {
                startLocationUpdates();
                getLocation();
                Log.d("LOCATION", "LOCATIONrrrr");
                Log.i("", "Not able to retrieve location of device right now");
            }
        } catch (Exception e) {
            Log.d("LOCATION", "LOCATIONeee" + e.getMessage());
        }

    }

    public void selfPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(OnBoardingActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            Log.e("accept", "accept");
            Toast.makeText(this, "Enable Location Permission to access this feature", Toast.LENGTH_SHORT).show(); // Something like this

            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", this.getPackageName(), null);
            intent.setData(uri);
            startActivity(intent);
            getTheLocale();
        } else {
            //request the permission
            Log.e("accept", "not accept");
            ActivityCompat.requestPermissions(OnBoardingActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_GPS_ACCESS);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_GPS_ACCESS: {

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLocation();
                } else {
                    showAlert("", "That permission is needed in order to update wait time. Tap Retry.");
                }
            }
        }
    }

    public void showAlert(String title, String message) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(OnBoardingActivity.this);
        builder.setTitle(title);
        builder.setMessage(message).setCancelable(false)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }


                })
                .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (ContextCompat.checkSelfPermission(OnBoardingActivity.this, Manifest.permission.GET_ACCOUNTS) == PackageManager.PERMISSION_DENIED) {
                            ActivityCompat.requestPermissions(OnBoardingActivity.this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    MY_PERMISSIONS_REQUEST_GPS_ACCESS);
                        }
                    }
                });


        android.app.AlertDialog alert = builder.create();
        alert.show();
        TextView message1 = (TextView) alert.findViewById(android.R.id.message);
        //assert message != null;
        message1.setLineSpacing(0, 1.5f);

    }

    public void alertNoForward(final Activity activity) {
        if (!(OnBoardingActivity.this).isFinishing()) {
            AlertDialog dialog = new AlertDialog.Builder(activity)
                    .setMessage("Can't update without GPS")
                    .setCancelable(false)
                    .setNegativeButton("Got it", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent backButtonIntent = new Intent(OnBoardingActivity.this, OnBoardingActivity.class);
                            backButtonIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            finish();
                            startActivity(backButtonIntent);
                        }
                    })
                    .create();
            dialog.show();
        }
    }

    public class PageListener extends ViewPager.SimpleOnPageChangeListener {
        public void onPageSelected(final int position) {
            Log.d("page", position + "");
            if (position == 0) {
                doneButton.setVisibility(View.GONE);
                if(Util.getFoodChoiceSelected()!=0) {
                    pager.setPagingEnabled(CustomViewPager.SwipeDirection.all);
                    OnBoardingActivity.pager.setCurrentItem(1);
                }else {
                    pager.setPagingEnabled(CustomViewPager.SwipeDirection.top);
                    OnBoardingActivity.pager.setCurrentItem(0);
                }

            }else if (position == 1) {
                doneButton.setVisibility(View.GONE);
                if (Util.homeCuisineSelected) {
                    pager.setPagingEnabled(CustomViewPager.SwipeDirection.all);
                    OnBoardingActivity.pager.setCurrentItem(2);
                }else {
                    pager.setPagingEnabled(CustomViewPager.SwipeDirection.top);
                    OnBoardingActivity.pager.setCurrentItem(1);
                }

            }else if (position == 2) {
                doneButton.setVisibility(View.GONE);
                if(Util.getFavCuisinetotal() == 3) {
                    pager.setPagingEnabled(CustomViewPager.SwipeDirection.all);
                    OnBoardingActivity.pager.setCurrentItem(3);
                }else {
                    pager.setPagingEnabled(CustomViewPager.SwipeDirection.top);
                    OnBoardingActivity.pager.setCurrentItem(2);
                }
            }else if (position == 3) {
                //Done button should be made visible
                //Call for updating user preferences done here
                //HomeActivity should be called
                pager.setPagingEnabled(CustomViewPager.SwipeDirection.top);
                doneButton.setVisibility(View.VISIBLE);
                doneButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        sendUserPrefData();
                    }
                });
            }
        }
    }

    public void hideSoftKeyboard() {
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {

        MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int pos) {
            switch (pos) {
                case 0:
                    return TastePrefFragment1.newInstance("FirstFragment, Instance 1");
                case 1:
                    return TastePrefFragment2.newInstance("SecondFragment, Instance 2");
                case 2:
                    return TastePrefFragment3.newInstance("ThirdFragment, Instance 3");
                case 3:
                    return TastePrefFragment4.newInstance("FourthFragment, Instance 4");
                default:
                    return TastePrefFragment4.newInstance("FourthFragment, Default");
            }
        }

        @Override
        public int getCount() {
            return 4;
        }
    }

    public void fetchTastePref() {
        RestApi restApi = Config.createService(RestApi.class);
        int userId = DishqApplication.getUserID();
        Call<TastePrefData> request = restApi.fetchTastePref(DishqApplication.getUniqueID(), userId);
        request.enqueue(new Callback<TastePrefData>() {
            @Override
            public void onResponse(Call<TastePrefData> call, Response<TastePrefData> response) {
                Log.d(TAG, "Success");
                try {
                    if(response.isSuccessful()) {
                        TastePrefData.FoodPreferencesInfo body = response.body().foodPreferencesinfo;
                        if(body!=null) {
                            Boolean onBoardingCompleted = true;
                            DishqApplication.getPrefs().edit().putBoolean(Constants.ON_BOARDING_DONE, onBoardingCompleted).apply();
                            DishqApplication.setOnBoardingDone(onBoardingCompleted);
                            Log.d(TAG, "");
                            Util.allergyModals.clear();
                            Util.foodChoicesModals.clear();
                            Util.favCuisinesModals.clear();
                            Util.homeCuisinesModals.clear();
                            Log.d(TAG, "Length of Food Allergies: " + body.foodAllergiesInfos.size());
                            for (int i = 0; i < body.foodAllergiesInfos.size(); i++) {
                                Util.allergyModals.add(new AllergyModal(body.foodAllergiesInfos.get(i).getAllergyClassName(),
                                        body.foodAllergiesInfos.get(i).getAllergyCurrentlySelect(), body.foodAllergiesInfos.get(i).getAllergyEntityId(),
                                        body.foodAllergiesInfos.get(i).getAllergyName(), body.foodAllergiesInfos.get(i).getAllergyFoodChoice()));
                            Log.d(TAG, "Data has been filled: " + Util.allergyModals.size());
                            }
                            for (int j = 0; j < body.foodChoicesInfos.size(); j++) {
                                Util.foodChoicesModals.add(new FoodChoicesModal(body.foodChoicesInfos.get(j).getFoodChoiceCurrSel(),
                                        body.foodChoicesInfos.get(j).getFoodChoiceName(), body.foodChoicesInfos.get(j).getFoodChoiceValue()));
                                Log.d(TAG, "Data has been filled: " + Util.foodChoicesModals.size());
                            }
                            for (int k = 0; k < body.favCuisineInfos.size(); k++) {
                                Util.favCuisinesModals.add(new FavCuisinesModal(body.favCuisineInfos.get(k).getFavCuisClassName(),
                                        body.favCuisineInfos.get(k).getFavCuisCurrentSelect(), body.favCuisineInfos.get(k).getFavCuisEntityId(),
                                        body.favCuisineInfos.get(k).getFavCuisName()));
                                Log.d(TAG, "Data has been filled: " + Util.favCuisinesModals.size());
                            }
                            for (int l = 0; l < body.homeCuisineInfos.size(); l++) {
                                Util.homeCuisinesModals.add(new HomeCuisinesModal(body.homeCuisineInfos.get(l).getHomeCuisClassName(),
                                        body.homeCuisineInfos.get(l).getHomeCuisCurrentSelect(), body.homeCuisineInfos.get(l).getHomeCuisEntityId(),
                                        body.homeCuisineInfos.get(l).getHomeCuisName()));
                                Log.d(TAG, "Data has been filled: " + Util.homeCuisinesModals.size());
                            }
                            pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
                            OnBoardingActivity.this.runOnUiThread(new Runnable() {

                                @Override
                                public void run() {

                                }
                            });
                        }
                    }else {
                        String error = response.errorBody().string();
                        Log.d(TAG, error);
                    }
                }catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<TastePrefData> call, Throwable t) {
                Log.d(TAG, "Failure");
            }
        });
    }

    public void sendUserPrefData() {
        RestApi restApi = Config.createService(RestApi.class);
        String authorization = DishqApplication.getAccessToken();
        final UserPrefRequest userPrefRequest = new UserPrefRequest(Util.getFoodChoiceSelected(), Util.homeCuisineSelects,
                Util.favCuisineSelects, Util.dontEatSelects);
        Call<ResponseBody> request = restApi.sendUserPref(authorization, userPrefRequest);
        request.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d(TAG, "Success");
                checkGPS();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d(TAG, "Failure");
            }
        });
    }
    protected void stopLocationUpdates() {

        Log.d(TAG, "Remove Location");
        try {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
        } catch (Exception e) {
            Log.d(TAG, "Exception:" + e);
        }
    }

    protected void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, mLocationRequest, this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopLocationUpdates();
    }
}
