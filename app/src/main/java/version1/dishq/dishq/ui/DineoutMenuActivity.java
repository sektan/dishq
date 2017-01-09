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
import version1.dishq.dishq.server.Response.DineoutMenuResponse;
import version1.dishq.dishq.server.RestApi;
import version1.dishq.dishq.util.DishqApplication;
import version1.dishq.dishq.util.Util;

/**
 * Created by dishq on 09-01-2017.
 * Package name version1.dishq.dishq.
 */

public class DineoutMenuActivity extends BaseActivity {

    private static final String TAG = "DineoutMenuActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dineout_menu);
        fetchDineoutMenuInfo();
        setTags();
    }

    protected void setTags() {

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
