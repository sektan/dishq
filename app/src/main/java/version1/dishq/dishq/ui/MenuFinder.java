package version1.dishq.dishq.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import version1.dishq.dishq.R;
import version1.dishq.dishq.adapters.MenuFinderRecyclerAdapter;
import version1.dishq.dishq.server.Config;
import version1.dishq.dishq.server.Response.MenuFinderResponse;
import version1.dishq.dishq.server.RestApi;

public class MenuFinder extends AppCompatActivity implements
        ActivityCompat.OnRequestPermissionsResultCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    RecyclerView recyclerView;
    MenuFinderRecyclerAdapter adapter;

    double latitude, longitude;
    String bestProvider;

    LocationManager locationManager;

    List<MenuFinderResponse.Datum> datumList;

    GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_finder);

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.menu_finder_toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Menu Finder");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        datumList = new ArrayList<>();
        adapter = new MenuFinderRecyclerAdapter(this, datumList);

        recyclerView = (RecyclerView) findViewById(R.id.menu_finder_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

//        initializeLocation();
    }

    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }


    @Override
    protected void onResume() {
        super.onResume();

        /*if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(bestProvider, 5 * 1000, 10, locationListener);
        }*/
    }

    /*private void initializeLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                Toast.makeText(this, "Enable location permission", Toast.LENGTH_SHORT).show();
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
            }
        } else {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            Criteria criteria = new Criteria();
            criteria.setPowerRequirement(Criteria.POWER_LOW);
            criteria.setAccuracy(Criteria.ACCURACY_FINE);
            criteria.setSpeedRequired(true);
            criteria.setAltitudeRequired(false);
            criteria.setBearingRequired(false);
            criteria.setCostAllowed(false);

            bestProvider = locationManager.getBestProvider(criteria, false);
            if (bestProvider != null) {
                Location location = locationManager.getLastKnownLocation(bestProvider);
                if (location != null) {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                    requestNetworkCall();
                }
                locationManager.requestLocationUpdates(bestProvider, 2 * 1000, 10, locationListener);
            }
        }
    }*/

    private void requestNetworkCall() {
        RestApi restApi = Config.createService(RestApi.class);
        // TODO Change the hardcoded values
        Call<MenuFinderResponse> call = restApi.getNearbyRestaurants("9cIl2ANA2PaYWdABtHIZMCl2rxhu06",
                0, String.valueOf(latitude), String.valueOf(longitude));
        call.enqueue(new Callback<MenuFinderResponse>() {
            @Override
            public void onResponse(Call<MenuFinderResponse> call, Response<MenuFinderResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getResponse().equalsIgnoreCase("success")) {
                        updateResultsToAdapter(response.body().getData());
                    } else {
                        Toast.makeText(MenuFinder.this, "Failed to get data", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MenuFinder.this, "Failed to get data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MenuFinderResponse> call, Throwable t) {
                Toast.makeText(MenuFinder.this, "Failed to get data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void updateResultsToAdapter(List<MenuFinderResponse.Datum> datumList) {
        if (datumList != null && datumList.size() > 0) {
            this.datumList.clear();
            this.datumList = datumList;
            adapter.notifyDataSetChanged();
        } else {
            // TODO Display no data found message
        }
    }

    /*LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();

                if (latitude != 0.0 && longitude != 0.0) {
                    if (locationManager != null) {
                        if (ContextCompat.checkSelfPermission(MenuFinder.this, Manifest.permission
                                .ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            locationManager.removeUpdates(locationListener);
                        }

                        requestNetworkCall();
                    }
                }

            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Toast.makeText(MenuFinder.this, "onStatusChanged : " + provider, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onProviderEnabled(String provider) {
            Toast.makeText(MenuFinder.this, "onProviderEnabled : " + provider, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onProviderDisabled(String provider) {
            Toast.makeText(MenuFinder.this, "onProviderDisabled : " + provider, Toast.LENGTH_SHORT).show();
        }
    };*/

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case 100:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    /*initializeLocation();*/
                } else {
                    Toast.makeText(this, "Require permission to be enabled", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                Toast.makeText(this, "Enable location permission", Toast.LENGTH_SHORT).show();
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
            }
        } else {
            Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (location != null) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                Toast.makeText(this, "LL " + latitude + " , " + longitude, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
