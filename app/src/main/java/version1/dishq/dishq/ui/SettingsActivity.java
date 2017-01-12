package version1.dishq.dishq.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.plus.Plus;

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
    Toolbar toolbar;
    String email = "";
    TextView toolbarTitle, user_email;
    GoogleApiClient mGoogleApiClient;
    JSONObject prop = new JSONObject();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_settings);
            Logout = (TextView) findViewById(R.id.fblogout);
            user_email = (TextView) findViewById(R.id.user_email);
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            user_email.setTypeface(Util.opensanslight);
            toolbarTitle = (TextView) findViewById(R.id.toolbarTitle);
            toolbarTitle.setTypeface(Util.opensanslight);
            toolbarTitle.setText("Settings");
            user_email.setText(email);
            setSupportActionBar(toolbar);
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
                            Logout();
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void Logout() {
        FacebookSdk.sdkInitialize(getApplicationContext());
        LoginManager.getInstance().logOut();
        DishqApplication.getPrefs().edit().putString(Constants.ACCESS_TOKEN, "").apply();
        DishqApplication.getPrefs().edit().putString(Constants.REFRESH_TOKEN, "").apply();
        DishqApplication.getPrefs().edit().putString(Constants.TOKEN_TYPE, "").apply();
        DishqApplication.setAccessToken(null, null);
        DishqApplication.getPrefs().edit().clear().apply();
        getApplicationContext().getSharedPreferences("dish_app_prefs", MODE_PRIVATE).edit().clear().apply();
        Intent intent = new Intent(this, SplashActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        this.startActivity(intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //Write your logic here
                this.finish();
                try {
                    prop.put("Screen", "settings");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
}
