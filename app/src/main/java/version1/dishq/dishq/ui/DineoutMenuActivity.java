package version1.dishq.dishq.ui;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
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
import com.squareup.picasso.Target;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.StringTokenizer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import version1.dishq.dishq.BaseActivity;
import version1.dishq.dishq.R;
import version1.dishq.dishq.adapters.menuAdapters.DineoutMenuMasonryAdapter;
import version1.dishq.dishq.server.Config;
import version1.dishq.dishq.server.Response.DineoutMenuResponse;
import version1.dishq.dishq.server.RestApi;
import version1.dishq.dishq.util.DishqApplication;
import version1.dishq.dishq.util.Util;

import static version1.dishq.dishq.util.DishqApplication.getContext;

/**
 * Created by dishq on 09-01-2017.
 * Package name version1.dishq.dishq.
 */

public class DineoutMenuActivity extends BaseActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private static final String TAG = "DineoutMenuActivity";
    private RecyclerView dineMenuRecyclerView;
    private RecyclerView.LayoutManager recyclerViewLayoutManager;
    private ImageView backButton, deliveryButton;
    private TextView dineMenuHeader;
    private TextView dineMenuRestTypeText, dineMenuRp1, dineMenuRp2, dineMenuRp3,
            dineMenuRp4, dineMenuDrive, dineMenuRestType;
    private TextView dineMenuTags, dineMenuRestAdd;
    private Button call, directions;
    private AppBarLayout rlDineoutToolbar;
    private TextView tvPersonalizedMenu;
    private MixpanelAPI mixpanel = null;
    private ProgressBar progressBar;
    private ImageView appbarImage;
    private FrameLayout progressBg;

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
        setContentView(R.layout.activity_dineout_menu);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);
        progressBg = (FrameLayout) findViewById(R.id.progress_bg_overlay_dine);
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
        collapsingToolbar.setTitle("My Toolbar Title");
    }

    protected void setTags() {
        dineMenuRecyclerView = (RecyclerView) findViewById(R.id.dine_menu_recyclerview);
        tvPersonalizedMenu = (TextView) findViewById(R.id.personalized_menu_text);
        tvPersonalizedMenu.setTypeface(Util.opensanslight);
        backButton = (ImageView) findViewById(R.id.dine_menu_back_button);
        dineMenuHeader = (TextView) findViewById(R.id.dinemenu_toolbarTitle);
        dineMenuHeader.setTypeface(Util.opensanssemibold);
        deliveryButton = (ImageView) findViewById(R.id.delivery_option);
        dineMenuRestTypeText = (TextView) findViewById(R.id.dinemenu_rest_type_text);
        dineMenuRestTypeText.setTypeface(Util.opensansregular);
        dineMenuRestType = (TextView) findViewById(R.id.dinemenu_rest_type);
        dineMenuRestType.setTypeface(Util.opensansregular);
        dineMenuRp1 = (TextView) findViewById(R.id.dinemenu_rup_1);
        dineMenuRp2 = (TextView) findViewById(R.id.dinemenu_rup_2);
        dineMenuRp3 = (TextView) findViewById(R.id.dinemenu_rup_3);
        dineMenuRp4 = (TextView) findViewById(R.id.dinemenu_rup_4);
        dineMenuDrive = (TextView) findViewById(R.id.dinemenu_drive_time);
        dineMenuDrive.setTypeface(Util.opensansregular);
        dineMenuTags = (TextView) findViewById(R.id.dinemenu_tags);
        dineMenuTags.setTypeface(Util.opensansregular);
        dineMenuRestAdd = (TextView) findViewById(R.id.dinemenu_rest_addr);
        dineMenuRestAdd.setTypeface(Util.opensansregular);
        call = (Button) findViewById(R.id.dinemenu_call);
        call.setTypeface(Util.opensanssemibold);
        directions = (Button) findViewById(R.id.dinemenu_directions);
        directions.setTypeface(Util.opensanssemibold);
        rlDineoutToolbar = (AppBarLayout) findViewById(R.id.dinemenu_appbar);
        appbarImage = (ImageView) findViewById(R.id.dineout_appbar_bg_image);
        //appbarImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
        setFunctionality();
    }

    protected void setFunctionality() {

        String imageUrl = Util.dineoutRestData.getDineRestPhoto().get(0);
        Picasso.with(getContext())
                .load(imageUrl)
                .fit()
                .centerCrop()
                .into(appbarImage);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Util.setHomeRefreshRequired(false);
                finish();
            }
        });

        if (Util.dineoutRestData != null) {
            dineMenuHeader.setText(Util.dineoutRestData.getDineMenuRestName());

            if(Util.dineoutRestData.getHasHomeDelivery()) {
                deliveryButton.setVisibility(View.VISIBLE);
                deliveryButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            final JSONObject properties = new JSONObject();
                            properties.put("Delivery menu flipbutton", "dineoutmenu");
                            mixpanel.track("Delivery menu flipbutton", properties);
                        } catch (final JSONException e) {
                            throw new RuntimeException("Could not encode hour of the day in JSON");
                        }
                        Util.setDelRestId(Util.dineoutRestData.getDineMenuRestId());
                        Intent intent = new Intent(DineoutMenuActivity.this, DeliveryMenuActivity.class);
                        overridePendingTransition(R.anim.from_middle, R.anim.to_middle);
                        startActivity(intent);
                        finish();
                    }
                });
            } else{
                deliveryButton.setVisibility(View.GONE);
            }

            StringBuilder sb = new StringBuilder();
            if(Util.dineoutRestData.getDineMenuCusineText()!=null) {
                for (String s : Util.dineoutRestData.getDineMenuCusineText()) {
                    if (sb.length() > 0) {
                        sb.append(',' + " ");
                    }
                    sb.append(s);
                }
            }
            String dishTypeText = sb.toString();
            dineMenuRestTypeText.setText(String.valueOf(dishTypeText));

            int dinePriceLvl = Util.dineoutRestData.getDineMenuPriceLvl();
            if (dinePriceLvl == 1) {
                dineMenuRp1.setTextColor(ContextCompat.getColor(DishqApplication.getContext(), R.color.rupeeGreen));
            } else if (dinePriceLvl == 2) {
                dineMenuRp1.setTextColor(ContextCompat.getColor(DishqApplication.getContext(), R.color.rupeeGreen));
                dineMenuRp2.setTextColor(ContextCompat.getColor(DishqApplication.getContext(), R.color.rupeeGreen));
            } else if (dinePriceLvl == 3) {
                dineMenuRp1.setTextColor(ContextCompat.getColor(DishqApplication.getContext(), R.color.rupeeGreen));
                dineMenuRp2.setTextColor(ContextCompat.getColor(DishqApplication.getContext(), R.color.rupeeGreen));
                dineMenuRp3.setTextColor(ContextCompat.getColor(DishqApplication.getContext(), R.color.rupeeGreen));
            } else if (dinePriceLvl == 4) {
                dineMenuRp1.setTextColor(ContextCompat.getColor(DishqApplication.getContext(), R.color.rupeeGreen));
                dineMenuRp2.setTextColor(ContextCompat.getColor(DishqApplication.getContext(), R.color.rupeeGreen));
                dineMenuRp3.setTextColor(ContextCompat.getColor(DishqApplication.getContext(), R.color.rupeeGreen));
                dineMenuRp4.setTextColor(ContextCompat.getColor(DishqApplication.getContext(), R.color.rupeeGreen));
            }
            dineMenuDrive.setText(Util.dineoutRestData.getDineMenuDriveTime());

            String dishTags = "";
            if(Util.dineoutRestData.getDineMenuRestTags()!=null) {
                for (String s : Util.dineoutRestData.getDineMenuRestTags()) {
                    dishTags += s + " ";
                }
            }
            dineMenuTags.setText(String.valueOf(dishTags));

            StringBuilder stringBuilder = new StringBuilder();
            if(Util.dineoutRestData.getDineMenuRestTypeText()!=null) {
                for (String st : Util.dineoutRestData.getDineMenuRestTypeText()) {
                    if (stringBuilder.length() > 0) {
                        stringBuilder.append(',' + " ");
                    }
                    stringBuilder.append(st);

                }
            }
            String restTypeText = stringBuilder.toString();
            dineMenuRestType.setText(String.valueOf(restTypeText));

            String dineRestAddress = "";
            if(Util.dineoutRestData.getDineMenuRestAddr()!=null) {
                for (String s : Util.dineoutRestData.getDineMenuRestAddr()) {
                    dineRestAddress += s;
                }
            }
            dineMenuRestAdd.setText(dineRestAddress);

            final String finalDineRestContactNo = Util.dineoutRestData.getDineMenuRestContNo().get(0);
            call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        final JSONObject properties = new JSONObject();
                        properties.put("Call restaurant", "dineoutmenu");
                        mixpanel.track("Call restaurant", properties);
                    } catch (final JSONException e) {
                        throw new RuntimeException("Could not encode hour of the day in JSON");
                    }
                    String uri = "tel:" + finalDineRestContactNo.trim() ;
                    Uri number = Uri.parse(uri);
                    Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
                    startActivity(callIntent);
                }
            });

            StringTokenizer tokens = new StringTokenizer(Util.dineoutRestData.getDineMenuRestLatLong(), ",");
            final String targetLat = tokens.nextToken();// this will contain latitude
            final String targetLang = tokens.nextToken();// this will contain longitude

            directions.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        final JSONObject properties = new JSONObject();
                        properties.put("Directions to restaurant", "dineoutmenu");
                        mixpanel.track("Directions to restaurant", properties);
                    } catch (final JSONException e) {
                        throw new RuntimeException("Could not encode hour of the day in JSON");
                    }
                    String currentLatitude = Util.getLatitude();
                    String currentLongitude = Util.getLongitude();

                    String url = "http://maps.google.com/maps?saddr="+currentLatitude+","+currentLongitude+"&daddr="+targetLat+","+targetLang+"&mode=driving";
                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(url));
                    intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                    startActivity(intent);
                }
            });
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

    private void fetchDineoutMenuInfo() {
        String latitude = Util.getLatitude(), longitude = Util.getLongitude(), source = "dineoutmenu";
        RestApi restApi = Config.createService(RestApi.class);
        Call<DineoutMenuResponse> call = restApi.getDineoutMenuOptions(DishqApplication.getAccessToken(),
                Util.getDineRestId(), DishqApplication.getUniqueID(), source, latitude, longitude,
                Util.getGenericDishIdTab());
        call.enqueue(new Callback<DineoutMenuResponse>() {
            @Override
            public void onResponse(Call<DineoutMenuResponse> call, Response<DineoutMenuResponse> response) {
                Log.d(TAG, "Success");
                try {
                    if(response.isSuccessful()) {
                        Log.d(TAG, "Response is successful");
                        DineoutMenuResponse.DineoutMenuInfo body = response.body().dineoutMenuInfo;
                        if(body!=null) {
                            Log.d(TAG, "body is nt null");
                            Util.dineoutMenuInfos.clear();
                            Util.dineoutRestData = body.dineoutRestData;
                            for (int i = 0; i<body.dineoutMenuDatas.size(); i++) {
                                Util.dineoutMenuInfos = body.dineoutMenuDatas;
                            }
                            setTags();
                            recyclerViewLayoutManager = new GridLayoutManager(DineoutMenuActivity.this, 2);
                            Util.SpacesItemDecoration decoration = new Util.SpacesItemDecoration(13);
                            dineMenuRecyclerView.setLayoutManager(recyclerViewLayoutManager);
                            dineMenuRecyclerView.addItemDecoration(decoration);
                            dineMenuRecyclerView.setNestedScrollingEnabled(false);
                            DineoutMenuMasonryAdapter masonryAdapter = new DineoutMenuMasonryAdapter(DineoutMenuActivity.this);
                            progressBar.setVisibility(View.GONE);
                            progressBg.setVisibility(View.GONE);
                            dineMenuRecyclerView.setAdapter(masonryAdapter);
                        }
                        progressBar.setVisibility(View.GONE);
                        progressBg.setVisibility(View.GONE);
                    }else {
                        progressBar.setVisibility(View.GONE);
                        progressBg.setVisibility(View.GONE);
                        String error = response.errorBody().string();
                        Log.d(TAG, "response error: " +error);
                    }
                }catch (IOException e){
                    progressBar.setVisibility(View.GONE);
                    progressBg.setVisibility(View.GONE);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<DineoutMenuResponse> call, Throwable t) {
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
        if (ContextCompat.checkSelfPermission(DineoutMenuActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) { // first check
            getTheLocale();

        } else if (ContextCompat.checkSelfPermission(DineoutMenuActivity.this,
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
                                status.startResolutionForResult(DineoutMenuActivity.this, REQUEST_CHECK_SETTINGS);
                            } catch (IntentSender.SendIntentException e) {
                                // Ignore the error.
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            // Location settings are not satisfied. However, we have
                            // no way to fix the
                            // settings so we won't show the dialog.
                            alertNoForward(DineoutMenuActivity.this);
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
                        alertNoForward(DineoutMenuActivity.this);
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
                fetchDineoutMenuInfo();

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
        if (ActivityCompat.shouldShowRequestPermissionRationale(DineoutMenuActivity.this,
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
            ActivityCompat.requestPermissions(DineoutMenuActivity.this,
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
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(DineoutMenuActivity.this);
        builder.setTitle(title);
        builder.setMessage(message).setCancelable(false)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }


                })
                .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (ContextCompat.checkSelfPermission(DineoutMenuActivity.this, Manifest.permission.GET_ACCOUNTS) == PackageManager.PERMISSION_DENIED) {
                            ActivityCompat.requestPermissions(DineoutMenuActivity.this,
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
        if (!(DineoutMenuActivity.this).isFinishing()) {
            AlertDialog dialog = new AlertDialog.Builder(activity)
                    .setMessage("Can't update without GPS")
                    .setCancelable(false)
                    .setNegativeButton("Got it", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent backButtonIntent = new Intent(DineoutMenuActivity.this, DineoutMenuActivity.class);
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
