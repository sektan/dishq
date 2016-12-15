package version1.dishq.dishq.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;

import version1.dishq.dishq.BaseActivity;
import version1.dishq.dishq.R;
import version1.dishq.dishq.util.Constants;
import version1.dishq.dishq.util.DishqApplication;

/**
 * Created by dishq on 13-12-2016.
 * Package name version1.dishq.dishq.
 */

public class SignInActivity extends BaseActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "SignUpActivity";
    private static GoogleApiClient mGoogleApiClient;
    private ProgressDialog progressDialog;
    private CallbackManager callbackManager;
    private static final int RC_SIGN_IN = 9001;
    final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 0;
    private String facebookAccessToken = "";
    private static String facebookOrGoogle = "";
    String ace = "";
    LoginButton loginButton;
    private Boolean GOOGLE_BUTTON_SELECTED, FACEBOOK_BUTTON_SELECTED;
    private Button facebookButton, googleButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Facebook SDK is initialized
        facebookSDKInitialize();
        //Build google client
        buildGoogleClient(googleSignInInitialize());

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
        mGoogleApiClient = new GoogleApiClient.Builder(this)
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
                            //fetchAccessToken(accessToken.getToken());
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
                //fetchAccessToken(facebookAccessToken);
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
    public void onClick(View view) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
