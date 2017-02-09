package version1.dishq.dishq.ui;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import version1.dishq.dishq.BaseActivity;
import version1.dishq.dishq.R;
import version1.dishq.dishq.adapters.menuFinderAdapters.MenuFinderNearbyRestAdapter;
import version1.dishq.dishq.server.Config;
import version1.dishq.dishq.server.Response.MenuFinderNearbyRestResponse;
import version1.dishq.dishq.server.Response.MenuFinderRestSuggestResponse;
import version1.dishq.dishq.server.RestApi;
import version1.dishq.dishq.util.DishqApplication;
import version1.dishq.dishq.util.Util;

/**
 * Created by dishq on 09-01-2017.
 * Package name version1.dishq.dishq.
 */

public class MenuFinder extends BaseActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private static final String TAG = "MenuFinder";
    RecyclerView recyclerView;
    ListView listView;
    protected LayoutManagerType mCurrentLayoutManagerType;
    protected RecyclerView.LayoutManager mLayoutManager;
    private EditText txtAutoComplete;
    private ProgressBar progressBar;
    private String str = "";
    private TextView nearbyTextView, mfTextView;
    private MyAdapter myAdapter;
    private LinearLayout norestaurant;
    private RelativeLayout rlNearByRest;

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
    private Boolean fetchSearchOptions = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_menu_finder);

        Toolbar toolbar = (Toolbar) findViewById(R.id.menu_finder_toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Restaurant Search");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);

        final int playServicesStatus = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);
        if (playServicesStatus != ConnectionResult.SUCCESS) {
            //If google play services in not available show an error dialog and return
            final Dialog errorDialog = GoogleApiAvailability.getInstance().getErrorDialog(this, playServicesStatus, 0, null);
            errorDialog.show();
            return;
        }

        checkInternetConnection(fetchSearchOptions, "");
        setTags();
    }

    protected void setTags() {
        listView = (ListView) findViewById(R.id.suggest_recyclerview);
        recyclerView = (RecyclerView) findViewById(R.id.menu_finder_recycler_view);
        txtAutoComplete = (EditText) findViewById(R.id.menufinder_autosuggest);
        txtAutoComplete.setTypeface(Util.opensansregular);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(txtAutoComplete.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
        rlNearByRest = (RelativeLayout) findViewById(R.id.rl_nearby_rest);
        rlNearByRest.setVisibility(View.GONE);
        mfTextView = (TextView) findViewById(R.id.menufinder_no_results_text);
        nearbyTextView = (TextView) findViewById(R.id.nearby_rest_text);
        nearbyTextView.setTypeface(Util.opensansregular);
        TextView searchDiffRest = (TextView) findViewById(R.id.search_diff_rest);
        searchDiffRest.setTypeface(Util.opensansregular);
        norestaurant = (LinearLayout) findViewById(R.id.noresult);
        setFunctionality();
    }

    protected void setFunctionality() {
        try {
            txtAutoComplete.setInputType(InputType.TYPE_CLASS_TEXT);
            txtAutoComplete.requestFocus();
            InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            mgr.showSoftInput(txtAutoComplete, InputMethodManager.SHOW_FORCED);
            txtAutoComplete.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    final int DRAWABLE_RIGHT = 2;
                    if (txtAutoComplete.getText().length() > 1) {
                        if (event.getAction() == MotionEvent.ACTION_UP) {
                            if (event.getRawX() >= (txtAutoComplete.getRight() - txtAutoComplete.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                                // your action here
                                txtAutoComplete.setText("");
                                Util.menuFinderRestInfos.clear();
                                myAdapter.notifyDataSetChanged();
                                return true;
                            }
                        }
                    }
                    return false;
                }
            });

            txtAutoComplete.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                    str = s.toString();
                    if (s.length() > 1) {
                        txtAutoComplete.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.menufinder_search, 0, R.drawable.menufinder_cross, 0);
                        if (!Util.checkAndShowNetworkPopup(MenuFinder.this)) {
                            progressBar.setVisibility(View.VISIBLE);
                            checkInternetConnection(true, str);
                            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                        }
                    } else {
                        txtAutoComplete.setCompoundDrawablesWithIntrinsicBounds(R.drawable.menufinder_search, 0, 0, 0);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                }
            });

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Method to check if the internet is connected or not
    private void checkInternetConnection(Boolean fetchSearchOptions, String query) {
        SharedPreferences settings;
        final String PREFS_NAME = "MyPrefsFile";
        settings = getSharedPreferences(PREFS_NAME, 0);

        if (settings.getBoolean("android_M", true)) {
            //the app is being launched for first time, do something
            Log.d("Comments", " android_M");
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // only for gingerbread and newer versions
                checkNetwork(fetchSearchOptions, query);
            } else {
                checkNetwork(fetchSearchOptions, query);
            }
            settings.edit().putBoolean("android_M", false).apply();
        } else {
            checkNetwork(fetchSearchOptions, query);
        }
    }

    //Check for internet
    private void checkNetwork(Boolean fetchSearchOptions, String query) {
        if (!Util.checkAndShowNetworkPopup(this)) {
            //Check for version
            Log.d(TAG, "Checking for GPS");

            if (!fetchSearchOptions) {
                //Check for gps
                checkGPS();

            } else {
                fetchRestaurant(query);
            }

        } else {
            networkFailed = true;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void showNearbyRest() {
        RestApi restApi = Config.createService(RestApi.class);
        String currentLatitude = Util.getLatitude();
        String currentLongitude = Util.getLongitude();
        Call<MenuFinderNearbyRestResponse> call = restApi.getNearbyRestaurants(DishqApplication.getUniqueID(),
                DishqApplication.getUserID(), currentLatitude, currentLongitude);
        call.enqueue(new Callback<MenuFinderNearbyRestResponse>() {
            @Override
            public void onResponse(Call<MenuFinderNearbyRestResponse> call,
                                   Response<MenuFinderNearbyRestResponse> response) {
                Log.d(TAG, "Success");
                try {
                    if (response.isSuccessful()) {
                        ArrayList<MenuFinderNearbyRestResponse.NearbyRestInfo> body = response.body().nearbyRestInfos;
                        if (body != null) {
                            Log.d(TAG, "body is not null");
                            Util.nearbyRestInfos.clear();
                            for (int i = 0; i < body.size(); i++) {
                                Util.nearbyRestInfos = body;
                                Log.d(TAG, "the restaurant" + Util.nearbyRestInfos.get(i).getNearByRestName()+
                                        "the price level is: " + Util.nearbyRestInfos.get(i).getPriceLevel());

                            }
                            MenuFinderNearbyRestAdapter adapter = new MenuFinderNearbyRestAdapter(MenuFinder.this);
                            recyclerView.setAdapter(adapter);
                            mLayoutManager = new LinearLayoutManager(MenuFinder.this);
                            mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
                            progressBar.setVisibility(View.GONE);
                            rlNearByRest.setVisibility(View.VISIBLE);
                            setRecyclerViewLayoutManager(mCurrentLayoutManagerType);
                            adapter.notifyDataSetChanged();
                        }
                    } else {
                        progressBar.setVisibility(View.GONE);
                        String error = response.errorBody().string();
                        Log.d(TAG, "Error: " + error);
                    }
                } catch (IOException e) {
                    progressBar.setVisibility(View.GONE);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<MenuFinderNearbyRestResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Log.d(TAG, "Failure");
            }
        });
    }

    private void fetchRestaurant(String query) {
        RestApi restApi = Config.createService(RestApi.class);
        String currentLatitude = Util.getLatitude();
        String currentLongitude = Util.getLongitude();
        Call<MenuFinderRestSuggestResponse> call = restApi.getRestSuggestion(query, DishqApplication.getUniqueID(),
                DishqApplication.getUserID(), currentLatitude, currentLongitude);
        call.enqueue(new Callback<MenuFinderRestSuggestResponse>() {
            @Override
            public void onResponse(Call<MenuFinderRestSuggestResponse> call, Response<MenuFinderRestSuggestResponse> response) {
                Log.d(TAG, "Success");
                try {
                    if (response.isSuccessful()) {
                        ArrayList<MenuFinderRestSuggestResponse.MenuFinderRestInfo> body = response.body().menuFinderRestInfos;
                        if (body != null) {
                            if (body.size() != 0) {
                                Log.d(TAG, "body is not null");

                                norestaurant.setVisibility(View.GONE);
                                Util.menuFinderRestInfos.clear();
                                rlNearByRest.setVisibility(View.GONE);
                                for (int i = 0; i < body.size(); i++) {
                                    Util.menuFinderRestInfos = body;
                                }
                                myAdapter = new MyAdapter(MenuFinder.this);
                                progressBar.setVisibility(View.GONE);
                                listView.setVisibility(View.VISIBLE);
                                listView.setAdapter(myAdapter);

                            } else {
                                progressBar.setVisibility(View.GONE);
                                Util.menuFinderRestInfos.clear();
                                norestaurant.setVisibility(View.VISIBLE);
                                listView.setVisibility(View.GONE);
                            }
                        }
                    } else {
                        progressBar.setVisibility(View.GONE);
                        String error = response.errorBody().string();
                        Log.d(TAG, "Error: " + error);
                    }
                } catch (IOException e) {
                    progressBar.setVisibility(View.GONE);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<MenuFinderRestSuggestResponse> call, Throwable t) {
                Log.d(TAG, "Failure");
                if (t instanceof SocketTimeoutException) {
                    progressBar.setVisibility(View.GONE);
                    Log.e("timeout", "timeout" + t.getMessage());
                    AlertDialog.Builder builder = new AlertDialog.Builder(MenuFinder.this);
                    builder.setMessage("Your Internet connection is slow. Please find a better connection.").setCancelable(false)
                            .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    fetchRestaurant(str);
                                }
                            });
                }
            }
        });
    }

    public void setRecyclerViewLayoutManager(LayoutManagerType layoutManagerType) {
        int scrollPosition = 0;

        // If a layout manager has already been set, get current scroll position.
        if (recyclerView.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) recyclerView.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition();
        }
        switch (layoutManagerType) {
            case LINEAR_LAYOUT_MANAGER:
                mLayoutManager = new LinearLayoutManager(MenuFinder.this);
                mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
                break;
            default:
                mLayoutManager = new LinearLayoutManager(MenuFinder.this);
                mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
        }

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.scrollToPosition(scrollPosition);
    }

    public void checkGPS() {
        createLocationRequest();
        if (ContextCompat.checkSelfPermission(MenuFinder.this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) { // first check
            getTheLocale();

        } else if (ContextCompat.checkSelfPermission(MenuFinder.this,
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
                                status.startResolutionForResult(MenuFinder.this, REQUEST_CHECK_SETTINGS);
                            } catch (IntentSender.SendIntentException e) {
                                // Ignore the error.
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            // Location settings are not satisfied. However, we have
                            // no way to fix the
                            // settings so we won't show the dialog.
                            alertNoForward(MenuFinder.this);
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
                        alertNoForward(MenuFinder.this);
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
                showNearbyRest();

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
        if (ActivityCompat.shouldShowRequestPermissionRationale(MenuFinder.this,
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
            ActivityCompat.requestPermissions(MenuFinder.this,
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
                    getTheLocale();
                } else {
                    showAlert("", "That permission is needed in order to update wait time. Tap Retry.");
                }
            }
        }
    }

    public void showAlert(String title, String message) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(MenuFinder.this);
        builder.setTitle(title);
        builder.setMessage(message).setCancelable(false)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }


                })
                .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (ContextCompat.checkSelfPermission(MenuFinder.this, Manifest.permission.GET_ACCOUNTS) == PackageManager.PERMISSION_DENIED) {
                            ActivityCompat.requestPermissions(MenuFinder.this,
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
        if (!(MenuFinder.this).isFinishing()) {
            android.support.v7.app.AlertDialog dialog = new android.support.v7.app.AlertDialog.Builder(activity)
                    .setMessage("You can't continue without GPS")
                    .setCancelable(false)
                    .setNegativeButton("Got it", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent backButtonIntent = new Intent(MenuFinder.this, MenuFinder.class);
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

    private enum LayoutManagerType {
        LINEAR_LAYOUT_MANAGER
    }

    class MyAdapter extends BaseAdapter {
        LayoutInflater layoutInflater;
        Context context;
        TextView mfRestName, mfRestAddr, mfRestCuisine, mfRup1,
                mfRup2, mfRup3, mfRup4, mfDriveTime, mfRestTypeText, mfDeliveryTime;

        RelativeLayout rlNearbyRest;

        ImageView cardBgImage;

        FrameLayout mfClosedFrame;

        Button mfClosed;

        MyAdapter(Context context) {
            this.context = context;
            layoutInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return Util.menuFinderRestInfos.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = null;
            if (view == null) {
                view = layoutInflater.inflate(R.layout.cardview_dineout, parent, false);
                mfRestName = (TextView) view.findViewById(R.id.dineout_rest_name);
                mfRestName.setTypeface(Util.opensanssemibold);
                mfRestAddr = (TextView) view.findViewById(R.id.dineout_rest_addr);
                mfRestAddr.setTypeface(Util.opensansregular);
                mfRestTypeText = (TextView) view.findViewById(R.id.dineout_rest_type);
                mfRestTypeText.setTypeface(Util.opensansregular);
                mfRestCuisine = (TextView) view.findViewById(R.id.dineout_rest_cuisine);
                mfRup1 = (TextView) view.findViewById(R.id.dineout_rup_1);
                mfRup2 = (TextView) view.findViewById(R.id.dineout_rup_2);
                mfRup3 = (TextView) view.findViewById(R.id.dineout_rup_3);
                mfRup4 = (TextView) view.findViewById(R.id.dineout_rup_4);
                mfDriveTime = (TextView) view.findViewById(R.id.dineout_drive_time);
                mfDriveTime.setTypeface(Util.opensanssemibold);
                mfDeliveryTime = (TextView) view.findViewById(R.id.dineout_delivery_time);
                mfDeliveryTime.setTypeface(Util.opensanssemibold);
                rlNearbyRest = (RelativeLayout) view.findViewById(R.id.cv_rl_dineout);
                cardBgImage = (ImageView) view.findViewById(R.id.card_bg_image);
                cardBgImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
                mfClosedFrame = (FrameLayout) view.findViewById(R.id.frame_dine_rest_closed);
                mfClosed = (Button) view.findViewById(R.id.dine_rest_closed_button);
                mfClosed.setTypeface(Util.opensanssemibold);
            }

            /**
             * Setting the UI in case the restaurant is closed
             */
            if (Util.menuFinderRestInfos.get(position).getMfOpenNow()) {
                mfClosedFrame.setVisibility(View.GONE);
                mfClosed.setVisibility(View.GONE);
            } else {
                mfClosedFrame.setVisibility(View.VISIBLE);
                mfClosed.setVisibility(View.VISIBLE);
            }
            /**********************************************************************/

            /**
             * Setting the background image
             */
            ArrayList<String> imageUrls = Util.menuFinderRestInfos.get(position).getMfPhotoThumbnail();
            String imageUrl = imageUrls.get(0);
            Picasso.with(DishqApplication.getContext())
                    .load(imageUrl)
                    .fit()
                    .centerCrop()
                    .noPlaceholder()
                    .into(cardBgImage);
            /*********************************************************************************************/

            final int posn = position;
            rlNearbyRest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Util.setDineRestId(Util.menuFinderRestInfos.get(posn).getMfRestId());
                    Intent intent = new Intent(context, DineoutMenuActivity.class);
                    context.startActivity(intent);
                }
            });
            mfRestName.setText(Util.menuFinderRestInfos.get(position).getMfRestName());

            /**
             * Setting the restaurant address
             */
            String mfRestAddress = "";
            if (Util.menuFinderRestInfos.get(position).getMfRestAddr() != null) {
                for (String s : Util.menuFinderRestInfos.get(position).getMfRestAddr()) {
                    mfRestAddress += s;
                }
            }
            mfRestAddr.setText(mfRestAddress);

            StringBuilder sb = new StringBuilder();
            if (Util.menuFinderRestInfos.get(position).getMfRestCuisineText() != null) {
                for (String s : Util.menuFinderRestInfos.get(position).getMfRestCuisineText()) {
                    if (sb.length() > 0) {
                        sb.append(',' + " ");
                    }
                    sb.append(s);
                }
            }
            String mfCusineText = sb.toString();
            mfRestCuisine.setText(mfCusineText);

            /**
             * Setting the price level for the restaurant
             */
            int mfPriceLvl = Util.menuFinderRestInfos.get(position).getMfPriceLvl();
            if (mfPriceLvl == 1) {
                mfRup1.setTextColor(ContextCompat.getColor(DishqApplication.getContext(), R.color.rupeeGreen));
            } else if (mfPriceLvl == 2) {
                mfRup1.setTextColor(ContextCompat.getColor(DishqApplication.getContext(), R.color.rupeeGreen));
                mfRup2.setTextColor(ContextCompat.getColor(DishqApplication.getContext(), R.color.rupeeGreen));
            } else if (mfPriceLvl == 3) {
                mfRup1.setTextColor(ContextCompat.getColor(DishqApplication.getContext(), R.color.rupeeGreen));
                mfRup2.setTextColor(ContextCompat.getColor(DishqApplication.getContext(), R.color.rupeeGreen));
                mfRup3.setTextColor(ContextCompat.getColor(DishqApplication.getContext(), R.color.rupeeGreen));
            } else if (mfPriceLvl == 4) {
                mfRup1.setTextColor(ContextCompat.getColor(DishqApplication.getContext(), R.color.rupeeGreen));
                mfRup2.setTextColor(ContextCompat.getColor(DishqApplication.getContext(), R.color.rupeeGreen));
                mfRup3.setTextColor(ContextCompat.getColor(DishqApplication.getContext(), R.color.rupeeGreen));
                mfRup4.setTextColor(ContextCompat.getColor(DishqApplication.getContext(), R.color.rupeeGreen));
            }
            /***************************************************************************************************************/

            /**
             * Setting the drive time for the restaurant
             */
            mfDriveTime.setText(Util.menuFinderRestInfos.get(position).getMfDriveTime());
            /****************************************************************************************************************/

            /**
             * Setting the delivery time of the restaurant
             */
            mfDeliveryTime.setText(Util.menuFinderRestInfos.get(position).getMfDeliveryTime());

            String mfRestType = "";
            if (Util.menuFinderRestInfos.get(position).getMfRestTypeText() != null) {
                for (String s : Util.menuFinderRestInfos.get(position).getMfRestTypeText()) {
                    mfRestType += s + " ";
                }
            }
            mfRestTypeText.setText(mfRestType);
            return view;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Util.setHomeRefreshRequired(false);
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
