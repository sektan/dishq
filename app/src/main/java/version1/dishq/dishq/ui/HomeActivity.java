package version1.dishq.dishq.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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
import com.mixpanel.android.mpmetrics.MixpanelAPI;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import version1.dishq.dishq.BaseActivity;
import version1.dishq.dishq.R;
import version1.dishq.dishq.custom.FontsOverride;
import version1.dishq.dishq.custom.OnSwipeTouchListener;
import version1.dishq.dishq.fragments.dialogfragment.filters.FiltersDialogFragment;
import version1.dishq.dishq.fragments.homeScreenFragment.HomeScreenFragment;
import version1.dishq.dishq.fragments.moodFoodFiltersFragments.MoodFoodDialogFragment;
import version1.dishq.dishq.server.Config;
import version1.dishq.dishq.server.Response.HomeDishesResponse;
import version1.dishq.dishq.server.RestApi;
import version1.dishq.dishq.util.Constants;
import version1.dishq.dishq.util.DishqApplication;
import version1.dishq.dishq.util.Util;

/**
 * Created by dishq on 14-12-2016.
 * Package name version1.dishq.dishq.
 */

public class HomeActivity extends BaseActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, NavigationView.OnNavigationItemSelectedListener {

    protected static final int REQUEST_CHECK_SETTINGS = 1000;
    private static final String TAG = "HomeActivity";
    private static final long INTERVAL = 1000 * 10;
    private static final long FASTEST_INTERVAL = 1000 * 5;
    private static final long THIRTY_MINUTES = 30 * 60 * 1000;
    @SuppressLint("StaticFieldLeak")
    public static ViewPager viewPager;
    private static String lat = "0.0";
    private static String lang = "0.0";
    final int MY_PERMISSIONS_REQUEST_GPS_ACCESS = 0;
    LocationRequest mLocationRequest;
    private TextView greetingHeader, greetingContext, navUserName;
    private Button greetingButton;
    private RelativeLayout rlGreeting;
    private RelativeLayout homeRlNoViews;
    private GoogleApiClient googleApiClient;
    private Location mLastLocation;
    private ProgressBar progressBar;
    private RelativeLayout rlNoResults, rlHamMood;
    private Button hamButton, moodButton, feelingLucky;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private MixpanelAPI mixpanel = null;
    private Button moodFilterText;
    private FrameLayout goingToNextCard;
    private int currentPageOfViewPager = -1;
    private Boolean wasEmptyScreen = false;
    private FrameLayout progressBg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //MixPanel Instantiation
        mixpanel = MixpanelAPI.getInstance(this, getResources().getString(R.string.mixpanel_token));
        FontsOverride.setDefaultFont(HomeActivity.this, "MONOSPACE", "opensanssemibold.ttf");
        final int playServicesStatus = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);
        if (playServicesStatus != ConnectionResult.SUCCESS) {
            //If google play services in not available show an error dialog and return
            final Dialog errorDialog = GoogleApiAvailability.getInstance().getErrorDialog(this, playServicesStatus, 0, null);
            errorDialog.show();
            return;
        }
        setContentView(R.layout.activity_home);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);
        progressBg = (FrameLayout) findViewById(R.id.progress_bg_overlay_home);
        progressBg.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (DishqApplication.getAppWentToBg() != 0) {
            long currentTime = System.currentTimeMillis();
            long difference = currentTime - DishqApplication.getAppWentToBg();
            DishqApplication.getPrefs().edit().putLong(Constants.APP_WENT_TO_BACKGROUND, 0).apply();
            DishqApplication.setAppWentToBg(0);
            if (difference > THIRTY_MINUTES) {
                int currentPage = 0;
                Util.setCurrentPage(currentPage);
                checkInternetConnection();
            } else {
                Log.d(TAG, "Checking where to go");
                checkWhereToGo();
            }
        } else {
            Log.d(TAG, "Checking where to go");
            checkWhereToGo();
        }
    }

    protected void checkWhereToGo() {
        if (Util.isHomeRefreshRequired()) {
            Util.setHomeRefreshRequired(false);
            Util.setHomeLastPage(-1);
            int currentPage = 0;
            Util.setCurrentPage(currentPage);
            Log.d(TAG, "Checking for internet");
            checkInternetConnection();
        } else {
            if (viewPager != null) {
                currentPageOfViewPager = viewPager.getCurrentItem();
                Util.setCurrentPage(currentPageOfViewPager);
                if(wasEmptyScreen) {
                    setNoResultTags();
                }else {
                    if(currentPageOfViewPager > -1) {
                        currentPageOfViewPager = -1;
                        setViews();
                    }else {
                        setNoResultTags();
                    }
                }
            } else{
                setNoResultTags();
            }

        }
    }

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
            Log.d(TAG, "checking for GPS");
            if (Util.getLatitude().equals("") && Util.getLongitude().equals("")) {
                Util.setHomeRefreshRequired(false);
                checkGPS();
            } else {
                fetchHomeDishResults();
            }
        }
    }

    protected void setTags() {
        wasEmptyScreen = false;
        viewPager = (ViewPager) findViewById(R.id.homeViewPager);
        viewPager.setVisibility(View.VISIBLE);
        rlGreeting = (RelativeLayout) findViewById(R.id.rl_greeting);
        homeRlNoViews = (RelativeLayout) findViewById(R.id.home_rl_no_views);
        homeRlNoViews.setVisibility(View.GONE);
        greetingHeader = (TextView) findViewById(R.id.greeting_heading);
        greetingContext = (TextView) findViewById(R.id.greeting_context);
        greetingButton = (Button) findViewById(R.id.greeting_button);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        goingToNextCard = (FrameLayout) findViewById(R.id.home_frame_new_card);

        setFonts();
    }

    protected void setNoResultTags() {
        wasEmptyScreen = true;
        viewPager = (ViewPager) findViewById(R.id.homeViewPager);
        viewPager.setVisibility(View.GONE);
        rlGreeting = (RelativeLayout) findViewById(R.id.rl_greeting);
        rlGreeting.setVisibility(View.GONE);
        homeRlNoViews = (RelativeLayout) findViewById(R.id.home_rl_no_views);
        homeRlNoViews.setVisibility(View.VISIBLE);
        moodFilterText = (Button) findViewById(R.id.mood_filter);
        moodFilterText.setTypeface(Util.opensansregular);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        hamButton = (Button) findViewById(R.id.home_hamburger);
        moodButton = (Button) findViewById(R.id.home_mood);
        TextView oops = (TextView) findViewById(R.id.home_oops);
        oops.setTypeface(Util.opensanssemibold);
        TextView noResults = (TextView) findViewById(R.id.home_no_results_text);
        noResults.setTypeface(Util.opensansregular);
        TextView homeOr = (TextView) findViewById(R.id.home_or);
        homeOr.setTypeface(Util.opensanslight);
        feelingLucky = (Button) findViewById(R.id.feeling_lucky);
        feelingLucky.setTypeface(Util.opensanslight);
        showTopRlHamMood();
    }

    protected void setFonts() {
        greetingHeader.setTypeface(Util.opensanslight);
        greetingContext.setTypeface(Util.opensanslight);
        greetingButton.setTypeface(Util.opensanssemibold);
    }

    public void showTopRlHamMood() {

        hamButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    final JSONObject properties = new JSONObject();
                    properties.put("Burger Menu", "homescreen");
                    mixpanel.track("Burger Menu", properties);
                } catch (final JSONException e) {
                    throw new RuntimeException("Could not encode hour of the day in JSON");
                }
                ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(HomeActivity.this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
                drawer.setDrawerListener(toggle);
                toggle.syncState();
                navigationView = (NavigationView) findViewById(R.id.nav_view);
                navUserName = (TextView) findViewById(R.id.nav_user_name);
                if (navUserName != null) {
                    navUserName.setTypeface(Util.opensanslight);
                    navUserName.setText(DishqApplication.getUserName());
                }
                navigationView.setNavigationItemSelectedListener(HomeActivity.this);
                drawer.openDrawer(GravityCompat.START);
            }
        });

        Boolean isNotEmpty = false;
        String moodText;
        if (!Util.getMoodName().equals("")) {
            isNotEmpty = true;
            moodFilterText.setVisibility(View.VISIBLE);
        } else if (!Util.getFilterName().equals("")) {
            isNotEmpty = true;
            moodFilterText.setVisibility(View.VISIBLE);
        }

        if (isNotEmpty) {
            if (Util.getFilterName().equals("")) {
                moodText = Util.getMoodName();
            } else if (Util.getMoodName().equals("")) {
                moodText = Util.getFilterName();
            } else {
                moodText = Util.getMoodName() + " , " + Util.getFilterName();
            }
            moodFilterText.setText(moodText);
        }

        moodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    final JSONObject properties = new JSONObject();
                    properties.put("Mood filters on home screen", "homescreen");
                    mixpanel.track("Mood filters on home screen", properties);
                } catch (final JSONException e) {
                    throw new RuntimeException("Could not encode hour of the day in JSON");
                }
                FragmentManager fragmentManager = getSupportFragmentManager();
                //FiltersDialogFragment dialogFragment = FiltersDialogFragment.getInstance();
                MoodFoodDialogFragment dialogFragment = MoodFoodDialogFragment.getInstance();
                dialogFragment.show(fragmentManager, "filters_dialog_fragment");
            }
        });

        feelingLucky.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Util.setHomeRefreshRequired(true);
                Util.setCurrentPage(0);
                Util.setMoodFilterId(-1);
                Util.setMoodName("");
                Util.setFilterName("");
                Util.setFilterClassName("");
                Util.setFilterEntityId(-1);
                Intent intent = new Intent(HomeActivity.this, HomeActivity.class);

                finish();
                startActivity(intent);
            }
        });
    }

    public void greetingsShownView(HomeDishesResponse.HomeData body) {
        setViews();

        rlGreeting.setVisibility(View.VISIBLE);

        if (body != null) {
            greetingHeader.setText(body.greetingsInfo.getGreetingMessage());
            greetingContext.setText(body.greetingsInfo.getContextMessage());
            greetingButton.setOnClickListener(onClickListener);
            rlGreeting.setOnClickListener(onClickListener);
        }
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            rlGreeting.setVisibility(View.GONE);
        }
    };

    public void fetchHomeDishResults() {
        Util.setShowFeedbackQues(true);
        String latitude = Util.getLatitude();
        String longitude = Util.getLongitude();
        int moodId = Util.getMoodFilterId();
        String filterClassName = Util.getFilterClassName();
        int filterEntityId = Util.getFilterEntityId();
        RestApi restApi = Config.createService(RestApi.class);
        Call<HomeDishesResponse> call = restApi.fetchPersonalDishes(DishqApplication.getAccessToken(), DishqApplication.getUniqueID(),
                latitude, longitude, moodId, filterClassName, filterEntityId, Util.getPageNumber());
        call.enqueue(new Callback<HomeDishesResponse>() {
            @Override
            public void onResponse(Call<HomeDishesResponse> call, Response<HomeDishesResponse> response) {
                Log.d(TAG, "Success");
                try {
                    if (response.isSuccessful()) {
                        HomeDishesResponse.HomeData body = response.body().homeData;
                        if (body != null) {
                            Log.d(TAG, "Body is not null");
                            if (body.dishDataInfos.size() != 0) {

                                Util.setDefaultTab(body.getDefaultTab());
                                Util.setFeedbackQuestion(body.getHomeFeedbackQues());
                                Util.dishDataModals.clear();
                                Util.dishSmallPic.clear();
                                for (int i = 0; i < body.dishDataInfos.size(); i++) {

                                    Util.dishDataModals = body.dishDataInfos;
                                    Util.dishSmallPic.add(body.dishDataInfos.get(i).getDishPhoto().get(0));
                                }

                                Util.setShowBanner(body.getAreaOutOfCoverage());
                                if(Util.getShowBanner()){
                                    Util.setBannerText(body.getOutOfCoverageText());
                                }else {
                                    Util.setBannerText("");
                                }
                                Boolean showGreeting = body.getShowGreeting();
                                if (showGreeting) {
                                    progressBar.setVisibility(View.GONE);
                                    progressBg.setVisibility(View.GONE);
                                    greetingsShownView(body);
                                } else {
                                    setViews();
                                }
                                if (progressBar != null) {
                                    progressBar.setVisibility(View.GONE);
                                    progressBg.setVisibility(View.GONE);
                                }
                            } else {
                                if (progressBar != null) {
                                    progressBar.setVisibility(View.GONE);
                                    progressBg.setVisibility(View.GONE);
                                }
                                Util.dishDataModals.clear();
                                Util.dishSmallPic.clear();
                                setNoResultTags();
                            }
                        } else {
                            if (progressBar != null) {
                                progressBar.setVisibility(View.GONE);
                                progressBg.setVisibility(View.GONE);
                            }
                            Util.dishDataModals.clear();
                            Util.dishSmallPic.clear();
                            setNoResultTags();
                        }

                    } else {
                        if (progressBar != null) {
                            progressBar.setVisibility(View.GONE);
                            progressBg.setVisibility(View.GONE);
                        }
                        Util.dishDataModals.clear();
                        Util.dishSmallPic.clear();
                        setNoResultTags();
                        String error = response.errorBody().string();
                        Log.d(TAG, error);
                    }
                } catch (IOException e) {
                    if (progressBar != null) {
                        progressBar.setVisibility(View.GONE);
                        progressBg.setVisibility(View.GONE);

                    }
                    Util.dishDataModals.clear();
                    Util.dishSmallPic.clear();
                    setNoResultTags();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<HomeDishesResponse> call, Throwable t) {
                Log.d(TAG, "Failure");
                if (progressBar != null) {
                    progressBar.setVisibility(View.GONE);
                    progressBg.setVisibility(View.GONE);
                }
                Util.dishDataModals.clear();
                Util.dishSmallPic.clear();
                setNoResultTags();
            }
        });
    }

    private void setViews() {
        setTags();
        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
            progressBg.setVisibility(View.GONE);
        }
        viewPager.setAdapter(new SectionsPagerAdapter(getSupportFragmentManager()));
        if (Util.getCurrentPage() != 200) {
            viewPager.setCurrentItem(Util.getCurrentPage());
        }

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.d(TAG, "onPageScrolled is called : " + position);

            }

            @Override
            public void onPageSelected(int position) {
                Log.d(TAG, "onPageSelected is called :" + position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                Log.d(TAG, "onPageScrollStateChanged is called" + viewPager.getCurrentItem() + " state : " + state);
                if (state == 0) {
                    int currentPage = viewPager.getCurrentItem();
                    Util.setCurrentPage(currentPage);
                    if ((currentPage == Util.getHomeLastPage()) && (viewPager.getCurrentItem() == viewPager.getAdapter().getCount() - 1)) {
                        Log.d(TAG, "Going into the swipeLeft");
                        Util.setCurrentPage(viewPager.getCurrentItem());
                        Log.d(TAG, "Setting the current Page");
                        Intent intent = new Intent(HomeActivity.this, FeedbackActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    }
                }
                if (state == 1) {
                    Util.setHomeLastPage(viewPager.getCurrentItem());
                }
            }
        });
    }

    public void checkGPS() {
        createLocationRequest();
        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
            progressBg.setVisibility(View.GONE);
        }
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) { // first check
            getTheLocale();

        } else if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
            Log.d(TAG, "self permission invoked");
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
                                status.startResolutionForResult(HomeActivity.this, REQUEST_CHECK_SETTINGS);
                            } catch (IntentSender.SendIntentException e) {
                                // Ignore the error.
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            // Location settings are not satisfied. However, we have
                            // no way to fix the
                            // settings so we won't show the dialog.
                            alertNoForward(HomeActivity.this);
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
                        alertNoForward(HomeActivity.this);
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
                Log.d(TAG, "fetching home results");
                fetchHomeDishResults();

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
        if (ActivityCompat.shouldShowRequestPermissionRationale(HomeActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            Log.e("accept", "accept");
            Toast.makeText(this, "Enable Location Permission to access this feature", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", this.getPackageName(), null);
            intent.setData(uri);
            startActivity(intent);
            getTheLocale();
        } else {
            //request the permission
            Log.e("accept", "not accept");
            ActivityCompat.requestPermissions(HomeActivity.this,
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
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(HomeActivity.this);
        builder.setTitle(title);
        builder.setMessage(message).setCancelable(false)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }


                })
                .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (ContextCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.GET_ACCOUNTS) == PackageManager.PERMISSION_DENIED) {
                            ActivityCompat.requestPermissions(HomeActivity.this,
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
        if (!(HomeActivity.this).isFinishing()) {
            AlertDialog dialog = new AlertDialog.Builder(activity)
                    .setMessage("You can't continue without GPS")
                    .setCancelable(false)
                    .setNegativeButton("Got it", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Util.setHomeRefreshRequired(false);
                            Intent backButtonIntent = new Intent(HomeActivity.this, HomeActivity.class);
                            backButtonIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            finish();
                            startActivity(backButtonIntent);
                        }
                    })
                    .create();
            dialog.show();
        }
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_fav) {
            try {
                final JSONObject properties = new JSONObject();
                properties.put("Favourites list nav bar", "navbar");
                mixpanel.track("Favourites list nav bar", properties);
            } catch (final JSONException e) {
                throw new RuntimeException("Could not encode hour of the day in JSON");
            }
            Intent intent = new Intent(HomeActivity.this, FavouritesActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else if (id == R.id.nav_menufinder) {
            try {
                final JSONObject properties = new JSONObject();
                properties.put("Menu Finder in nav bar", "navbar");
                mixpanel.track("Menu Finder in nav bar", properties);
            } catch (final JSONException e) {
                throw new RuntimeException("Could not encode hour of the day in JSON");
            }
            Intent intent = new Intent(HomeActivity.this, MenuFinder.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else if (id == R.id.nav_settings) {
            try {
                final JSONObject properties = new JSONObject();
                properties.put("Settings in nav bar", "navbar");
                mixpanel.track("Settings in nav bar", properties);
            } catch (final JSONException e) {
                throw new RuntimeException("Could not encode hour of the day in JSON");
            }
            Intent intent = new Intent(HomeActivity.this, SettingsActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else if (id == R.id.nav_rate) {
            TextView title = new TextView(HomeActivity.this);
            title.setText("Like the dishq app?");
            title.setGravity(Gravity.START);
            title.setTextSize(22);
            title.setBackgroundColor(Color.WHITE);
            title.setTextColor(Color.parseColor("#000000"));
            title.setTypeface(Util.opensanslight);
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(HomeActivity.this);
            builder.setTitle("Like the dishq app?");
            builder.setMessage("").setCancelable(true)
                    .setPositiveButton("Love it", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + HomeActivity.this.getPackageName())));
                        }
                    });
            builder.setNegativeButton("Could Be Better", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Sendemail("App Feedback", "help@dishq.in");
                }
            });
            android.app.AlertDialog alert = builder.create();
            alert.show();
            TextView textView = (TextView) alert.findViewById(android.R.id.message);
            textView.setTextSize(20);
            textView.setTextColor(Color.parseColor("#90000000"));
            textView.setTypeface(Util.opensanssemibold);

        } else if (id == R.id.nav_share) {
            try {
                final JSONObject properties = new JSONObject();
                properties.put("Share in nav bar", "navbar");
                mixpanel.track("Share in nav bar", properties);
            } catch (final JSONException e) {
                throw new RuntimeException("Could not encode hour of the day in JSON");
            }
            Intent i = new Intent(android.content.Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(android.content.Intent.EXTRA_SUBJECT, "Check this out");
            i.putExtra(android.content.Intent.EXTRA_TEXT, "Love this foodie app, dishq, it gives personalised recommendations!  http://bit.ly/2bLhPig");
            startActivity(Intent.createChooser(i, "Share via"));
        } else if (id == R.id.nav_about) {
            Intent intent = new Intent(HomeActivity.this, AboutUsActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void Sendemail(String Subject, String to) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:" + to));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, Subject);
        try {
            startActivity(Intent.createChooser(emailIntent, "Send email using..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(HomeActivity.this, "No email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }

    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            HomeScreenFragment fragment = new HomeScreenFragment();
            fragment.setArguments(Util.dishDataModals.get(position).toBundle());
            // getItem is called to instantiate the fragment for the given page.
            // Return a HomeScreenFragment.
            return fragment;
        }

        @Override
        public int getCount() {
            // Show total pages.
            return Util.dishDataModals.size();
        }

    }

    @Override
    protected void onDestroy() {
        mixpanel.flush();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //finish();
        System.exit(0);
    }
}
