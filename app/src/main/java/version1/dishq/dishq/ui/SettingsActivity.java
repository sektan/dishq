package version1.dishq.dishq.ui;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.plus.Plus;
import com.google.firebase.auth.FirebaseAuth;
import com.mixpanel.android.mpmetrics.MixpanelAPI;

import org.json.JSONException;
import org.json.JSONObject;

import version1.dishq.dishq.R;
import version1.dishq.dishq.util.Constants;
import version1.dishq.dishq.util.DishqApplication;
import version1.dishq.dishq.util.Util;

/**
 * Created by dishq on 8/17/16.
 * Package name version1.dishq.dishq.
 */
public class SettingsActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, ConnectionCallbacks {
    TextView Logout;
    private ImageView backButton;
    TextView toolbarTitle, userName, versionNam;
    GoogleApiClient mGoogleApiClient;
    private MixpanelAPI mixpanel = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //MixPanel Instantiation
        mixpanel = MixpanelAPI.getInstance(this, getResources().getString(R.string.mixpanel_token));

        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_settings);
            Logout = (TextView) findViewById(R.id.fblogout);
            Logout.setTypeface(Util.opensanslight);
            userName = (TextView) findViewById(R.id.user_name);
            userName.setTypeface(Util.opensansregular);
            userName.setText(DishqApplication.getUserName());
            toolbarTitle = (TextView) findViewById(R.id.settings_toolbarTitle);
            toolbarTitle.setTypeface(Util.opensanssemibold);
            backButton = (ImageView) findViewById(R.id.settings_back_button);
            versionNam = (TextView) findViewById(R.id.version_name);
            versionNam.setTypeface(Util.opensansregular);
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = pInfo.versionName;

            versionNam.setText("v" +version);

            backButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Util.setHomeRefreshRequired(false);
                    finish();
                }
            });

            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .requestScopes(new Scope(Scopes.PROFILE))
                    .requestScopes(new Scope(Scopes.PLUS_LOGIN))
                    .requestProfile()
                    .requestEmail()
                    .requestIdToken("1065480470289-fa2sfel9s88t4svp6nee7l1cklol8nda.apps.googleusercontent.com")
                    .build();

            // Build a GoogleApiClient with access to the Google Sign-In API
            // and the options specified by gGoogleSignInOptions.
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .addApi(Plus.API)
                    .build();
            if (getSupportActionBar() != null)
                getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            Logout.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                final JSONObject properties = new JSONObject();
                                properties.put("Logout in settings", "settings");
                                mixpanel.track("Logout in settings", properties);
                            } catch (final JSONException e) {
                                throw new RuntimeException("Could not encode hour of the day in JSON");
                            }
                            Logout();
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void Logout() {
        String facebookOrGoogle = DishqApplication.getFacebookOrGoogle();
        try {
            final JSONObject properties = new JSONObject();
            properties.put("log out", "nav");
        } catch (final JSONException e) {
            throw new RuntimeException("Could not encode hour of the day in JSON");
        }
        if (facebookOrGoogle.equals("facebook")) {
            facebookSignOut();
        } else {
            googleSignOut();
        }
    }

    public void facebookSignOut() {
        FacebookSdk.sdkInitialize(getApplicationContext());
        LoginManager.getInstance().logOut();
        userLogOut();
    }

    public void googleSignOut() {

        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
            mGoogleApiClient.registerConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                @Override
                public void onConnected(@Nullable Bundle bundle) {

                    FirebaseAuth.getInstance().signOut();
                    if (mGoogleApiClient.isConnected()) {
                        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
                            @Override
                            public void onResult(@NonNull Status status) {
                                if (status.isSuccess()) {
                                    Log.d("HomePage", "User Logged out");
                                    userLogOut();
                                }
                            }
                        });
                    }
                }

                @Override
                public void onConnectionSuspended(int i) {
                    Log.d("HomePage", "Google API Client Connection Suspended");
                }
            });
        }
    }

    public void userLogOut() {
        DishqApplication.getPrefs().edit().putString(Constants.ACCESS_TOKEN, "").apply();
        DishqApplication.getPrefs().edit().putString(Constants.REFRESH_TOKEN, "").apply();
        DishqApplication.getPrefs().edit().putString(Constants.TOKEN_TYPE, "").apply();
        DishqApplication.setAccessToken(null, null);
        DishqApplication.getPrefs().edit().clear().apply();
        getApplicationContext().getSharedPreferences("dish_app_prefs", MODE_PRIVATE).edit().clear().apply();
        Intent intent = new Intent(this, SplashActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finish();
        startActivity(intent);

    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    protected void onDestroy() {
        mixpanel.flush();
        super.onDestroy();
    }
}
