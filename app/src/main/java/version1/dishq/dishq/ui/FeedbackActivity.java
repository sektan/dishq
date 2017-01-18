package version1.dishq.dishq.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mixpanel.android.mpmetrics.MixpanelAPI;

import org.json.JSONException;
import org.json.JSONObject;

import version1.dishq.dishq.BaseActivity;
import version1.dishq.dishq.R;
import version1.dishq.dishq.adapters.HorizontalAdapter;
import version1.dishq.dishq.fragments.dialogfragment.filters.FiltersDialogFragment;
import version1.dishq.dishq.util.DishqApplication;
import version1.dishq.dishq.util.Util;

/**
 * Created by dishq on 18-01-2017.
 * Package name version1.dishq.dishq.
 */

public class FeedbackActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener{

    private Button hamButton, moodButton;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private TextView navUserName;
    private MixpanelAPI mixpanel = null;
    private ImageView fbBgImage;
    private RecyclerView recyclerView;
    HorizontalAdapter horizontalAdapter;
    private TextView feedbackQuestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //MixPanel Instantiation
        mixpanel = MixpanelAPI.getInstance(this, getResources().getString(R.string.mixpanel_token));
        setContentView(R.layout.activity_feedback);
        setTags();
    }

    protected void setTags() {
        recyclerView = (RecyclerView) findViewById(R.id.feedback_recyclerview);
        feedbackQuestion = (TextView) findViewById(R.id.feedback_question_text);
        drawer = (DrawerLayout) findViewById(R.id.feedback_drawer_layout);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        hamButton = (Button) findViewById(R.id.feedback_hamburger);
        moodButton = (Button) findViewById(R.id.feedback_mood);
        fbBgImage = (ImageView) findViewById(R.id.feedback_bg_image);
        fbBgImage.setScaleType(ImageView.ScaleType.CENTER_CROP);

        feedbackQuestion.setText(Util.getFeedbackQuestion());

        hamButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    final JSONObject properties = new JSONObject();
                    properties.put("Burger Menu", "homescreen");
                    mixpanel.track("Burger Menu", properties);
                } catch (final JSONException e) {
                    throw new RuntimeException("Could not encode hour of the day in JSON");
                }
                ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(FeedbackActivity.this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
                drawer.setDrawerListener(toggle);
                toggle.syncState();
                navigationView = (NavigationView) findViewById(R.id.feedback_nav_view);
                navUserName = (TextView) findViewById(R.id.nav_user_name);
                if (navUserName != null) {
                    navUserName.setTypeface(Util.opensanslight);
                    navUserName.setText(DishqApplication.getUserName());
                }
                navigationView.setNavigationItemSelectedListener(FeedbackActivity.this);
                drawer.openDrawer(GravityCompat.START);
            }
        });

        moodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    final JSONObject properties = new JSONObject();
                    properties.put("Mood filters on home screen", "homescreen");
                    mixpanel.track("Mood filters on home screen", properties);
                } catch (final JSONException e) {
                    throw new RuntimeException("Could not encode hour of the day in JSON");
                }
                FragmentManager fragmentManager = getSupportFragmentManager();
                FiltersDialogFragment dialogFragment = FiltersDialogFragment.getInstance();
                dialogFragment.show(fragmentManager, "filters_dialog_fragment");
            }
        });

        horizontalAdapter=new HorizontalAdapter(getApplication());

        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(FeedbackActivity.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(horizontalLayoutManager);
        recyclerView.setAdapter(horizontalAdapter);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_fav) {
            try {
                final JSONObject properties = new JSONObject();
                properties.put("Favourites list nav bar", "navbar");
                mixpanel.track("Favourites list nav bar", properties);
            } catch (final JSONException e) {
                throw new RuntimeException("Could not encode hour of the day in JSON");
            }
            Intent intent = new Intent(FeedbackActivity.this, FavouritesActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            finish();
            startActivity(intent);
        } else if (id == R.id.nav_menufinder) {
            try {
                final JSONObject properties = new JSONObject();
                properties.put("Menu Finder in nav bar", "navbar");
                mixpanel.track("Menu Finder in nav bar", properties);
            } catch (final JSONException e) {
                throw new RuntimeException("Could not encode hour of the day in JSON");
            }
            Intent intent = new Intent(FeedbackActivity.this, MenuFinder.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            finish();
            startActivity(intent);
        } else if (id == R.id.nav_settings) {
            try {
                final JSONObject properties = new JSONObject();
                properties.put("Settings in nav bar", "navbar");
                mixpanel.track("Settings in nav bar", properties);
            } catch (final JSONException e) {
                throw new RuntimeException("Could not encode hour of the day in JSON");
            }
            Intent intent = new Intent(FeedbackActivity.this, SettingsActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            finish();
            startActivity(intent);
        } else if (id == R.id.nav_rate) {
            TextView title = new TextView(FeedbackActivity.this);
            title.setText("Like the dishq app?");
            title.setGravity(Gravity.LEFT);
            title.setTextSize(22);
            title.setBackgroundColor(Color.WHITE);
            title.setTextColor(Color.parseColor("#000000"));
            title.setTypeface(Util.opensanslight);
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(FeedbackActivity.this);
            builder.setTitle("Like the dishq app?");
            builder.setMessage("Tell us what you think").setCancelable(true)
                    .setPositiveButton("Love it", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + FeedbackActivity.this.getPackageName())));
                        }
                    });
            builder.setNegativeButton("Not Happy", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Sendemail("App Feedback", "food@dishq.in");
                }
            });
            android.app.AlertDialog alert = builder.create();
            alert.show();
            TextView textView = (TextView) alert.findViewById(android.R.id.message);
            textView.setTextSize(20);
            textView.setTextColor(Color.parseColor("#90000000"));
            textView.setTypeface(Util.opensanssemibold);

        } else if (id == R.id.nav_share) {
            try {
                final JSONObject properties = new JSONObject();
                properties.put("Share in nav bar", "navbar");
                mixpanel.track("Share in nav bar", properties);
            } catch (final JSONException e) {
                throw new RuntimeException("Could not encode hour of the day in JSON");
            }
            Intent i = new Intent(android.content.Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(android.content.Intent.EXTRA_SUBJECT, "Check this out");
            i.putExtra(android.content.Intent.EXTRA_TEXT, "Love this foodie app, dishq, it gives personalised recommendations!  http://bit.ly/2bLhPig");
            startActivity(Intent.createChooser(i, "Share via"));
        } else if (id == R.id.nav_about) {
            Intent intent = new Intent(FeedbackActivity.this, AboutUsActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            finish();
            startActivity(intent);
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void Sendemail(String Subject, String to) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:" + to));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, Subject);
        try {
            startActivity(Intent.createChooser(emailIntent, "Send email using..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(FeedbackActivity.this, "No email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        mixpanel.flush();
        super.onDestroy();
    }
}
