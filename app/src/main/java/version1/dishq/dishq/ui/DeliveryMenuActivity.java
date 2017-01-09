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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_menu);
        fetchDeliveryMenuInfo();
        setTags();
    }

    protected void setTags() {

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
                    if(response.isSuccessful()) {
                        Log.d(TAG, "response is successful");
                        DeliveryMenuResponse.DeliveryMenuInfo body = response.body().deliveryMenuInfo;
                        if(body!=null) {
                            Log.d(TAG, "body is not null");
                        }
                    }else {
                        String error = response.errorBody().string();
                        Log.d(TAG,error);
                    }
                }catch (IOException e) {
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
