package version1.dishq.dishq.fragments.homeScreenFragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
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
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import version1.dishq.dishq.R;
import version1.dishq.dishq.fragments.bottomSheetFragment.BottomSheetFragment;
import version1.dishq.dishq.fragments.dialogfragment.filters.FiltersDialogFragment;
import version1.dishq.dishq.server.Config;
import version1.dishq.dishq.server.Request.FavDishAddRemHelper;
import version1.dishq.dishq.server.Response.DishDataInfo;
import version1.dishq.dishq.server.RestApi;
import version1.dishq.dishq.ui.AboutUsActivity;
import version1.dishq.dishq.ui.FavouritesActivity;
import version1.dishq.dishq.ui.MenuFinder;
import version1.dishq.dishq.ui.SettingsActivity;
import version1.dishq.dishq.util.DishqApplication;
import version1.dishq.dishq.util.Util;

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

    public HomeScreenFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            dishDataInfo = new DishDataInfo();
            this.dishDataInfo = dishDataInfo.toModel(bundle);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        rlHomeScreen = (RelativeLayout) rootView.findViewById(R.id.rl_home_screen);
        hamburgerButton = (Button) rootView.findViewById(R.id.hamburger);
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

        moodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FiltersDialogFragment dialogFragment = FiltersDialogFragment.getInstance();
                dialogFragment.show(fragmentManager, "filters_dialog_fragment");
            }
        });

        hamburgerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
            String imageUrl = dishDataInfo.getDishPhoto().get(0);
            Picasso.with(DishqApplication.getContext())
                    .load(imageUrl)
                    .into(new Target() {
                        @Override
                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                            rlHomeScreen.setBackground(new BitmapDrawable(bitmap));
                        }

                        @Override
                        public void onBitmapFailed(Drawable errorDrawable) {

                        }

                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) {

                        }
                    });

            frameClick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (drawer.isDrawerOpen(GravityCompat.START)) {
                        drawer.closeDrawer(GravityCompat.START);
                    }
                    Util.setRecipeUrl(dishDataInfo.getRecipeUrl());
                    Util.setGenericDishIdTab(dishDataInfo.getGenericDishId());
                    new BottomSheetFragment().show(getActivity().getSupportFragmentManager(), "dialog");
                }
            });

            dineButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (drawer.isDrawerOpen(GravityCompat.START)) {
                        drawer.closeDrawer(GravityCompat.START);
                    }
                    Util.setRecipeUrl(dishDataInfo.getRecipeUrl());
                    Util.setGenericDishIdTab(dishDataInfo.getGenericDishId());
                    new BottomSheetFragment().show(getActivity().getSupportFragmentManager(), "dialog");

                }
            });

            Boolean isFavourited = dishDataInfo.getAddedToFav();
            if(isFavourited) {
                favButton.setChecked(true);
            }else {
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
                    if(isChecked) {
                        Util.addRemoveDishFromFav(source, genericDishId, 1, TAG);
                    }else {
                        Util.addRemoveDishFromFav(source, genericDishId, 0, TAG);
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
            Intent intent = new Intent(getActivity(), FavouritesActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else if (id == R.id.nav_menufinder) {
            Intent intent = new Intent(getActivity(), MenuFinder.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else if (id == R.id.nav_settings) {
            Intent intent = new Intent(getActivity(), SettingsActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else if (id == R.id.nav_rate) {
            TextView title = new TextView(getActivity());
            title.setText("Like the dishq app?");
            title.setGravity(Gravity.LEFT);
            title.setTextSize(22);
            title.setBackgroundColor(Color.WHITE);
            title.setTextColor(Color.parseColor("#000000"));
            title.setTypeface(Util.opensanslight);
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
            builder.setTitle("Like the dishq app?");
            builder.setMessage("Tell us what you think").setCancelable(true)
                    .setPositiveButton("Love it", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getActivity().getPackageName())));
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
            Intent i = new Intent(android.content.Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(android.content.Intent.EXTRA_SUBJECT, "Check this out");
            i.putExtra(android.content.Intent.EXTRA_TEXT, "Love this foodie app, dishq, it gives personalised recommendations!  http://bit.ly/2bLhPig");
            startActivity(Intent.createChooser(i, "Share via"));
        } else if (id == R.id.nav_about) {
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


}
