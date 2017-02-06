package version1.dishq.dishq.fragments.homeScreenFragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.mixpanel.android.mpmetrics.MixpanelAPI;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Target;

import org.json.JSONException;
import org.json.JSONObject;

import version1.dishq.dishq.R;
import version1.dishq.dishq.custom.FontsOverride;
import version1.dishq.dishq.fragments.bottomSheetFragment.BottomSheetFragment;
import version1.dishq.dishq.fragments.dialogfragment.filters.FiltersDialogFragment;
import version1.dishq.dishq.server.Response.DishDataInfo;
import version1.dishq.dishq.ui.AboutUsActivity;
import version1.dishq.dishq.ui.FavouritesActivity;
import version1.dishq.dishq.ui.MenuFinder;
import version1.dishq.dishq.ui.SettingsActivity;
import version1.dishq.dishq.util.DishqApplication;
import version1.dishq.dishq.util.Util;

import static version1.dishq.dishq.ui.HomeActivity.viewPager;

/**
 * Created by dishq on 27-12-2016.
 * Package name version1.dishq.dishq.
 */

public class HomeScreenFragment extends Fragment implements NavigationView.OnNavigationItemSelectedListener {

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String TAG = "HomeScreenFragment";
    DishDataInfo dishDataInfo;
    private RelativeLayout rlHomeScreen;
    private ImageView vegTag, eggTag, nonVegTag, isSpicyTag, hasAlcoholTag;
    private TextView dishName, dishType, navUserName;
    private Button foodTags, dineButton, hamburgerButton, moodButton;
    private ToggleButton favButton;
    private FrameLayout frameClick;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private MixpanelAPI mixpanel = null;
    ImageView homeBgImage;
    private Button moodFilterText;
    private FrameLayout goingToNextCard;
    private ImageView navBarBg;
    private boolean networkFailed;
    private Button noCoverageBanner;

