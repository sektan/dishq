package version1.dishq.dishq.fragments.bottomSheetFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import version1.dishq.dishq.R;
import version1.dishq.dishq.server.Config;
import version1.dishq.dishq.server.Response.DineoutTabResponse;
import version1.dishq.dishq.server.RestApi;
import version1.dishq.dishq.util.DishqApplication;
import version1.dishq.dishq.util.Util;

/**
 * Created by dishq on 29-12-2016.
 * Package name version1.dishq.dishq.
 */

public class DineoutFragment extends Fragment {

    private static final String TAG = "DineoutFragment";
    private Button showMore;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fetchDineoutRest();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_dineout, container, false);
        showMore = (Button) v.findViewById(R.id.dineout_show);
        return v;
    }

    public static DineoutFragment newInstance(String text) {
        DineoutFragment f = new DineoutFragment();
        Bundle b = new Bundle();
        b.putString("msg", text);
        f.setArguments(b);
        return f;
    }

    public void fetchDineoutRest() {

        int genericDishId = Util.getGenericDishIdTab();
        String latitude = "12.92923258", longitude = "77.63082482", source = "homescreen";
        int showMore = 0;
        RestApi restApi = Config.createService(RestApi.class);
        Call<DineoutTabResponse> call = restApi.addDineRestOptions(DishqApplication.getAccessToken(),
                genericDishId, DishqApplication.getUniqueID(), source, latitude,
                longitude, showMore);
        call.enqueue(new Callback<DineoutTabResponse>() {
            @Override
            public void onResponse(Call<DineoutTabResponse> call, Response<DineoutTabResponse> response) {
                Log.d(TAG, "Success");
                try {
                    if(response.isSuccessful()) {
                        DineoutTabResponse.DineoutRestaurants body = response.body().dineoutRestaurants;
                        if(body!=null) {
                            Log.d(TAG, "body is not null");
                        }
                    }else {
                        String error = response.errorBody().string();
                        Log.d(TAG, "Error: " + error);
                    }
                }catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<DineoutTabResponse> call, Throwable t) {
                Log.d(TAG, "Failure");
            }
        });
    }
}
