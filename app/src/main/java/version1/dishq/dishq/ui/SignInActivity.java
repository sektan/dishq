package version1.dishq.dishq.ui;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.plus.Plus;
import com.google.firebase.iid.FirebaseInstanceId;
import com.mixpanel.android.mpmetrics.MixpanelAPI;

import org.json.JSONException;
import org.json.JSONObject;

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


public class SignInActivity extends BaseActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;
    private static GoogleApiClient googleApiClient;
    private static String facebookOrGoogle = "";
    LoginButton loginButton;
    private ProgressBar progressBar;
    private CallbackManager callbackManager;
    private String facebookAccessToken = "";
    private Boolean GOOGLE_BUTTON_SELECTED, FACEBOOK_BUTTON_SELECTED;
    private Button facebookButton, googleButton;

    private MixpanelAPI mixpanel = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //MixPanel Instantiation
        mixpanel = MixpanelAPI.getInstance(this, getResources().getString(R.string.mixpanel_token));
        //Facebook SDK is initialized
        facebookSDKInitialize();
        //Build google client
        String serverClientId = DishqApplication.getContext().getString(R.string.server_client_id);
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestScopes(new Scope(Scopes.DRIVE_APPFOLDER))
                .requestServerAuthCode(serverClientId, false)
                .requestIdToken(serverClientId)
                .build();
        Log.d(TAG, "Google sign in has been set up");

        // [START build_client]
        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .addOnConnectionFailedListener(this)
                .build();
        // [END build_client]

        Log.d(TAG, "Google client has been created");
        setContentView(R.layout.activity_signin);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Logs 'install' and 'app activate'App events
        AppEventsLogger.activateApp(this);
        setTags();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //logs "app deactivate" app Event
        AppEventsLogger.deactivateApp(this);
    }

    //Initializing the facebook sdk
    protected void facebookSDKInitialize() {
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
    }

    protected void setTags() {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int h = displaymetrics.heightPixels;
        int w = displaymetrics.widthPixels;
        VideoView mVideoView = (VideoView) findViewById(R.id.VideoView);
        mVideoView.setLayoutParams(new RelativeLayout.LayoutParams(w, h));
        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/raw/" + R.raw.sign_in_video);
        mVideoView.setMediaController(null);
        mVideoView.setVideoURI(uri);
        mVideoView.requestFocus();
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });
        mVideoView.start();
        loginButton = (LoginButton) findViewById(R.id.facebook_login_button);
        facebookButton = (Button) findViewById(R.id.fb);
        facebookButton.setTypeface(Util.opensanssemibold);
        googleButton = (Button) findViewById(R.id.google_sign_up);
        googleButton.setTypeface(Util.opensanssemibold);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        TextView connectWith = (TextView) findViewById(R.id.connect_with);
        connectWith.setTypeface(Util.opensansregular);
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
                            progressBar.setVisibility(View.VISIBLE);
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
                progressBar.setVisibility(View.VISIBLE);
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
                    Log.d(TAG, "handleSignInResult is going to be called");
                    handleSignInResult(result);
                } catch (IOException | GoogleAuthException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.google_sign_up:
                GOOGLE_BUTTON_SELECTED = true;
                FACEBOOK_BUTTON_SELECTED = false;
                Log.d(TAG, "Sign in for google is called");
                    signIn();
                break;
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    // [START handleSignInResult]
    private void handleSignInResult(GoogleSignInResult result) throws IOException, GoogleAuthException {
        Log.e(TAG, "Handle: " + "1");
        if (result.isSuccess()) {
            Log.d(TAG, "result is a success" + result.toString());
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            assert acct != null;
            String authCode = acct.getServerAuthCode();
            Log.d(TAG, "the authCode is:" + authCode);
            String idToken = acct.getIdToken();
            Log.d(TAG, "the idToken is:" + idToken);
            fetchAccessToken(idToken);

            Log.e(TAG, acct.getDisplayName() + acct.getIdToken() + acct.getEmail());

        } else {
            Log.e(TAG, result + "");
        }
    }

    public void checkWhichActivity() {
        if (!DishqApplication.getOnBoardingDone()) {
            Intent i = new Intent(SignInActivity.this, OnBoardingActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            finish();
            startActivity(i);
        } else {
            Intent i = new Intent(SignInActivity.this, HomeActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            finish();
            startActivity(i);
        }
    }

    private void signIn() {
        Log.d(TAG, "Sign in is called here");
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void fetchAccessToken(final String accessToken) {
        Log.d(TAG, "fetchAccessToken called");
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
                progressBar.setVisibility(View.GONE);
                Log.d(TAG, "success");
                if(FACEBOOK_BUTTON_SELECTED) {
                    try {
                        final JSONObject properties = new JSONObject();
                        properties.put("facebook login", "signup");
                        mixpanel.track("facebook login", properties);
                    } catch (final JSONException e) {
                        throw new RuntimeException("Could not encode hour of the day in JSON");
                    }
                }else if(GOOGLE_BUTTON_SELECTED) {
                    try {
                        final JSONObject properties = new JSONObject();
                        properties.put("google login ", "signup");
                        mixpanel.track("google login", properties);
                    } catch (final JSONException e) {
                        throw new RuntimeException("Could not encode hour of the day in JSON");
                    }
                }
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
                            Boolean isNewUser = body.getIsNewUser();
                            DishqApplication.getPrefs().edit().putBoolean(Constants.IS_NEW_USER, true).apply();
                            DishqApplication.setIsNewUser(isNewUser);
                            if (isNewUser) {
                                DishqApplication.getPrefs().edit().putBoolean(Constants.ON_BOARDING_DONE, false).apply();
                                DishqApplication.setOnBoardingDone(false);
                            } else {
                                DishqApplication.getPrefs().edit().putBoolean(Constants.ON_BOARDING_DONE, true).apply();
                                DishqApplication.setOnBoardingDone(true);
                            }
                            checkWhichActivity();
                        }
                    } else {
                        String error = response.errorBody().string();
                        Log.d(TAG, "The error: " +error);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<SignUpResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Log.d(TAG, "failure");
            }
        });
    }

    @Override
    protected void onDestroy() {
        mixpanel.flush();
        super.onDestroy();
    }

}