    public HomeScreenFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //MixPanel Instantiation
        mixpanel = MixpanelAPI.getInstance(getActivity(), getResources().getString(R.string.mixpanel_token));
        FontsOverride.setDefaultFont(getActivity(), "MONOSPACE", "opensanssemibold.ttf");
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            dishDataInfo = new DishDataInfo();
            this.dishDataInfo = dishDataInfo.toModel(bundle);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(dineButton!=null) {
            dineButton.setEnabled(true);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(dineButton!=null) {
            dineButton.setEnabled(false);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        goingToNextCard = (FrameLayout) rootView.findViewById(R.id.home_frame_new_card);
        rlHomeScreen = (RelativeLayout) rootView.findViewById(R.id.rl_home_screen);
        homeBgImage = (ImageView) rootView.findViewById(R.id.home_screen_bg_image);
        homeBgImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
        hamburgerButton = (Button) rootView.findViewById(R.id.hamburger);
        moodFilterText = (Button) rootView.findViewById(R.id.mood_filter);
        moodFilterText.setTypeface(Util.opensansregular);
        moodButton = (Button) rootView.findViewById(R.id.mood);
        vegTag = (ImageView) rootView.findViewById(R.id.veg_tag);
        eggTag = (ImageView) rootView.findViewById(R.id.egg_tag);
        nonVegTag = (ImageView) rootView.findViewById(R.id.non_veg_tag);
        isSpicyTag = (ImageView) rootView.findViewById(R.id.spicy_tag);
        hasAlcoholTag = (ImageView) rootView.findViewById(R.id.alcohol_tag);
        dishName = (TextView) rootView.findViewById(R.id.dish_name);
        dishType = (TextView) rootView.findViewById(R.id.dish_type);
        foodTags = (Button) rootView.findViewById(R.id.food_tags);
        favButton = (ToggleButton) rootView.findViewById(R.id.favourites);
        dineButton = (Button) rootView.findViewById(R.id.dining);
        frameClick = (FrameLayout) rootView.findViewById(R.id.frame_click);
        drawer = (DrawerLayout) rootView.findViewById(R.id.drawer_layout);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        noCoverageBanner = (Button) rootView.findViewById(R.id.no_coverage_banner);
        noCoverageBanner.setTypeface(Util.opensansregular);

        Boolean isNotEmpty = false;
        String moodText;
        if(!Util.getMoodName().equals("")) {
            isNotEmpty = true;
            moodFilterText.setVisibility(View.VISIBLE);
        }else if(!Util.getFilterName().equals("")) {
            isNotEmpty = true;
            moodFilterText.setVisibility(View.VISIBLE);
        }

        if(isNotEmpty) {
            if(Util.getFilterName().equals("")) {
                moodText = Util.getMoodName();
            }else if(Util.getMoodName().equals("")) {
                moodText = Util.getFilterName();
            }else {
                moodText = Util.getMoodName() + " , " + Util.getFilterName();
            }
            moodFilterText.setText(moodText);
        }

        if(Util.getShowBanner()) {
            if(isNotEmpty) {
                noCoverageBanner.setVisibility(View.GONE);
            }else {
                noCoverageBanner.setVisibility(View.VISIBLE);
                noCoverageBanner.setText(Util.getBannerText());
            }
        }else {
            noCoverageBanner.setVisibility(View.GONE);
        }

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
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FiltersDialogFragment dialogFragment = FiltersDialogFragment.getInstance();
                dialogFragment.show(fragmentManager, "filters_dialog_fragment");
            }
        });

        hamburgerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    final JSONObject properties = new JSONObject();
                    properties.put("Burger Menu", "homescreen");
                    mixpanel.track("Burger Menu", properties);
                } catch (final JSONException e) {
                    throw new RuntimeException("Could not encode hour of the day in JSON");
                }
                ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(getActivity(), drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
                drawer.setDrawerListener(toggle);
                toggle.syncState();

                navigationView = (NavigationView) rootView.findViewById(R.id.nav_view);
                navUserName = (TextView) rootView.findViewById(R.id.nav_user_name);
                if (navUserName != null) {
                    navUserName.setTypeface(Util.opensanslight);
                    navUserName.setText(DishqApplication.getUserName());
                }
                navigationView.setNavigationItemSelectedListener(HomeScreenFragment.this);
                drawer.openDrawer(GravityCompat.START);
            }
        });

        setFont();

        if (dishDataInfo != null) {
            Picasso.with(DishqApplication.getContext())
                    .load(dishDataInfo.getDishPhoto().get(0))
                    .fit()
                    .centerCrop()
                    .into(homeBgImage);

//            frameClick.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    try {
//                        final JSONObject properties = new JSONObject();
//                        properties.put("Dineoptions nonbutton home screen", "homescreen");
//                        mixpanel.track("Dineoptions nonbutton home screen", properties);
//                    } catch (final JSONException e) {
//                        throw new RuntimeException("Could not encode hour of the day in JSON");
//                    }
//                    if (drawer.isDrawerOpen(GravityCompat.START)) {
//                        drawer.closeDrawer(GravityCompat.START);
//                    }
//                    Util.setRecipeUrl(dishDataInfo.getRecipeUrl());
//                    Util.setGenericDishIdTab(dishDataInfo.getGenericDishId());
//                    Util.setCurrentPage(viewPager.getCurrentItem());
//                    new BottomSheetFragment().show(getActivity().getSupportFragmentManager(), "dialog");
//                }
//            });

            dineButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        final JSONObject properties = new JSONObject();
                        properties.put("Dineoptions button home screen", "homescreen");
                        mixpanel.track("Dineoptions button home screen", properties);
                    } catch (final JSONException e) {
                        throw new RuntimeException("Could not encode hour of the day in JSON");
                    }
                    if (drawer.isDrawerOpen(GravityCompat.START)) {
                        drawer.closeDrawer(GravityCompat.START);
                    }
                    Util.setRecipeUrl(dishDataInfo.getRecipeUrl());
                    Util.setGenericDishIdTab(dishDataInfo.getGenericDishId());
                    Util.setCurrentPage(viewPager.getCurrentItem());

                    new BottomSheetFragment().show(getActivity().getSupportFragmentManager(), "dialog");

                }
            });

            Boolean isFavourited = dishDataInfo.getAddedToFav();

            if (isFavourited) {
                favButton.setChecked(true);
            } else {
                favButton.setChecked(false);
            }

            favButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    if (drawer.isDrawerOpen(GravityCompat.START)) {
                        drawer.closeDrawer(GravityCompat.START);
                    }
                    String source = "homescreen";
                    int genericDishId = dishDataInfo.getGenericDishId();
                    if (isChecked) {
                        try {
                            final JSONObject properties = new JSONObject();
                            properties.put("Favourites button home screen", "homescreen");
                            mixpanel.track("Favourites button home screen", properties);
                        } catch (final JSONException e) {
                            throw new RuntimeException("Could not encode hour of the day in JSON");
                        }
                        checkInternetConnection(source, genericDishId, 1, TAG);
                    } else {
                        checkInternetConnection(source, genericDishId, 0, TAG);
                    }
                }
            });

            dishName.setText(String.valueOf(dishDataInfo.getDishName()));

            Boolean isSpicy = dishDataInfo.getSpicy();
            Boolean hasAlcohol = dishDataInfo.getHasAlcohol();
            int homeDishNature = dishDataInfo.getHomeDishNature();
            if (homeDishNature == 1) {
                eggTag.setVisibility(View.GONE);
                vegTag.setVisibility(View.VISIBLE);
                nonVegTag.setVisibility(View.GONE);

            } else if (homeDishNature == 2) {
                eggTag.setVisibility(View.VISIBLE);
                vegTag.setVisibility(View.GONE);
                nonVegTag.setVisibility(View.GONE);
            } else {
                eggTag.setVisibility(View.GONE);
                vegTag.setVisibility(View.GONE);
                nonVegTag.setVisibility(View.VISIBLE);
            }

            if (isSpicy) {
                isSpicyTag.setVisibility(View.VISIBLE);
            } else {
                isSpicyTag.setVisibility(View.GONE);
            }

            if (hasAlcohol) {
                hasAlcoholTag.setVisibility(View.VISIBLE);
            } else {
                hasAlcoholTag.setVisibility(View.GONE);
            }

            StringBuilder sb = new StringBuilder();
            if (dishDataInfo.getCuisineText() != null) {
                for (String s : dishDataInfo.getCuisineText()) {
                    if (sb.length() > 0) {
                        sb.append(',' + " ");
                    }
                    sb.append(s);
                }
            }
            String dishTypeText = sb.toString();
            dishType.setText(String.valueOf(dishTypeText));
            String dishTags = "";
            if (dishDataInfo.getTags() != null) {
                for (String s : dishDataInfo.getTags()) {
                    dishTags += s + " " + " ";
                }
            }
            foodTags.setText(String.valueOf(dishTags));
        }
        return rootView;
    }

    protected void setFont() {
        dishName.setTypeface(Util.opensansregular);
        dishType.setTypeface(Util.opensansregular);
        foodTags.setTypeface(Util.opensanslight);
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
            Util.setCurrentPage(viewPager.getCurrentItem());
            Intent intent = new Intent(getActivity(), FavouritesActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else if (id == R.id.nav_menufinder) {
            try {
                final JSONObject properties = new JSONObject();
                properties.put("Menu Finder in nav bar", "navbar");
                mixpanel.track("Menu Finder in nav bar", properties);
            } catch (final JSONException e) {
                throw new RuntimeException("Could not encode hour of the day in JSON");
            }
            Util.setCurrentPage(viewPager.getCurrentItem());
            Intent intent = new Intent(getActivity(), MenuFinder.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else if (id == R.id.nav_settings) {
            try {
                final JSONObject properties = new JSONObject();
                properties.put("Settings in nav bar", "navbar");
                mixpanel.track("Settings in nav bar", properties);
            } catch (final JSONException e) {
                throw new RuntimeException("Could not encode hour of the day in JSON");
            }
            Util.setCurrentPage(viewPager.getCurrentItem());
            Intent intent = new Intent(getActivity(), SettingsActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else if (id == R.id.nav_rate) {
            TextView title = new TextView(getActivity());
            title.setText("Like the dishq app?");
            title.setGravity(Gravity.START);
            title.setTextSize(22);
            title.setBackgroundColor(Color.WHITE);
            title.setTextColor(Color.parseColor("#000000"));
            title.setTypeface(Util.opensanslight);
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
            builder.setTitle("Like the dishq app?");
            builder.setMessage("").setCancelable(true)
                    .setPositiveButton("Love it", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getActivity().getPackageName())));
                        }
                    });
            builder.setNegativeButton("Could Be Better", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Sendemail("App Feedback", "help@dishq.in");
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
            Util.setCurrentPage(viewPager.getCurrentItem());
            Intent intent = new Intent(getActivity(), AboutUsActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
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
            Toast.makeText(getActivity(), "No email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroy() {
        mixpanel.flush();
        super.onDestroy();
    }

    //Method to check if the internet is connected or not
    private void checkInternetConnection(String source, int genericDishId, int addRemove, String tag) {
        SharedPreferences settings;
        final String PREFS_NAME = "MyPrefsFile";
        settings = getActivity().getSharedPreferences(PREFS_NAME, 0);

        if (settings.getBoolean("android_M", true)) {
            //the app is being launched for first time, do something
            Log.d("Comments", " android_M");
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // only for gingerbread and newer versions
                checkNetwork(source, genericDishId, addRemove, tag);
            } else {
                checkNetwork(source, genericDishId, addRemove, tag);
            }
            settings.edit().putBoolean("android_M", false).apply();
        } else {
            checkNetwork(source, genericDishId, addRemove, tag);
        }
    }

    //Check for internet
    private void checkNetwork(String source, int genericDishId, int addRemove, String tag) {
        if (!Util.checkAndShowNetworkPopup(getActivity())) {
            //Check for version
            Log.d(TAG, "Checking for GPS");
            //Check for gps
            Util.addRemoveDishFromFav(source, genericDishId, addRemove, tag);

        } else {
            networkFailed = true;
        }
    }

}
