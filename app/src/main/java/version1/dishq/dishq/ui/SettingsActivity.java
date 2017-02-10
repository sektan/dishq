package version1.dishq.dishq.ui;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
    private Button logoutButton;
    private ImageView backButton;
    TextView toolbarTitle, userName, versionNam;
    private GoogleApiClient googleApiClient;
    private MixpanelAPI mixpanel = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //MixPanel Instantiation
        mixpanel = MixpanelAPI.getInstance(this, getResources().getString(R.string.mixpanel_token));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        logoutButton = (Button) findViewById(R.id.fblogout);
        logoutButton.setTypeface(Util.opensanslight);
        userName = (TextView) findViewById(R.id.user_name);
        userName.setTypeface(Util.opensansregular);
        userName.setText(DishqApplication.getUserName());
        toolbarTitle = (TextView) findViewById(R.id.settings_toolbarTitle);
        toolbarTitle.setTypeface(Util.opensanssemibold);
        backButton = (ImageView) findViewById(R.id.settings_back_button);
        versionNam = (TextView) findViewById(R.id.version_name);
        versionNam.setTypeface(Util.opensansregular);
        PackageInfo pInfo = null;
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        assert pInfo != null;
        String version = pInfo.versionName;

        versionNam.setText("v" + version);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Util.setHomeRefreshRequired(false);
                finish();
            }
        });

        String serverClientId = DishqApplication.getContext().getString(R.string.server_client_id);
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestScopes(new Scope(Scopes.DRIVE_APPFOLDER))
                .requestServerAuthCode(serverClientId, false)
                .requestIdToken(serverClientId)
                .build();

        // [START build_client]
        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .addOnConnectionFailedListener(this)
                .build();
        // [END build_client]

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    final JSONObject properties = new JSONObject();
                    properties.put("Logout in settings", "settings");
                    mixpanel.track("Logout in settings", properties);
                } catch (final JSONException e) {
                    throw new RuntimeException("Could not encode hour of the day in JSON");
                }
                Util.setMoodFilterId(-1);
                Util.setMoodName("");
                Util.setFilterName("");
                Util.setFilterClassName("");
                Util.setFilterEntityId(-1);
                Util.setQuickFilterPosition(-1);
                Util.setMoodPosition(-1);
                Util.setHomeRefreshRequired(true);
                Logout();
            }
        });
    }

    public void Logout() {
        try {
            final JSONObject properties = new JSONObject();
            properties.put("log out", "nav");
            mixpanel.track("log out", properties);
        } catch (final JSONException e) {
            throw new RuntimeException("Could not encode hour of the day in JSON");
        }
        if (DishqApplication.getFacebookOrGoogle().equals("facebook")) {
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

        if (googleApiClient != null) {
            googleApiClient.connect();
            googleApiClient.registerConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                @Override
                public void onConnected(@Nullable Bundle bundle) {

                    FirebaseAuth.getInstance().signOut();
                    if (googleApiClient.isConnected()) {
                        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
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
        Intent intent = new Intent(SettingsActivity.this, SplashActivity.class);
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
