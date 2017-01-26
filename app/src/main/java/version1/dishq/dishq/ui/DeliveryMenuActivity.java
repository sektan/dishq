package version1.dishq.dishq.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mixpanel.android.mpmetrics.MixpanelAPI;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URISyntaxException;

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
    private RelativeLayout rlOrderNow;
    private Button orderNow;
    private NestedScrollView nestedScrollView;
    private MixpanelAPI mixpanel = null;
    private ProgressBar progressBar;
    private FrameLayout progressBg;
    private TextView personalizedText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_menu);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);
        progressBg = (FrameLayout) findViewById(R.id.progress_bg_overlay_del);
        progressBg.setVisibility(View.VISIBLE);
        //MixPanel Instantiation
        mixpanel = MixpanelAPI.getInstance(this, getResources().getString(R.string.mixpanel_token));

        fetchDeliveryMenuInfo();

        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapse_toolbar);
        collapsingToolbar.setTitle("My Toolbar Tittle");

    }

    protected void setTags() {

        nestedScrollView = (NestedScrollView) findViewById(R.id.del_nested_scroll);
        personalizedText = (TextView) findViewById(R.id.personalized_menu_text);
        personalizedText.setTypeface(Util.opensansregular);
        rlOrderNow = (RelativeLayout) findViewById(R.id.rl_delmenu_ordernow);
        orderNow = (Button) findViewById(R.id.order_now);
        orderNow.setTypeface(Util.opensanssemibold);
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
        delMenuRp2.setTypeface(Util.opensanssemibold);
        delMenuRp3 = (TextView) findViewById(R.id.delmenu_rup_3);
        delMenuRp3.setTypeface(Util.opensanssemibold);
        delMenuRp4 = (TextView) findViewById(R.id.delmenu_rup_4);
        delMenuRp4.setTypeface(Util.opensanssemibold);
        delMenuDrive = (TextView) findViewById(R.id.delmenu_drive_time);
        delMenuDrive.setTypeface(Util.opensansregular);
        delMenuTags = (TextView) findViewById(R.id.delmenu_tags);
        delMenuTags.setTypeface(Util.opensansregular);
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
        rlOrderNow.setVisibility(View.VISIBLE);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Util.setHomeRefreshRequired(false);
                finish();
            }
        });

        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                if (scrollY > oldScrollY) {
                    Log.i(TAG, "Scroll DOWN");
                    rlOrderNow.setVisibility(View.GONE);
                }
                if (scrollY < oldScrollY) {
                    Log.i(TAG, "Scroll UP");
                    rlOrderNow.setVisibility(View.VISIBLE);
                    final int position = v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight();
                    orderNow.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            try {
                                final JSONObject properties = new JSONObject();
                                properties.put("Order now button in delivery menu", "deliverymenu");
                                mixpanel.track("Order now button in delivery menu", properties);
                            } catch (final JSONException e) {
                                throw new RuntimeException("Could not encode hour of the day in JSON");
                            }
                            nestedScrollView.scrollTo(0, position);
                        }
                    });
                }

                if (scrollY == 0) {
                    Log.i(TAG, "TOP SCROLL");
                    rlOrderNow.setVisibility(View.VISIBLE);

                    final int position = v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight();
                    orderNow.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            try {
                                final JSONObject properties = new JSONObject();
                                properties.put("Order now button in delivery menu", "deliverymenu");
                                mixpanel.track("Order now button in delivery menu", properties);
                            } catch (final JSONException e) {
                                throw new RuntimeException("Could not encode hour of the day in JSON");
                            }
                            nestedScrollView.scrollTo(0, position);
                        }
                    });
                }

                if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
                    Log.i(TAG, "BOTTOM SCROLL");

                    rlOrderNow.setVisibility(View.GONE);
                }

            }
        });

        if (Util.deliveryRestData != null) {
            delMenuHeader.setText(Util.deliveryRestData.getDelMenuRestName());
            dineoutButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        final JSONObject properties = new JSONObject();
                        properties.put("Dineout menu flipbutton", "deliverymenu");
                        mixpanel.track("Dineout menu flipbutton", properties);
                    } catch (final JSONException e) {
                        throw new RuntimeException("Could not encode hour of the day in JSON");
                    }
                    Util.setDineRestId(Util.deliveryRestData.getDelMenuRestId());
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
            final String runnrUrl = Util.deliveryRestData.getDelMenuRunnrUrl();
            if(runnrUrl.equals("")) {
                runnr.setVisibility(View.GONE);
            }else {
                runnr.setVisibility(View.VISIBLE);
                runnr.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            final JSONObject properties = new JSONObject();
                            properties.put("Runnr order", "deliverymenu");
                            mixpanel.track("Runnr order", properties);
                        } catch (final JSONException e) {
                            throw new RuntimeException("Could not encode hour of the day in JSON");
                        }
                        Intent shareIntent = new Intent();
                        shareIntent.setAction(Intent.ACTION_VIEW);
                        shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        shareIntent.setData(Uri.parse(runnrUrl));
                        startActivity(shareIntent);
                    }
                });
            }
        }else {
            runnr.setVisibility(View.GONE);
        }

        if(Util.deliveryRestData.getDelMenuFoodPandaUrl()!=null) {
            final String foodpandaUrl = Util.deliveryRestData.getDelMenuFoodPandaUrl();
            if(foodpandaUrl.equals("")) {
                foodpanda.setVisibility(View.GONE);
            }else {
                foodpanda.setVisibility(View.VISIBLE);
                foodpanda.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            final JSONObject properties = new JSONObject();
                            properties.put("Foodpanda order", "deliverymenu");
                            mixpanel.track("Foodpanda order", properties);
                        } catch (final JSONException e) {
                            throw new RuntimeException("Could not encode hour of the day in JSON");
                        }

                        Intent shareIntent = new Intent();
                        shareIntent.setAction(Intent.ACTION_VIEW);
                        shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        shareIntent.setData(Uri.parse(foodpandaUrl));
                        startActivity(shareIntent);
                    }
                });
            }
        }else {
            foodpanda.setVisibility(View.GONE);
        }

        if(Util.deliveryRestData.getDelMenuZomatoUrl()!=null) {
            final String zomatoUrl = Util.deliveryRestData.getDelMenuZomatoUrl();
            Log.d(TAG, "The url is : " + zomatoUrl);
            if(zomatoUrl.equals("")) {
                zomato.setVisibility(View.GONE);
            }else {
                zomato.setVisibility(View.VISIBLE);

                zomato.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            final JSONObject properties = new JSONObject();
                            properties.put("Zomato order", "deliverymenu");
                            mixpanel.track("Zomato order", properties);
                        } catch (final JSONException e) {
                            throw new RuntimeException("Could not encode hour of the day in JSON");
                        }

                    Intent shareIntent = new Intent();
                    shareIntent.setAction(Intent.ACTION_VIEW);
                    shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    shareIntent.setData(Uri.parse(zomatoUrl));
                    startActivity(shareIntent);
                    }
                });
            }
        }else {
            zomato.setVisibility(View.GONE);
        }

        if (Util.deliveryRestData.getDelMenuSwiggyUrl()!=null) {
            final String swiggyUrl = Util.deliveryRestData.getDelMenuSwiggyUrl();
            Log.d(TAG, "the url is : " + swiggyUrl);
            if(swiggyUrl.equals("")) {
                swiggy.setVisibility(View.GONE);
            }else {
                swiggy.setVisibility(View.VISIBLE);
                swiggy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            final JSONObject properties = new JSONObject();
                            properties.put("Swiggy order", "deliverymenu");
                            mixpanel.track("Swiggy order", properties);
                        } catch (final JSONException e) {
                            throw new RuntimeException("Could not encode hour of the day in JSON");
                        }


                        Intent shareIntent = new Intent();
                        shareIntent.setAction(Intent.ACTION_VIEW);
                        shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        shareIntent.setData(Uri.parse(swiggyUrl));
                        startActivity(shareIntent);
                    }
                });
            }
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
                            recyclerViewLayoutManager = new GridLayoutManager
                                    (DeliveryMenuActivity.this, 2);
                            Util.SpacesItemDecoration decoration = new Util.SpacesItemDecoration(13);
                            delMenuRecyclerView.setLayoutManager(recyclerViewLayoutManager);
                            delMenuRecyclerView.addItemDecoration(decoration);
                            delMenuRecyclerView.setNestedScrollingEnabled(false);
                            DeliveryMenuMasonryAdapter masonryAdapter = new DeliveryMenuMasonryAdapter(DeliveryMenuActivity.this);
                            progressBar.setVisibility(View.GONE);
                            progressBg.setVisibility(View.GONE);
                            delMenuRecyclerView.setAdapter(masonryAdapter);
                        }
                        progressBar.setVisibility(View.GONE);
                        progressBg.setVisibility(View.GONE);
                    } else {
                        progressBar.setVisibility(View.GONE);
                        progressBg.setVisibility(View.GONE);
                        String error = response.errorBody().string();
                        Log.d(TAG, error);
                    }
                } catch (IOException e) {
                    progressBar.setVisibility(View.GONE);
                    progressBg.setVisibility(View.GONE);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<DeliveryMenuResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                progressBg.setVisibility(View.GONE);
                Log.d(TAG, "Failure");
            }
        });

    }

    @Override
    protected void onDestroy() {
        mixpanel.flush();
        super.onDestroy();
    }

}
