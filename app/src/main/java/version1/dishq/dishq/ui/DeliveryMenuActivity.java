package version1.dishq.dishq.ui;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
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
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import version1.dishq.dishq.BaseActivity;
import version1.dishq.dishq.R;
import version1.dishq.dishq.adapters.menuAdapters.DeliveryMenuMasonryAdapter;
import version1.dishq.dishq.server.Config;
import version1.dishq.dishq.server.Response.DeliveryMenuResponse;
import version1.dishq.dishq.server.RestApi;
import version1.dishq.dishq.util.DishqApplication;
import version1.dishq.dishq.util.Util;

/**
 * Created by dishq on 09-01-2017.
 * Package name version1.dishq.dishq.
 */

public class DeliveryMenuActivity extends BaseActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener{

    private static final String TAG = "DeliveryMenuActivity";
    private RecyclerView delMenuRecyclerView;
    private RecyclerView.LayoutManager recyclerViewLayoutManager;
    private ImageView backButton, dineoutButton;
    private TextView delMenuHeader;
    private TextView delMenuRestType, delMenuRp1, delMenuRp2, delMenuRp3,
            delMenuRp4, delMenuDrive;
    private TextView delMenuTags, delMenuRestAdd;
    private TextView orderFrom;
    private TextView swiggy, foodpanda, runnr, zomato;
    private RelativeLayout rlOrderNow;
    private Button orderNow;
    private ImageView dishImage1, dishImage2, dishImage3;
    private NestedScrollView nestedScrollView;
    private MixpanelAPI mixpanel = null;
    private ProgressBar progressBar;
    private FrameLayout progressBg;
    private TextView personalizedText;

