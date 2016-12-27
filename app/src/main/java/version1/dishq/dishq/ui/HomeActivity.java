package version1.dishq.dishq.ui;

import android.os.Bundle;
import android.util.Log;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import version1.dishq.dishq.BaseActivity;
import version1.dishq.dishq.R;
import version1.dishq.dishq.server.Config;
import version1.dishq.dishq.server.Response.HomeDishesResponse;
import version1.dishq.dishq.server.RestApi;
import version1.dishq.dishq.util.DishqApplication;
import version1.dishq.dishq.util.Util;

/**
 * Created by dishq on 14-12-2016.
 * Package name version1.dishq.dishq.
 */

public class HomeActivity extends BaseActivity {

    private static final String TAG = "HomeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        fetchHomeDishResults();
    }

    public void fetchHomeDishResults() {
        String latitude = Util.getLatitude();
        String longitude = Util.getLongitude();
        RestApi restApi = Config.createService(RestApi.class);
        Call<HomeDishesResponse> call = restApi.fetchPersonalDishes(DishqApplication.getAccessToken(), DishqApplication.getUniqueID(),
                latitude, longitude, -1, "", -1);
        call.enqueue(new Callback<HomeDishesResponse>() {
            @Override
            public void onResponse(Call<HomeDishesResponse> call, Response<HomeDishesResponse> response) {
                Log.d(TAG, "Success");
                try {
                    if(response.isSuccessful()) {
                        HomeDishesResponse.HomeData body = response.body().homeData;
                        if(body!=null) {
                            Log.d(TAG, "Body is not null");
                        }

                    }else {
                        String error = response.errorBody().string();
                        Log.d(TAG, error);
                    }
                }catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<HomeDishesResponse> call, Throwable t) {
                Log.d(TAG, "Failure");
            }
        });

    }
}
