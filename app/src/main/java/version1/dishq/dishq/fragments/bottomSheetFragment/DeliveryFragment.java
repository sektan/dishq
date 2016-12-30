package version1.dishq.dishq.fragments.bottomSheetFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import version1.dishq.dishq.R;
import version1.dishq.dishq.server.Config;
import version1.dishq.dishq.server.Response.DeliveryTabResponse;
import version1.dishq.dishq.server.RestApi;
import version1.dishq.dishq.util.DishqApplication;
import version1.dishq.dishq.util.Util;

/**
 * Created by dishq on 29-12-2016.
 * Package name version1.dishq.dishq.
 */

public class DeliveryFragment extends Fragment {

    private static final String TAG = "DeliveryFragment";
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fetchDeliveryRest();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_delivery, container, false);

        return v;
    }

    public static DeliveryFragment newInstance(String text) {
        DeliveryFragment f = new DeliveryFragment();
        Bundle b = new Bundle();
        b.putString("msg", text);
        f.setArguments(b);
        return f;
    }

    public void fetchDeliveryRest() {
        String latitude = "12.92923258", longitude = "77.63082482", source = "homescreen";
        int showMore = 0;

        RestApi restApi = Config.createService(RestApi.class);
        Call<DeliveryTabResponse> call = restApi.addDelivRestOptions(DishqApplication.getAccessToken(), Util.getGenericDishIdTab(),
               DishqApplication.getUniqueID(), source, latitude, longitude, showMore);
        call.enqueue(new Callback<DeliveryTabResponse>() {
            @Override
            public void onResponse(Call<DeliveryTabResponse> call, Response<DeliveryTabResponse> response) {
                Log.d(TAG, "Success");
            }

            @Override
            public void onFailure(Call<DeliveryTabResponse> call, Throwable t) {
                Log.d(TAG, "Failure");
            }
        });
    }
}
