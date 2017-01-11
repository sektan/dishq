package version1.dishq.dishq.ui;

import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import version1.dishq.dishq.BaseActivity;
import version1.dishq.dishq.R;
import version1.dishq.dishq.adapters.menuAdapters.DeliveryMenuMasonryAdapter;
import version1.dishq.dishq.server.Config;
import version1.dishq.dishq.server.Response.DeliveryMenuResponse;
import version1.dishq.dishq.server.RestApi;
import version1.dishq.dishq.util.DishqApplication;
import version1.dishq.dishq.util.Util;

/**
 * Created by dishq on 09-01-2017.
 * Package name version1.dishq.dishq.
 */

public class DeliveryMenuActivity extends BaseActivity {

    private static final String TAG = "DeliveryMenuActivity";
    private RecyclerView delMenuRecyclerView;
    private RecyclerView.LayoutManager recyclerViewLayoutManager;
    private ImageView backButton, dineoutButton;
    private TextView delMenuHeader;
    private TextView delMenuRestType, delMenuRp1, delMenuRp2, delMenuRp3,
            delMenuRp4, delMenuDrive;
    private TextView delMenuTags, delMenuRestAdd;
    private TextView orderFrom;
    private TextView swiggy, foodpanda, runnr, zomato;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_menu);
        fetchDeliveryMenuInfo();
        final Toolbar toolbar = (Toolbar) findViewById(R.id.delmenu_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapse_toolbar);
        collapsingToolbar.setTitle("My Toolbar Tittle");

    }

    protected void setTags() {
        delMenuRecyclerView = (RecyclerView) findViewById(R.id.del_menu_recyclerview);
        backButton = (ImageView) findViewById(R.id.del_menu_back_button);
        delMenuHeader = (TextView) findViewById(R.id.delmenu_toolbarTitle);
        delMenuHeader.setTypeface(Util.opensanssemibold);
        dineoutButton = (ImageView) findViewById(R.id.dineout_option);
        delMenuRestType = (TextView) findViewById(R.id.delmenu_rest_type_text);
        delMenuRestType.setTypeface(Util.opensansregular);
        delMenuRp1 = (TextView) findViewById(R.id.delmenu_rup_1);
        delMenuRp1.setTypeface(Util.opensanssemibold);
        delMenuRp2 = (TextView) findViewById(R.id.delmenu_rup_2);
        delMenuRp3 = (TextView) findViewById(R.id.delmenu_rup_3);
        delMenuRp4 = (TextView) findViewById(R.id.delmenu_rup_4);
        delMenuDrive = (TextView) findViewById(R.id.delmenu_drive_time);
        delMenuDrive.setTypeface(Util.opensansregular);
        delMenuTags = (TextView) findViewById(R.id.delmenu_tags);
        delMenuTags.setTypeface(Util.opensanssemibold);
        delMenuRestAdd = (TextView) findViewById(R.id.delmenu_rest_addr);
        delMenuRestAdd.setTypeface(Util.opensansregular);
        swiggy = (TextView) findViewById(R.id.swiggy_text);
        orderFrom = (TextView) findViewById(R.id.delmenu_order_text);
        orderFrom.setTypeface(Util.opensansregular);
        swiggy = (TextView) findViewById(R.id.swiggy_text);
        swiggy.setTypeface(Util.opensansregular);
        foodpanda = (TextView) findViewById(R.id.foodpanda_text);
        foodpanda.setTypeface(Util.opensansregular);
        runnr = (TextView) findViewById(R.id.runnr_text);
        runnr.setTypeface(Util.opensansregular);
        zomato = (TextView) findViewById(R.id.zomato_text);
        zomato.setTypeface(Util.opensansregular);
        setFunctionality();
    }

    protected void setFunctionality() {
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Util.setHomeRefreshRequired(false);
                finish();
            }
        });

        if (Util.deliveryRestData != null) {
            delMenuHeader.setText(Util.deliveryRestData.getDelMenuRestName());
            dineoutButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(DeliveryMenuActivity.this, DineoutMenuActivity.class);
                    overridePendingTransition(R.anim.from_middle, R.anim.to_middle);
                    startActivity(intent);
                    finish();
                }
            });

            StringBuilder sb = new StringBuilder();

            if (Util.deliveryRestData.getDelMenuCusineText() != null) {
                for (String s : Util.deliveryRestData.getDelMenuCusineText()) {
                    if (sb.length() > 0) {
                        sb.append(',' + " ");
                    }
                    sb.append(s);

                }
            }
            String dishTypeText = sb.toString();
            delMenuRestType.setText(String.valueOf(dishTypeText));

            int dinePriceLvl = Util.deliveryRestData.getDelMenuPriceLvl();
            if (dinePriceLvl == 1) {
                delMenuRp1.setTextColor(ContextCompat.getColor(DishqApplication.getContext(), R.color.rupeeGreen));
            } else if (dinePriceLvl == 2) {
                delMenuRp1.setTextColor(ContextCompat.getColor(DishqApplication.getContext(), R.color.rupeeGreen));
                delMenuRp2.setTextColor(ContextCompat.getColor(DishqApplication.getContext(), R.color.rupeeGreen));
            } else if (dinePriceLvl == 3) {
                delMenuRp1.setTextColor(ContextCompat.getColor(DishqApplication.getContext(), R.color.rupeeGreen));
                delMenuRp2.setTextColor(ContextCompat.getColor(DishqApplication.getContext(), R.color.rupeeGreen));
                delMenuRp3.setTextColor(ContextCompat.getColor(DishqApplication.getContext(), R.color.rupeeGreen));
            } else if (dinePriceLvl == 4) {
                delMenuRp1.setTextColor(ContextCompat.getColor(DishqApplication.getContext(), R.color.rupeeGreen));
                delMenuRp2.setTextColor(ContextCompat.getColor(DishqApplication.getContext(), R.color.rupeeGreen));
                delMenuRp3.setTextColor(ContextCompat.getColor(DishqApplication.getContext(), R.color.rupeeGreen));
                delMenuRp4.setTextColor(ContextCompat.getColor(DishqApplication.getContext(), R.color.rupeeGreen));
            }
            delMenuDrive.setText(Util.deliveryRestData.getDelMenuDriveTime());

            String dishTags = "";
            if (Util.deliveryRestData.getDelMenuTags() != null) {
                for (String s : Util.deliveryRestData.getDelMenuTags()) {
                    dishTags += s + " ";
                }
            }
            delMenuTags.setText(String.valueOf(dishTags));

            String dineRestAddress = "";
            if (Util.deliveryRestData.getDelMenuRestAddr() != null) {
                for (String s : Util.deliveryRestData.getDelMenuRestAddr())
                    dineRestAddress += s;
                delMenuRestAdd.setText(dineRestAddress);
            }
        }

        if(Util.deliveryRestData.getDelMenuRunnrUrl()!=null) {
            runnr.setVisibility(View.VISIBLE);
            runnr.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String runnrUrl = "";
                    for (String s : Util.deliveryRestData.getDelMenuRunnrUrl()) {
                        runnrUrl += s;
                    }
                    Intent shareIntent = new Intent();
                    shareIntent.setAction(Intent.ACTION_SEND);
                    shareIntent.setData(Uri.parse(runnrUrl));
                    shareIntent.setType("*/*");
                    startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.chooser_title)));
                }
            });
        }else {
            runnr.setVisibility(View.GONE);
        }

        if(Util.deliveryRestData.getDelMenuFoodPandaUrl()!=null) {
            foodpanda.setVisibility(View.VISIBLE);
            foodpanda.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String foodpandaUrl = "";
                    for (String s : Util.deliveryRestData.getDelMenuFoodPandaUrl()) {
                        foodpandaUrl += s;
                    }

                    Intent shareIntent = new Intent();
                    shareIntent.setAction(Intent.ACTION_SEND);
                    shareIntent.setData(Uri.parse(foodpandaUrl));
                    shareIntent.setType("*/*");
                    startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.chooser_title)));
                }
            });
        }else {
            foodpanda.setVisibility(View.GONE);
        }

        if(Util.deliveryRestData.getDelMenuZomatoUrl()!=null) {
            zomato.setVisibility(View.VISIBLE);
            zomato.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String zomatoUrl = "";
                    for (String s : Util.deliveryRestData.getDelMenuZomatoUrl()) {
                        zomatoUrl += s;
                    }
                    Intent shareIntent = new Intent();
                    shareIntent.setAction(Intent.ACTION_SEND);
                    shareIntent.setData(Uri.parse(zomatoUrl));
                    shareIntent.setType("*/*");
                    startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.chooser_title)));
                }
            });
        }else {
            zomato.setVisibility(View.GONE);
        }

        if (Util.deliveryRestData.getDelMenuSwiggyUrl()!=null) {
            swiggy.setVisibility(View.VISIBLE);
            swiggy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String swiggyUrl = "";
                    for (String s : Util.deliveryRestData.getDelMenuSwiggyUrl()) {
                        swiggyUrl += s;
                    }
                    Intent shareIntent = new Intent();
                    shareIntent.setAction(Intent.ACTION_SEND);
                    shareIntent.setData(Uri.parse(swiggyUrl));
                    shareIntent.setType("*/*");
                    startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.chooser_title)));
                }
            });
        }else {
            swiggy.setVisibility(View.GONE);
        }
    }

    private void fetchDeliveryMenuInfo() {
        String latitude = "12.92923258", longitude = "77.63082482", source = "deliverymenu";
        RestApi restApi = Config.createService(RestApi.class);
        Call<DeliveryMenuResponse> call = restApi.getDeliveryMenuOptions(DishqApplication.getAccessToken(),
                Util.getDelRestId(), DishqApplication.getUniqueID(), source, latitude, longitude,
                Util.getGenericDishIdTab());
        call.enqueue(new Callback<DeliveryMenuResponse>() {
            @Override
            public void onResponse(Call<DeliveryMenuResponse> call, Response<DeliveryMenuResponse> response) {
                Log.d(TAG, "Success");
                try {
                    if (response.isSuccessful()) {
                        Log.d(TAG, "response is successful");
                        DeliveryMenuResponse.DeliveryMenuInfo body = response.body().deliveryMenuInfo;
                        if (body != null) {
                            Log.d(TAG, "body is not null");
                            Util.deliveryMenuInfos.clear();
                            Util.deliveryRestData = body.deliveryRestData;
                            for (int i = 0; i < body.deliveryMenuDatas.size(); i++) {
                                Util.deliveryMenuInfos = body.deliveryMenuDatas;
                            }
                            setTags();
                            recyclerViewLayoutManager = new StaggeredGridLayoutManager
                                    (2, StaggeredGridLayoutManager.VERTICAL);
                            Util.SpacesItemDecoration decoration = new Util.SpacesItemDecoration(25);
                            delMenuRecyclerView.setLayoutManager(recyclerViewLayoutManager);
                            delMenuRecyclerView.addItemDecoration(decoration);
                            delMenuRecyclerView.setNestedScrollingEnabled(false);
                            DeliveryMenuMasonryAdapter masonryAdapter = new DeliveryMenuMasonryAdapter(DeliveryMenuActivity.this);
                            delMenuRecyclerView.setAdapter(masonryAdapter);
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
            public void onFailure(Call<DeliveryMenuResponse> call, Throwable t) {
                Log.d(TAG, "Failure");
            }
        });

    }


}
