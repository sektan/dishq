package version1.dishq.dishq.fragments.bottomSheetFragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import version1.dishq.dishq.R;
import version1.dishq.dishq.adapters.bottomSheetAdapters.DeliveryAdapter;
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
    protected LayoutManagerType mCurrentLayoutManagerType;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected RecyclerView mRecyclerView;
    private ProgressDialog progressDialog;
    private Button showMore;
    private RelativeLayout rlNoDel;
    private TextView noDelText;
    private Boolean hasMoreResults = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.show();
        fetchDeliveryRest(0);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_delivery, container, false);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.delivery_rest_cardlist);
        showMore = (Button) v.findViewById(R.id.delivery_show);
        showMore.setTypeface(Util.opensanssemibold);
        rlNoDel = (RelativeLayout) v.findViewById(R.id.rl_no_del);
        noDelText = (TextView) v.findViewById(R.id.no_del_text);
        noDelText.setTypeface(Util.opensanslight);

        if (hasMoreResults) {
            showMore.setVisibility(View.VISIBLE);
            showMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    fetchDeliveryRest(1);
                }
            });
        }
        return v;
    }

    public void setRecyclerViewLayoutManager(LayoutManagerType layoutManagerType) {
        int scrollPosition = 0;

        // If a layout manager has already been set, get current scroll position.
        if (mRecyclerView.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) mRecyclerView.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition();
        }
        switch (layoutManagerType) {
            case LINEAR_LAYOUT_MANAGER:
                mLayoutManager = new LinearLayoutManager(getActivity());
                mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
                break;
            default:
                mLayoutManager = new LinearLayoutManager(getActivity());
                mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
        }

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.scrollToPosition(scrollPosition);
    }

    public void fetchDeliveryRest(final int showMoreOptions) {
        String latitude = "12.92923258", longitude = "77.63082482", source = "homescreen";

        RestApi restApi = Config.createService(RestApi.class);
        Call<DeliveryTabResponse> call = restApi.addDelivRestOptions(DishqApplication.getAccessToken(), Util.getGenericDishIdTab(),
               DishqApplication.getUniqueID(), source, latitude, longitude, showMoreOptions);
        call.enqueue(new Callback<DeliveryTabResponse>() {
            @Override
            public void onResponse(Call<DeliveryTabResponse> call, Response<DeliveryTabResponse> response) {
                Log.d(TAG, "Success");
                try {
                    if(response.isSuccessful()) {
                        if (showMoreOptions == 1) {
                            showMore.setVisibility(View.GONE);
                        } else {
                            showMore.setVisibility(View.VISIBLE);
                        }
                        DeliveryTabResponse.DeliveryRestaurants body = response.body().deliveryRestaurants;
                        Util.deliveryRestInfos.clear();
                        if(body!=null) {
                            Log.d(TAG, "the body is not empty");
                            rlNoDel.setVisibility(View.GONE);
                            mRecyclerView.setVisibility(View.VISIBLE);
                            for(int i = 0; i <body.deliveryRestInfo.size(); i++) {
                                Util.deliveryRestInfos = body.deliveryRestInfo;
                            }
                            progressDialog.dismiss();
                            DeliveryAdapter deliveryAdapter = new DeliveryAdapter(getActivity());
                            mRecyclerView.setAdapter(deliveryAdapter);
                            mLayoutManager = new LinearLayoutManager(getActivity());
                            mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
                            setRecyclerViewLayoutManager(mCurrentLayoutManagerType);

                        }else{
                            progressDialog.dismiss();
                            rlNoDel.setVisibility(View.VISIBLE);
                            mRecyclerView.setVisibility(View.GONE);
                        }
                    }else {
                        progressDialog.dismiss();
                        String error = response.errorBody().string();
                        Log.d(TAG, "Error: " + error);
                    }
                }catch (IOException e) {
                    progressDialog.dismiss();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<DeliveryTabResponse> call, Throwable t) {
                Log.d(TAG, "Failure");
                progressDialog.dismiss();
            }
        });
    }

    private enum LayoutManagerType {
        LINEAR_LAYOUT_MANAGER
    }
}
