package version1.dishq.dishq.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import version1.dishq.dishq.BaseActivity;
import version1.dishq.dishq.R;
import version1.dishq.dishq.adapters.menuAdapters.DineoutMenuMasonryAdapter;
import version1.dishq.dishq.server.Config;
import version1.dishq.dishq.server.Response.DineoutMenuResponse;
import version1.dishq.dishq.server.RestApi;
import version1.dishq.dishq.util.DishqApplication;
import version1.dishq.dishq.util.Util;

import static version1.dishq.dishq.util.DishqApplication.getContext;

/**
 * Created by dishq on 09-01-2017.
 * Package name version1.dishq.dishq.
 */

public class DineoutMenuActivity extends BaseActivity {

    private static final String TAG = "DineoutMenuActivity";
    private RecyclerView dineMenuRecyclerView;
    private RecyclerView.LayoutManager recyclerViewLayoutManager;
    private ImageView backButton, deliveryButton;
    private TextView dineMenuHeader;
    private TextView dineMenuRestType, dineMenuRp1, dineMenuRp2, dineMenuRp3,
            dineMenuRp4, dineMenuDrive;
    private TextView dineMenuTags, dineMenuRestAdd;
    private Button call, directions;
    private RelativeLayout rlDineoutToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dineout_menu);
        fetchDineoutMenuInfo();

        final Toolbar toolbar = (Toolbar) findViewById(R.id.dinemenu_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapse_toolbar);
        collapsingToolbar.setTitle("My Toolbar Title");
    }

    protected void setTags() {
        dineMenuRecyclerView = (RecyclerView) findViewById(R.id.dine_menu_recyclerview);
        backButton = (ImageView) findViewById(R.id.dine_menu_back_button);
        dineMenuHeader = (TextView) findViewById(R.id.dinemenu_toolbarTitle);
        dineMenuHeader.setTypeface(Util.opensanssemibold);
        deliveryButton = (ImageView) findViewById(R.id.delivery_option);
        dineMenuRestType = (TextView) findViewById(R.id.dinemenu_rest_type_text);
        dineMenuRestType.setTypeface(Util.opensansregular);
        dineMenuRp1 = (TextView) findViewById(R.id.dinemenu_rup_1);
        dineMenuRp1.setTypeface(Util.opensanssemibold);
        dineMenuRp2 = (TextView) findViewById(R.id.dinemenu_rup_2);
        dineMenuRp3 = (TextView) findViewById(R.id.dinemenu_rup_3);
        dineMenuRp4 = (TextView) findViewById(R.id.dinemenu_rup_4);
        dineMenuDrive = (TextView) findViewById(R.id.dinemenu_drive_time);
        dineMenuDrive.setTypeface(Util.opensansregular);
        dineMenuTags = (TextView) findViewById(R.id.dinemenu_tags);
        dineMenuTags.setTypeface(Util.opensanssemibold);
        dineMenuRestAdd = (TextView) findViewById(R.id.dinemenu_rest_addr);
        dineMenuRestAdd.setTypeface(Util.opensansregular);
        call = (Button) findViewById(R.id.dinemenu_call);
        call.setTypeface(Util.opensanssemibold);
        directions = (Button) findViewById(R.id.dinemenu_directions);
        directions.setTypeface(Util.opensanssemibold);
        rlDineoutToolbar = (RelativeLayout) findViewById(R.id.rl_dinemenu_toolbar);
        setFunctionality();
    }

    protected void setFunctionality() {

        String imageUrl = Util.dineoutRestData.getDineRestPhoto().get(0);
        Picasso.with(getContext())
                .load(imageUrl)
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        rlDineoutToolbar.setBackground(new BitmapDrawable(bitmap));
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Util.setHomeRefreshRequired(false);
                finish();
            }
        });

        if (Util.dineoutRestData != null) {
            dineMenuHeader.setText(Util.dineoutRestData.getDineMenuRestName());
            deliveryButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(DineoutMenuActivity.this, DeliveryMenuActivity.class);
                    overridePendingTransition(R.anim.from_middle, R.anim.to_middle);
                    startActivity(intent);
                    finish();
                }
            });

            StringBuilder sb = new StringBuilder();

            if(Util.dineoutRestData.getDineMenuCusineText()!=null) {
                for (String s : Util.dineoutRestData.getDineMenuCusineText()) {
                    if (sb.length() > 0) {
                        sb.append(',' + " ");
                    }
                    sb.append(s);

                }
            }
            String dishTypeText = sb.toString();
            dineMenuRestType.setText(String.valueOf(dishTypeText));

            int dinePriceLvl = Util.dineoutRestData.getDineMenuPriceLvl();
            if (dinePriceLvl == 1) {
                dineMenuRp1.setTextColor(ContextCompat.getColor(DishqApplication.getContext(), R.color.rupeeGreen));
            } else if (dinePriceLvl == 2) {
                dineMenuRp1.setTextColor(ContextCompat.getColor(DishqApplication.getContext(), R.color.rupeeGreen));
                dineMenuRp2.setTextColor(ContextCompat.getColor(DishqApplication.getContext(), R.color.rupeeGreen));
            } else if (dinePriceLvl == 3) {
                dineMenuRp1.setTextColor(ContextCompat.getColor(DishqApplication.getContext(), R.color.rupeeGreen));
                dineMenuRp2.setTextColor(ContextCompat.getColor(DishqApplication.getContext(), R.color.rupeeGreen));
                dineMenuRp3.setTextColor(ContextCompat.getColor(DishqApplication.getContext(), R.color.rupeeGreen));
            } else if (dinePriceLvl == 4) {
                dineMenuRp1.setTextColor(ContextCompat.getColor(DishqApplication.getContext(), R.color.rupeeGreen));
                dineMenuRp2.setTextColor(ContextCompat.getColor(DishqApplication.getContext(), R.color.rupeeGreen));
                dineMenuRp3.setTextColor(ContextCompat.getColor(DishqApplication.getContext(), R.color.rupeeGreen));
                dineMenuRp4.setTextColor(ContextCompat.getColor(DishqApplication.getContext(), R.color.rupeeGreen));
            }
            dineMenuDrive.setText(Util.dineoutRestData.getDineMenuDriveTime());

            String dishTags = "";
            if(Util.dineoutRestData.getDineMenuRestTags()!=null) {
                for (String s : Util.dineoutRestData.getDineMenuRestTags()) {
                    dishTags += s + " ";
                }
            }
            dineMenuTags.setText(String.valueOf(dishTags));

            String dineRestAddress = "";
            if(Util.dineoutRestData.getDineMenuRestAddr()!=null) {
                for (String s : Util.dineoutRestData.getDineMenuRestAddr()) {
                    dineRestAddress += s;
                }
            }
            dineMenuRestAdd.setText(dineRestAddress);

            final String finalDineRestContactNo = Util.dineoutRestData.getDineMenuRestContNo().get(0);
            call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String uri = "tel:" + finalDineRestContactNo.trim() ;
                    Uri number = Uri.parse(uri);
                    Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
                    startActivity(callIntent);
                }
            });

            StringTokenizer tokens = new StringTokenizer(Util.dineoutRestData.getDineMenuRestLatLong(), ",");
            final String targetLat = tokens.nextToken();// this will contain latitude
            final String targetLang = tokens.nextToken();// this will contain longitude

            directions.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String currentLatitude = "12.92923258";
                    String currentLongitude = "77.63082482";

                    String url = "http://maps.google.com/maps?saddr="+currentLatitude+","+currentLongitude+"&daddr="+targetLat+","+targetLang+"&mode=driving";
                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(url));
                    intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                    startActivity(intent);
                }
            });
        }
    }

    private void fetchDineoutMenuInfo() {
        String latitude = "12.92923258", longitude = "77.63082482", source = "dineoutmenu";
        RestApi restApi = Config.createService(RestApi.class);
        Call<DineoutMenuResponse> call = restApi.getDineoutMenuOptions(DishqApplication.getAccessToken(),
                Util.getDineRestId(), DishqApplication.getUniqueID(), source, latitude, longitude,
                Util.getGenericDishIdTab());
        call.enqueue(new Callback<DineoutMenuResponse>() {
            @Override
            public void onResponse(Call<DineoutMenuResponse> call, Response<DineoutMenuResponse> response) {
                Log.d(TAG, "Success");
                try {
                    if(response.isSuccessful()) {
                        Log.d(TAG, "Response is successful");
                        DineoutMenuResponse.DineoutMenuInfo body = response.body().dineoutMenuInfo;
                        if(body!=null) {
                            Log.d(TAG, "body is nt null");
                            Util.dineoutMenuInfos.clear();
                            Util.dineoutRestData = body.dineoutRestData;
                            for (int i = 0; i<body.dineoutMenuDatas.size(); i++) {
                                Util.dineoutMenuInfos = body.dineoutMenuDatas;
                            }
                            setTags();
                            recyclerViewLayoutManager = new StaggeredGridLayoutManager
                                    (2, StaggeredGridLayoutManager.VERTICAL);
                            Util.SpacesItemDecoration decoration = new Util.SpacesItemDecoration(25);
                            dineMenuRecyclerView.setLayoutManager(recyclerViewLayoutManager);
                            dineMenuRecyclerView.addItemDecoration(decoration);
                            dineMenuRecyclerView.setNestedScrollingEnabled(false);
                            DineoutMenuMasonryAdapter masonryAdapter = new DineoutMenuMasonryAdapter(DineoutMenuActivity.this);
                            dineMenuRecyclerView.setAdapter(masonryAdapter);
                        }
                    }else {
                        String error = response.errorBody().string();
                        Log.d(TAG, "response error: " +error);
                    }
                }catch (IOException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<DineoutMenuResponse> call, Throwable t) {
                Log.d(TAG, "Failure");
            }
        });
    }
}