    //Checking for gps and internet
    LocationRequest mLocationRequest;
    private boolean networkFailed;
    private GoogleApiClient googleApiClient;
    private Location mLastLocation;
    protected static final int REQUEST_CHECK_SETTINGS = 1000;
    private static final long INTERVAL = 1000 * 10;
    private static final long FASTEST_INTERVAL = 1000 * 5;
    private static String lat = "0.0";
    private static String lang = "0.0";
    final int MY_PERMISSIONS_REQUEST_GPS_ACCESS = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_menu);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);
        progressBg = (FrameLayout) findViewById(R.id.progress_bg_overlay_del);
        progressBg.setVisibility(View.VISIBLE);
        //MixPanel Instantiation
        mixpanel = MixpanelAPI.getInstance(this, getResources().getString(R.string.mixpanel_token));

        final int playServicesStatus = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);
        if (playServicesStatus != ConnectionResult.SUCCESS) {
            //If google play services in not available show an error dialog and return
            final Dialog errorDialog = GoogleApiAvailability.getInstance().getErrorDialog(this, playServicesStatus, 0, null);
            errorDialog.show();
            return;
        }

        checkInternetConnection();

        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapse_toolbar);
        collapsingToolbar.setTitle("My Toolbar Tittle");

    }

    protected void setTags() {

        nestedScrollView = (NestedScrollView) findViewById(R.id.del_nested_scroll);
        personalizedText = (TextView) findViewById(R.id.personalized_menu_text);
        personalizedText.setTypeface(Util.opensansregular);
        rlOrderNow = (RelativeLayout) findViewById(R.id.rl_delmenu_ordernow);
        orderNow = (Button) findViewById(R.id.order_now);
        orderNow.setTypeface(Util.opensanssemibold);
        dishImage1 = (ImageView) findViewById(R.id.delmenu_dish_image1);
        dishImage1.setScaleType(ImageView.ScaleType.CENTER_CROP);
        dishImage2 = (ImageView) findViewById(R.id.delmenu_dish_image2);
        dishImage2.setScaleType(ImageView.ScaleType.CENTER_CROP);
        dishImage3 = (ImageView) findViewById(R.id.delmenu_dish_image3);
        dishImage3.setScaleType(ImageView.ScaleType.CENTER_CROP);
        delMenuRecyclerView = (RecyclerView) findViewById(R.id.del_menu_recyclerview);
        backButton = (ImageView) findViewById(R.id.del_menu_back_button);
        delMenuHeader = (TextView) findViewById(R.id.delmenu_toolbarTitle);
        delMenuHeader.setTypeface(Util.opensanssemibold);
        dineoutButton = (ImageView) findViewById(R.id.dineout_option);
        delMenuRestType = (TextView) findViewById(R.id.delmenu_rest_type_text);
        delMenuRestType.setTypeface(Util.opensansregular);
        delMenuRp1 = (TextView) findViewById(R.id.delmenu_rup_1);
        delMenuRp1.setTypeface(Util.opensanssemibold);
        delMenuRp2 = (TextView) findViewById(R.id.delmenu_rup_2);
        delMenuRp2.setTypeface(Util.opensanssemibold);
        delMenuRp3 = (TextView) findViewById(R.id.delmenu_rup_3);
        delMenuRp3.setTypeface(Util.opensanssemibold);
        delMenuRp4 = (TextView) findViewById(R.id.delmenu_rup_4);
        delMenuRp4.setTypeface(Util.opensanssemibold);
        delMenuDrive = (TextView) findViewById(R.id.delmenu_drive_time);
        delMenuDrive.setTypeface(Util.opensansregular);
        delMenuTags = (TextView) findViewById(R.id.delmenu_tags);
        delMenuTags.setTypeface(Util.opensansregular);
        delMenuRestAdd = (TextView) findViewById(R.id.delmenu_rest_addr);
        delMenuRestAdd.setTypeface(Util.opensansregular);
        orderFrom = (TextView) findViewById(R.id.delmenu_order_text);
        orderFrom.setTypeface(Util.opensansregular);
        swiggy = (TextView) findViewById(R.id.swiggy_text);
        swiggy.setTypeface(Util.opensansregular);
        foodpanda = (TextView) findViewById(R.id.foodpanda_text);
        foodpanda.setTypeface(Util.opensansregular);
        runnr = (TextView) findViewById(R.id.runnr_text);
        runnr.setTypeface(Util.opensansregular);
        zomato = (TextView) findViewById(R.id.zomato_text);
        zomato.setTypeface(Util.opensansregular);
        setFunctionality();
    }

    protected void setFunctionality() {

        ArrayList<DeliveryMenuResponse.DeliveryMenuDishData> delPhotos = Util.deliveryRestData.getDeliveryMenuDishDatas();
        DeliveryMenuResponse.DeliveryMenuDishData listOfPhotos1 = delPhotos.get(0);
        DeliveryMenuResponse.DeliveryMenuDishData listOfPhotos2 = delPhotos.get(1);
        DeliveryMenuResponse.DeliveryMenuDishData listOfPhotos3 = delPhotos.get(2);
        ArrayList<String> photos1 = listOfPhotos1.getDelivPhoto();
        ArrayList<String> photos2 = listOfPhotos2.getDelivPhoto();
        ArrayList<String> photos3 = listOfPhotos3.getDelivPhoto();

        Picasso.with(DishqApplication.getContext())
                .load(photos1.get(0))
                .fit()
                .centerCrop()
                .into(dishImage1);
        Picasso.with(DishqApplication.getContext())
                .load(photos2.get(0))
                .fit()
                .centerCrop()
                .into(dishImage2);
        Picasso.with(DishqApplication.getContext())
                .load(photos3.get(0))
                .fit()
                .centerCrop()
                .into(dishImage3);
        rlOrderNow.setVisibility(View.VISIBLE);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Util.setHomeRefreshRequired(false);
                finish();
            }
        });

        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                if (scrollY > oldScrollY) {
                    Log.i(TAG, "Scroll DOWN");
                    rlOrderNow.setVisibility(View.VISIBLE);
                    final int position = v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight();
                    orderNowButtonClick(position);
                }
                if (scrollY < oldScrollY) {
                    Log.i(TAG, "Scroll UP");
                    rlOrderNow.setVisibility(View.VISIBLE);
                    final int position = v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight();
                    orderNowButtonClick(position);
                }

                if (scrollY == 0) {
                    Log.i(TAG, "TOP SCROLL");
                    rlOrderNow.setVisibility(View.VISIBLE);

                    final int position = v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight();
                    orderNowButtonClick(position);
                }

                if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
                    Log.i(TAG, "BOTTOM SCROLL");

                    rlOrderNow.setVisibility(View.VISIBLE);
                    final int position = v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight();
                    orderNowButtonClick(position);
                }

            }
        });

        if (Util.deliveryRestData != null) {
            delMenuHeader.setText(Util.deliveryRestData.getDelMenuRestName());
            dineoutButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        final JSONObject properties = new JSONObject();
                        properties.put("Dineout menu flipbutton", "deliverymenu");
                        mixpanel.track("Dineout menu flipbutton", properties);
                    } catch (final JSONException e) {
                        throw new RuntimeException("Could not encode hour of the day in JSON");
                    }
                    Util.setDineRestId(Util.deliveryRestData.getDelMenuRestId());
                    Intent intent = new Intent(DeliveryMenuActivity.this, DineoutMenuActivity.class);
                    overridePendingTransition(R.anim.from_middle, R.anim.to_middle);
                    startActivity(intent);
                    finish();
                }
            });

            StringBuilder sb = new StringBuilder();

            if (Util.deliveryRestData.getDelMenuCusineText() != null) {
                for (String s : Util.deliveryRestData.getDelMenuCusineText()) {
                    if (sb.length() > 0) {
                        sb.append(',' + " ");
                    }
                    sb.append(s);

                }
            }
            String dishTypeText = sb.toString();
            delMenuRestType.setText(String.valueOf(dishTypeText));

            int dinePriceLvl = Util.deliveryRestData.getDelMenuPriceLvl();
            if (dinePriceLvl == 1) {
                delMenuRp1.setTextColor(ContextCompat.getColor(DishqApplication.getContext(), R.color.rupeeGreen));
            } else if (dinePriceLvl == 2) {
                delMenuRp1.setTextColor(ContextCompat.getColor(DishqApplication.getContext(), R.color.rupeeGreen));
                delMenuRp2.setTextColor(ContextCompat.getColor(DishqApplication.getContext(), R.color.rupeeGreen));
            } else if (dinePriceLvl == 3) {
                delMenuRp1.setTextColor(ContextCompat.getColor(DishqApplication.getContext(), R.color.rupeeGreen));
                delMenuRp2.setTextColor(ContextCompat.getColor(DishqApplication.getContext(), R.color.rupeeGreen));
                delMenuRp3.setTextColor(ContextCompat.getColor(DishqApplication.getContext(), R.color.rupeeGreen));
            } else if (dinePriceLvl == 4) {
                delMenuRp1.setTextColor(ContextCompat.getColor(DishqApplication.getContext(), R.color.rupeeGreen));
                delMenuRp2.setTextColor(ContextCompat.getColor(DishqApplication.getContext(), R.color.rupeeGreen));
                delMenuRp3.setTextColor(ContextCompat.getColor(DishqApplication.getContext(), R.color.rupeeGreen));
                delMenuRp4.setTextColor(ContextCompat.getColor(DishqApplication.getContext(), R.color.rupeeGreen));
            }
            delMenuDrive.setText(Util.deliveryRestData.getDelMenuDriveTime());

            String dishTags = "";
            if (Util.deliveryRestData.getDelMenuTags() != null) {
                for (String s : Util.deliveryRestData.getDelMenuTags()) {
                    dishTags += s + " ";
                }
            }
            delMenuTags.setText(String.valueOf(dishTags));

            String dineRestAddress = "";
            if (Util.deliveryRestData.getDelMenuRestAddr() != null) {
                for (String s : Util.deliveryRestData.getDelMenuRestAddr())
                    dineRestAddress += s;
                delMenuRestAdd.setText(dineRestAddress);
            }
        }

        if (Util.deliveryRestData.getDelMenuRunnrUrl() != null) {
            final String runnrUrl = Util.deliveryRestData.getDelMenuRunnrUrl();
            if (runnrUrl.equals("")) {
                runnr.setVisibility(View.GONE);
            } else {
                runnr.setVisibility(View.VISIBLE);
                runnr.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            final JSONObject properties = new JSONObject();
                            properties.put("Runnr order", "deliverymenu");
                            mixpanel.track("Runnr order", properties);
                        } catch (final JSONException e) {
                            throw new RuntimeException("Could not encode hour of the day in JSON");
                        }
                        Intent shareIntent = new Intent();
                        shareIntent.setAction(Intent.ACTION_VIEW);
                        shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        shareIntent.setData(Uri.parse(runnrUrl));
                        startActivity(shareIntent);
                    }
                });
            }
        } else {
            runnr.setVisibility(View.GONE);
        }

        if (Util.deliveryRestData.getDelMenuFoodPandaUrl() != null) {
            final String foodpandaUrl = Util.deliveryRestData.getDelMenuFoodPandaUrl();
            if (foodpandaUrl.equals("")) {
                foodpanda.setVisibility(View.GONE);
            } else {
                foodpanda.setVisibility(View.VISIBLE);
                foodpanda.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            final JSONObject properties = new JSONObject();
                            properties.put("Foodpanda order", "deliverymenu");
                            mixpanel.track("Foodpanda order", properties);
                        } catch (final JSONException e) {
                            throw new RuntimeException("Could not encode hour of the day in JSON");
                        }

                        Intent shareIntent = new Intent();
                        shareIntent.setAction(Intent.ACTION_VIEW);
                        shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        shareIntent.setData(Uri.parse(foodpandaUrl));
                        startActivity(shareIntent);
                    }
                });
            }
        } else {
            foodpanda.setVisibility(View.GONE);
        }

        if (Util.deliveryRestData.getDelMenuZomatoUrl() != null) {
            final String zomatoUrl = Util.deliveryRestData.getDelMenuZomatoUrl();
            Log.d(TAG, "The url is : " + zomatoUrl);
            if (zomatoUrl.equals("")) {
                zomato.setVisibility(View.GONE);
            } else {
                zomato.setVisibility(View.VISIBLE);

                zomato.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            final JSONObject properties = new JSONObject();
                            properties.put("Zomato order", "deliverymenu");
                            mixpanel.track("Zomato order", properties);
                        } catch (final JSONException e) {
                            throw new RuntimeException("Could not encode hour of the day in JSON");
                        }

                        Intent shareIntent = new Intent();
                        shareIntent.setAction(Intent.ACTION_VIEW);
                        shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        shareIntent.setData(Uri.parse(zomatoUrl));
                        startActivity(shareIntent);
                    }
                });
            }
        } else {
            zomato.setVisibility(View.GONE);
        }

        if (Util.deliveryRestData.getDelMenuSwiggyUrl() != null) {
            final String swiggyUrl = Util.deliveryRestData.getDelMenuSwiggyUrl();
            Log.d(TAG, "the url is : " + swiggyUrl);
            if (swiggyUrl.equals("")) {
                swiggy.setVisibility(View.GONE);
            } else {
                swiggy.setVisibility(View.VISIBLE);
                swiggy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            final JSONObject properties = new JSONObject();
                            properties.put("Swiggy order", "deliverymenu");
                            mixpanel.track("Swiggy order", properties);
                        } catch (final JSONException e) {
                            throw new RuntimeException("Could not encode hour of the day in JSON");
                        }


                        Intent shareIntent = new Intent();
                        shareIntent.setAction(Intent.ACTION_VIEW);
                        shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        shareIntent.setData(Uri.parse(swiggyUrl));
                        startActivity(shareIntent);
                    }
                });
            }
        } else {
            swiggy.setVisibility(View.GONE);
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
            Log.d(TAG, "Checking for GPS");
            //Check for gps
            checkGPS();

        } else {
            networkFailed = true;
        }
    }

    protected void orderNowButtonClick(final int position) {
        orderNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    final JSONObject properties = new JSONObject();
                    properties.put("Order now button in delivery menu", "deliverymenu");
                    mixpanel.track("Order now button in delivery menu", properties);
                } catch (final JSONException e) {
                    throw new RuntimeException("Could not encode hour of the day in JSON");
                }
                nestedScrollView.scrollTo(0, position);
            }
        });
    }

    private void fetchDeliveryMenuInfo() {
        String latitude = Util.getLatitude(), longitude = Util.getLongitude(), source = "deliverymenu";
        RestApi restApi = Config.createService(RestApi.class);
        Call<DeliveryMenuResponse> call = restApi.getDeliveryMenuOptions(DishqApplication.getAccessToken(),
                Util.getDelRestId(), DishqApplication.getUniqueID(), source, latitude, longitude,
                Util.getGenericDishIdTab());
        call.enqueue(new Callback<DeliveryMenuResponse>() {
            @Override
            public void onResponse(Call<DeliveryMenuResponse> call, Response<DeliveryMenuResponse> response) {
                Log.d(TAG, "Success");
                try {
                    if (response.isSuccessful()) {
                        Log.d(TAG, "response is successful");
                        DeliveryMenuResponse.DeliveryMenuInfo body = response.body().deliveryMenuInfo;
                        if (body != null) {
                            Log.d(TAG, "body is not null");
                            Util.deliveryMenuInfos.clear();
                            Util.deliveryRestData = body.deliveryRestData;
                            for (int i = 0; i < body.deliveryMenuDatas.size(); i++) {
                                Util.deliveryMenuInfos = body.deliveryMenuDatas;
                            }
                            setTags();
                            recyclerViewLayoutManager = new GridLayoutManager
                                    (DeliveryMenuActivity.this, 2);
                            Util.SpacesItemDecoration decoration = new Util.SpacesItemDecoration(13);
                            delMenuRecyclerView.setLayoutManager(recyclerViewLayoutManager);
                            delMenuRecyclerView.addItemDecoration(decoration);
                            delMenuRecyclerView.setNestedScrollingEnabled(false);
                            DeliveryMenuMasonryAdapter masonryAdapter = new DeliveryMenuMasonryAdapter(DeliveryMenuActivity.this);
                            progressBar.setVisibility(View.GONE);
                            progressBg.setVisibility(View.GONE);
                            delMenuRecyclerView.setAdapter(masonryAdapter);
                        }
                        progressBar.setVisibility(View.GONE);
                        progressBg.setVisibility(View.GONE);
                    } else {
                        progressBar.setVisibility(View.GONE);
                        progressBg.setVisibility(View.GONE);
                        String error = response.errorBody().string();
                        Log.d(TAG, error);
                    }
                } catch (IOException e) {
                    progressBar.setVisibility(View.GONE);
                    progressBg.setVisibility(View.GONE);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<DeliveryMenuResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                progressBg.setVisibility(View.GONE);
                Log.d(TAG, "Failure");
            }
        });

    }

    @Override
    protected void onDestroy() {
        mixpanel.flush();
        super.onDestroy();
    }

    public void checkGPS() {
        createLocationRequest();
        if (ContextCompat.checkSelfPermission(DeliveryMenuActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) { // first check
            getTheLocale();

        } else if (ContextCompat.checkSelfPermission(DeliveryMenuActivity.this,
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
                public void onResult(LocationSettingsResult result) {
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
                                status.startResolutionForResult(DeliveryMenuActivity.this, REQUEST_CHECK_SETTINGS);
                            } catch (IntentSender.SendIntentException e) {
                                // Ignore the error.
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            // Location settings are not satisfied. However, we have
                            // no way to fix the
                            // settings so we won't show the dialog.
                            alertNoForward(DeliveryMenuActivity.this);
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
                        alertNoForward(DeliveryMenuActivity.this);
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
                fetchDeliveryMenuInfo();

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
        if (ActivityCompat.shouldShowRequestPermissionRationale(DeliveryMenuActivity.this,
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
            ActivityCompat.requestPermissions(DeliveryMenuActivity.this,
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
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(DeliveryMenuActivity.this);
        builder.setTitle(title);
        builder.setMessage(message).setCancelable(false)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }


                })
                .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (ContextCompat.checkSelfPermission(DeliveryMenuActivity.this, Manifest.permission.GET_ACCOUNTS) == PackageManager.PERMISSION_DENIED) {
                            ActivityCompat.requestPermissions(DeliveryMenuActivity.this,
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
        if (!(DeliveryMenuActivity.this).isFinishing()) {
            AlertDialog dialog = new AlertDialog.Builder(activity)
                    .setMessage("Can't update without GPS")
                    .setCancelable(false)
                    .setNegativeButton("Got it", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent backButtonIntent = new Intent(DeliveryMenuActivity.this, DeliveryMenuActivity.class);
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
            Log.d(TAG, "Exception:" +e);
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
