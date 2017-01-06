package version1.dishq.dishq.ui;

import android.os.Bundle;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import version1.dishq.dishq.BaseActivity;
import version1.dishq.dishq.R;
import version1.dishq.dishq.server.Config;
import version1.dishq.dishq.server.Response.FavouriteDishesResponse;
import version1.dishq.dishq.server.RestApi;
import version1.dishq.dishq.util.DishqApplication;

/**
 * Created by dishq on 05-01-2017.
 * Package name version1.dishq.dishq.
 */

public class FavouritesActivity extends BaseActivity {

    private static final String TAG = "FavouritesActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);
        fetchDishFavourites();
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
                    if(response.isSuccessful()) {
                        Log.d(TAG, "response is successful");
                       // ArrayList<FavouriteDishesResponse.FavouriteDishesInfo> body = response.body().favouriteDishesInfos;
//                        if(body!=null) {
//                            Log.d(TAG, "body is not null");
                       // }
                    }else {
                        String error = response.errorBody().string();
                        Log.d(TAG, error);
                    }
                }catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<FavouriteDishesResponse> call, Throwable t) {
                Log.d(TAG, "Failure");
            }
        });
    }
}
