package version1.dishq.dishq.ui;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import version1.dishq.dishq.BaseActivity;
import version1.dishq.dishq.R;
import version1.dishq.dishq.adapters.favGridViewAdapter.FavGridViewAdapter;
import version1.dishq.dishq.server.Config;
import version1.dishq.dishq.server.Response.FavouriteDishesResponse;
import version1.dishq.dishq.server.RestApi;
import version1.dishq.dishq.util.DishqApplication;
import version1.dishq.dishq.util.Util;

/**
 * Created by dishq on 05-01-2017.
 * Package name version1.dishq.dishq.
 */

public class FavouritesActivity extends BaseActivity {

    private static final String TAG = "FavouritesActivity";
    private TextView favHeader;
    private ImageView backButton;
    public RelativeLayout rlNoFav;
    private RecyclerView.LayoutManager recyclerViewLayoutManager;
    private RecyclerView favRecyclerView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);
        fetchDishFavourites();
        setTags();
    }

    protected void setTags() {
        favHeader = (TextView) findViewById(R.id.fav_toolbarTitle);
        favHeader.setText(getResources().getString(R.string.fav_header));
        favHeader.setTypeface(Util.opensanssemibold);
        backButton = (ImageView) findViewById(R.id.fav_back_button);
        rlNoFav = (RelativeLayout) findViewById(R.id.rl_no_fav);
        favRecyclerView = (RecyclerView) findViewById(R.id.fav_recyclerView);
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
    }

    public RelativeLayout getRlNoFav() {
        return this.rlNoFav;
    }

    public void setRlNoFav(RelativeLayout rlNoFav) {
        this.rlNoFav = rlNoFav;
    }

    private void fetchDishFavourites() {
        RestApi restApi = Config.createService(RestApi.class);
        Call<FavouriteDishesResponse> call = restApi.getFavouriteDishes(DishqApplication.getAccessToken(),
                DishqApplication.getUniqueID());
        call.enqueue(new Callback<FavouriteDishesResponse>() {
            @Override
            public void onResponse(Call<FavouriteDishesResponse> call, Response<FavouriteDishesResponse> response) {
                Log.d(TAG, "Success");
                try {
                    if (response.isSuccessful()) {
                        progressBar.setVisibility(View.GONE);
                        Log.d(TAG, "response is successful");
                        ArrayList<FavouriteDishesResponse.FavouriteDishesInfo> body = response.body().favouriteDishesInfos;
                        if (body != null) {
                            Log.d(TAG, "body is not null");
                            if(body.size() == 0) {
                                rlNoFav.setVisibility(View.VISIBLE);
                                favRecyclerView.setVisibility(View.GONE);
                            } else{
                                Util.favouriteDishesInfos.clear();
                                for(int i = 0; i<body.size(); i++) {
                                    Util.favouriteDishesInfos = body;
                                }
                                rlNoFav.setVisibility(View.GONE);
                                favRecyclerView.setVisibility(View.VISIBLE);
                                recyclerViewLayoutManager = new GridLayoutManager(FavouritesActivity.this, 2);
                                favRecyclerView.setLayoutManager(recyclerViewLayoutManager);
                                FavGridViewAdapter favGridViewAdapter = new FavGridViewAdapter(FavouritesActivity.this, rlNoFav);
                                favRecyclerView.setAdapter(favGridViewAdapter);
                                if(Util.favouriteDishesInfos.size() == 0) {
                                    rlNoFav.setVisibility(View.VISIBLE);
                                    favRecyclerView.setVisibility(View.GONE);
                                }
                            }
                        }else {
                            rlNoFav.setVisibility(View.VISIBLE);
                            favRecyclerView.setVisibility(View.GONE);
                        }
                    } else {
                        progressBar.setVisibility(View.GONE);
                        String error = response.errorBody().string();
                        Log.d(TAG, error);
                        rlNoFav.setVisibility(View.VISIBLE);
                        favRecyclerView.setVisibility(View.GONE);
                    }
                } catch (IOException e) {
                    progressBar.setVisibility(View.GONE);
                    e.printStackTrace();
                    rlNoFav.setVisibility(View.VISIBLE);
                    favRecyclerView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<FavouriteDishesResponse> call, Throwable t) {
                Log.d(TAG, "Failure");
                progressBar.setVisibility(View.GONE);
                rlNoFav.setVisibility(View.VISIBLE);
                favRecyclerView.setVisibility(View.GONE);
            }
        });
    }
}
