package version1.dishq.dishq.ui;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.location.Location;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
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
import com.google.android.gms.plus.Plus;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import version1.dishq.dishq.BaseActivity;
import version1.dishq.dishq.R;
import version1.dishq.dishq.server.Config;
import version1.dishq.dishq.server.Request.SignUpHelper;
import version1.dishq.dishq.server.Response.SignUpResponse;
import version1.dishq.dishq.server.RestApi;
import version1.dishq.dishq.util.Constants;
import version1.dishq.dishq.util.DishqApplication;
import version1.dishq.dishq.util.Util;

/**
 * Created by dishq on 13-12-2016.
 * Package name version1.dishq.dishq.
 */


public class SignInActivity extends BaseActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks, LocationListener{

    protected static final int REQUEST_CHECK_SETTINGS = 1000;
    private static final String TAG = "SignUpActivity";
    private static final int RC_SIGN_IN = 9001;
    private static final long INTERVAL = 1000 * 10;
    private static final long FASTEST_INTERVAL = 1000 * 5;
    private static GoogleApiClient googleApiClient;
    private static String lat = "0.0";
    private static String lang = "0.0";
    private static String facebookOrGoogle = "";
    final int MY_PERMISSIONS_REQUEST_GPS_ACCESS = 1;
    private final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 0;
    LocationRequest mLocationRequest;
    String ace = "";
    LoginButton loginButton;
    private ProgressDialog progressDialog;
    private CallbackManager callbackManager;
    private Location mLastLocation;
    private String facebookAccessToken = "";
    private Boolean GOOGLE_BUTTON_SELECTED, FACEBOOK_BUTTON_SELECTED;
    private Button facebookButton, googleButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Facebook SDK is initialized
        facebookSDKInitialize();
        //Build google client
        buildGoogleClient(googleSignInInitialize());
        final int playServicesStatus = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);
        if (playServicesStatus != ConnectionResult.SUCCESS) {
            //If google play services in not available show an error dialog and return
            final Dialog errorDialog = GoogleApiAvailability.getInstance().getErrorDialog(this, playServicesStatus, 0, null);
            errorDialog.show();
            return;
        }

        setContentView(R.layout.activity_signin);
        setTags();
    }

    //Initializing the facebook sdk
    protected void facebookSDKInitialize() {
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
    }

    //Initializing googleSignIn
    protected GoogleSignInOptions googleSignInInitialize() {
        String serverClientId = DishqApplication.getContext().getString(R.string.server_client_id);
        return new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestServerAuthCode(serverClientId)
                .requestIdToken(serverClientId)
                .build();
    }

    //Building a google client
    protected void buildGoogleClient(GoogleSignInOptions googleSignInOptions) {
        // [START build_client]
        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .addOnConnectionFailedListener(this).
                        addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .addApi(Plus.API)
                .build();
        // [END build_client]
    }

    protected void setTags() {
        loginButton = (LoginButton) findViewById(R.id.facebook_login_button);
        facebookButton = (Button) findViewById(R.id.fb);
        googleButton = (Button) findViewById(R.id.google_sign_up);
        VideoView mVideoView = (VideoView)findViewById(R.id.VideoView);
        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/raw/" + R.raw.sign_in_video);
        mVideoView.setMediaController(null);
        mVideoView.setVideoURI(uri);
        mVideoView.requestFocus();
        mVideoView.setOnPreparedListener (new MediaPlayer.OnPreparedListener() {
                                              @Override
                                              public void onPrepared(MediaPlayer mp) {
                                                  mp.setLooping(true);
                                              }
                                          });
        mVideoView.start();
        TextView connectWith = (TextView) findViewById(R.id.connect_with);
        setClickables();
    }

    //Setting up the clickables of the current activity
    public void setClickables() {
        if (loginButton != null) {
            loginButton.setReadPermissions("email");
            getLoginDetails(loginButton);
            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GOOGLE_BUTTON_SELECTED = false;
                }
            });
        }

        if (facebookButton != null) {
            facebookButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GOOGLE_BUTTON_SELECTED = false;
                    FACEBOOK_BUTTON_SELECTED = true;
                    facebookOrGoogle = "facebook";
                    DishqApplication.getPrefs().edit().putString(Constants.FACEBOOK_OR_GOOGLE, facebookOrGoogle).apply();
                    DishqApplication.setFacebookOrGoogle(facebookOrGoogle);
                    if (!DishqApplication.getAccessToken().equals("null null")) {
                        AccessToken accessToken = AccessToken.getCurrentAccessToken();
                        if (accessToken != null && accessToken.getToken() != null) {
                            FACEBOOK_BUTTON_SELECTED = true;
                            progressDialog = new ProgressDialog(SignInActivity.this);
                            progressDialog.show();
                            fetchAccessToken(accessToken.getToken());
                        }
                    } else {
                        loginButton.performClick();
                    }
                }
            });
        }
        if (googleButton != null) {
            GOOGLE_BUTTON_SELECTED = true;
            googleButton.setOnClickListener(this);
        }
    }

    //retrieving facebook login details
    protected void getLoginDetails(LoginButton loginButton) {
        //Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                facebookOrGoogle = "facebook";
                DishqApplication.setFacebookOrGoogle(facebookOrGoogle);
                DishqApplication.getPrefs().edit().putString(Constants.FACEBOOK_OR_GOOGLE, facebookOrGoogle).apply();
                facebookAccessToken = loginResult.getAccessToken().getToken();
                progressDialog = new ProgressDialog(SignInActivity.this);
                progressDialog.show();
                fetchAccessToken(facebookAccessToken);
                Log.d(TAG, "Facebook Access Token: " + facebookAccessToken);
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "OnCancel");
            }

            @Override
            public void onError(FacebookException error) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (!GOOGLE_BUTTON_SELECTED) {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        } else {
            if (requestCode == RC_SIGN_IN) {
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                try {
                    handleSignInResult(result);
                } catch (IOException | GoogleAuthException e) {
                    e.printStackTrace();
                }
            } else if (requestCode == 1000) {
                switch (resultCode) {
                    case Activity.RESULT_OK: {
                        Log.d(TAG, "VALUES--OK");
                        // All required changes were successfully made
                        getLocation();
                        break;
                    }
                    case Activity.RESULT_CANCELED: {
                        alertNoForward(SignInActivity.this);
                        // The user was asked to change settings, but chose not to
                        break;
                    }
                    default: {
                        break;
                    }
                }
            }
        }

    }

    public void checkGPS() {
        createLocationRequest();
        if (ContextCompat.checkSelfPermission(SignInActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) { // first check
            getTheLocale();

        } else if (ContextCompat.checkSelfPermission(SignInActivity.this,
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
                                status.startResolutionForResult(SignInActivity.this, REQUEST_CHECK_SETTINGS);
                            } catch (IntentSender.SendIntentException e) {
                                // Ignore the error.
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            // Location settings are not satisfied. However, we have
                            // no way to fix the
                            // settings so we won't show the dialog.
                            alertNoForward(SignInActivity.this);
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
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        getTheLocale();
    }

    // [START handleSignInResult]
    private void handleSignInResult(GoogleSignInResult result) throws IOException, GoogleAuthException {
        Log.e("handle", "1");
        if (result.isSuccess()) {

            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            assert acct != null;
            final String access = acct.getServerAuthCode();
            final String SCOPES = "https://www.googleapis.com/auth/userinfo.profile";

            AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {
                @Override
                protected String doInBackground(Void... params) {
                    try {

                        if (ActivityCompat.checkSelfPermission(SignInActivity.this, Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            ace = GoogleAuthUtil.getToken(getApplicationContext(),
                                    Plus.AccountApi.getAccountName(googleApiClient),
                                    "oauth2:" + SCOPES);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    Log.i(TAG, "googleAccessToken: " + access);
                    return ace;
                }

                @Override
                protected void onPostExecute(String token) {
                    Log.i(TAG, "Access token retrieved:" + ace);
                    facebookOrGoogle = "google";
                    DishqApplication.getPrefs().edit().putString(Constants.FACEBOOK_OR_GOOGLE, facebookOrGoogle).apply();
                    DishqApplication.setFacebookOrGoogle(facebookOrGoogle);
                    progressDialog = new ProgressDialog(SignInActivity.this);
                    progressDialog.show();
                    fetchAccessToken(ace);
                }
            };
            task.execute();

            Log.e("signin", acct.getDisplayName() + acct.getIdToken() + acct.getEmail());


        } else {
            Log.e("google", result + "");
        }
    }

    public void checkWhichActivity(Boolean isNewUser) {
        if (!DishqApplication.getOnBoardingDone()) {
            // if (isNewUser) {
            Intent i = new Intent(SignInActivity.this, OnBoardingActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            finish();
            startActivity(i);
            // }
        } else {
            //Check for gps

            checkGPS();
        }
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    void selfPermissionAccounts() {
        if (ContextCompat.checkSelfPermission(SignInActivity.this,
                Manifest.permission.GET_ACCOUNTS)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(SignInActivity.this,
                    Manifest.permission.GET_ACCOUNTS)) {
                Log.e(TAG, "accept");
            } else {
                // we can request the permission.
                Log.e(TAG, "not accept");
                ActivityCompat.requestPermissions(SignInActivity.this,
                        new String[]{Manifest.permission.GET_ACCOUNTS},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);
            }
        }
    }

    private void fetchAccessToken(final String accessToken) {
        String backend = "";
        if (FACEBOOK_BUTTON_SELECTED) {
            backend = getString(R.string.backend_facebook);
        } else if (GOOGLE_BUTTON_SELECTED) {
            backend = getString(R.string.backend_google);
        }

        // Retrieving unique gcm device registration id
        String gcmDeviceRegId = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "The gcmDeviceRegId: " + gcmDeviceRegId);
        //Creating an APIRequest
        final SignUpHelper signUpHelper = new SignUpHelper(DishqApplication.getContext().getString(R.string.conv_token),
                backend, DishqApplication.getContext().getString(R.string.client_id),
                DishqApplication.getContext().getString(R.string.client_secret), DishqApplication.getUniqueID(), gcmDeviceRegId, accessToken);
        String authorization = "Bearer" + " " + backend + " " + accessToken;
        RestApi restApi = Config.createService(RestApi.class);
        Call<SignUpResponse> call = restApi.createNewUser(authorization, signUpHelper);
        call.enqueue(new Callback<SignUpResponse>() {
            @Override
            public void onResponse(Call<SignUpResponse> call, Response<SignUpResponse> response) {
                progressDialog.dismiss();
                Log.d(TAG, "success");
                try {
                    if (response.isSuccessful()) {
                        SignUpResponse.NewUserDataInfo body = response.body().newUserDataInfo;
                        if (body != null) {
                            Log.d(TAG, "AccessToken: " + body.getAccessToken());
                            //Storing the AccessToken in shared preferences
                            DishqApplication.getPrefs().edit().putString(Constants.ACCESS_TOKEN, body.getAccessToken()).apply();
                            DishqApplication.getPrefs().edit().putString(Constants.REFRESH_TOKEN, body.getRefreshToken()).apply();
                            DishqApplication.getPrefs().edit().putString(Constants.TOKEN_TYPE, body.getTokenType()).apply();
                            DishqApplication.setAccessToken(body.getAccessToken(), body.getTokenType());

                            //Storing userID
                            DishqApplication.getPrefs().edit().putInt(Constants.USER_ID, body.userDataInfo.getUserId()).apply();
                            DishqApplication.setUserID(body.userDataInfo.getUserId());

                            //Storing the status of new/old user
                            DishqApplication.getPrefs().edit().putBoolean(Constants.IS_NEW_USER, body.getIsNewUser()).apply();
                            DishqApplication.setIsNewUser(body.getIsNewUser());

                            //Storing User Name
                            DishqApplication.getPrefs().edit().putString(Constants.USER_NAME, body.userDataInfo.getFirstName()).apply();
                            DishqApplication.setUserName(body.userDataInfo.getFirstName());
                            checkWhichActivity(body.getIsNewUser());
                        }
                    } else {
                        String error = response.errorBody().string();
                        Log.d(TAG, error);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<SignUpResponse> call, Throwable t) {
                progressDialog.dismiss();
                Log.d(TAG, "failure");
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    signIn();
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Log.e(TAG, "Permission was granted by the user");
                } else {
                    Log.e(TAG, "Permission was denied by the user");
                    Util.showAlert("", "That permission is needed to use Google Signup. Tap retry or use Facebook to Signup.", SignInActivity.this);

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                break;
            }
            case MY_PERMISSIONS_REQUEST_GPS_ACCESS: {

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLocation();
                } else {
                    showAlert("", "That permission is needed in order to update wait time. Tap Retry.");
                }
            }
            // other 'case' lines to check for other
            // permissions this app might request
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
    public void onLocationChanged(Location location) {

    }

    public void showAlert(String title, String message) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(SignInActivity.this);
        builder.setTitle(title);
        builder.setMessage(message).setCancelable(false)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }


                })
                .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (ContextCompat.checkSelfPermission(SignInActivity.this, Manifest.permission.GET_ACCOUNTS) == PackageManager.PERMISSION_DENIED) {
                            ActivityCompat.requestPermissions(SignInActivity.this,
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
        if (!(SignInActivity.this).isFinishing()) {
            AlertDialog dialog = new AlertDialog.Builder(activity)
                    .setMessage("Can't update without GPS")
                    .setCancelable(false)
                    .setNegativeButton("Got it", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent backButtonIntent = new Intent(SignInActivity.this, SignInActivity.class);
                            backButtonIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            finish();
                            startActivity(backButtonIntent);
                        }
                    })
                    .create();
            dialog.show();
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
                Intent startHomeActivity = new Intent(SignInActivity.this, HomeActivity.class);
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
        if (ActivityCompat.shouldShowRequestPermissionRationale(SignInActivity.this,
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
            ActivityCompat.requestPermissions(SignInActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_GPS_ACCESS);
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.google_sign_up:
                GOOGLE_BUTTON_SELECTED = true;
                FACEBOOK_BUTTON_SELECTED = false;
                if (ContextCompat.checkSelfPermission(SignInActivity.this, Manifest.permission.GET_ACCOUNTS) == PackageManager.PERMISSION_GRANTED) {
                    signIn();
                } else if (ContextCompat.checkSelfPermission(SignInActivity.this, Manifest.permission.GET_ACCOUNTS) == PackageManager.PERMISSION_DENIED) {
                    selfPermissionAccounts();
                }
                break;
        }
    }

}
