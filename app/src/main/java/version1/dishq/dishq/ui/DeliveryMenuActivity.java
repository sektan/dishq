package version1.dishq.dishq.ui;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import version1.dishq.dishq.BaseActivity;
import version1.dishq.dishq.R;
import version1.dishq.dishq.adapters.deliveryMenuAdapter.DeliveryMenuMasonryAdapter;
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
    private RelativeLayout rlTopContent;
    private Button backButton, dineoutButton;
    private TextView delMenuHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_menu);
        fetchDeliveryMenuInfo();
        setTags();
    }

    protected void setTags() {
        delMenuRecyclerView = (RecyclerView) findViewById(R.id.del_menu_recyclerview);
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
                            Util.deliveryMenuInfos.clear();
                            Util.deliveryRestData = body.deliveryRestData;
                            for(int i = 0; i<body.deliveryMenuDatas.size(); i++) {
                                Util.deliveryMenuInfos = body.deliveryMenuDatas;
                            }
                            recyclerViewLayoutManager = new StaggeredGridLayoutManager
                                    (2, StaggeredGridLayoutManager.VERTICAL);
                            SpacesItemDecoration decoration = new SpacesItemDecoration(25);
                            delMenuRecyclerView.setLayoutManager(recyclerViewLayoutManager);
                            delMenuRecyclerView.addItemDecoration(decoration);
                            DeliveryMenuMasonryAdapter masonryAdapter = new DeliveryMenuMasonryAdapter(DeliveryMenuActivity.this);
                            delMenuRecyclerView.setAdapter(masonryAdapter);
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

    public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
        private final int mSpace;
        public SpacesItemDecoration(int space) {
            this.mSpace = space;
        }
        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            outRect.left = mSpace;
            outRect.right = mSpace;
            outRect.bottom = mSpace;
            // Add top margin only for the first item to avoid double space between items
            if (parent.getChildAdapterPosition(view) == 0)
                outRect.top = mSpace;
        }
    }
}
