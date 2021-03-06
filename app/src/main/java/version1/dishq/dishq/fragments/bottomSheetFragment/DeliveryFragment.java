package version1.dishq.dishq.fragments.bottomSheetFragment;

import android.content.SharedPreferences;
import android.os.Build;
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
import android.widget.ProgressBar;
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
    private ProgressBar progressBar;
    private Button showMore;
    private RelativeLayout rlNoDel;
    private TextView noDelText;
    private TextView deliveredBy;
    private Boolean hasMoreResults = false;
    private DeliveryAdapter deliveryAdapter;
    Boolean showMoreClicked = false;
    private boolean networkFailed;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_delivery, container, false);
        progressBar = (ProgressBar) v.findViewById(R.id.del_bsf_progress);
        progressBar.setVisibility(View.VISIBLE);
        checkInternetConnection(0);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.delivery_rest_cardlist);
        deliveredBy = (TextView) v.findViewById(R.id.delivered_by);
        deliveredBy.setTypeface(Util.opensansregular);
        showMore = (Button) v.findViewById(R.id.delivery_show);
        showMore.setTypeface(Util.opensanssemibold);
        rlNoDel = (RelativeLayout) v.findViewById(R.id.rl_no_del);
        noDelText = (TextView) v.findViewById(R.id.no_del_text);
        noDelText.setTypeface(Util.opensansregular);
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

    //Method to check if the internet is connected or not
    private void checkInternetConnection(final int showMoreOptions) {
        SharedPreferences settings;
        final String PREFS_NAME = "MyPrefsFile";
        settings = getActivity().getSharedPreferences(PREFS_NAME, 0);

        if (settings.getBoolean("android_M", true)) {
            //the app is being launched for first time, do something
            Log.d("Comments", " android_M");
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // only for gingerbread and newer versions
                checkNetwork(showMoreOptions);
            } else {
                checkNetwork(showMoreOptions);
            }
            settings.edit().putBoolean("android_M", false).apply();
        } else {
            checkNetwork(showMoreOptions);
        }
    }

    //Check for internet
    private void checkNetwork(final int showMoreOptions) {
        if (!Util.checkAndShowNetworkPopup(getActivity())) {
            //Check for version
            Log.d(TAG, "Implementing a Version Check");
            fetchDeliveryRest(showMoreOptions);

        } else {
            networkFailed = true;
        }
    }

    public void fetchDeliveryRest(final int showMoreOptions) {
        String latitude = Util.getLatitude(), longitude = Util.getLongitude(), source = "homescreen";

        RestApi restApi = Config.createService(RestApi.class);
        Call<DeliveryTabResponse> call = restApi.addDelivRestOptions(DishqApplication.getAccessToken(), Util.getGenericDishIdTab(),
               DishqApplication.getUniqueID(), source, latitude, longitude, showMoreOptions);
        call.enqueue(new Callback<DeliveryTabResponse>() {
            @Override
            public void onResponse(Call<DeliveryTabResponse> call, Response<DeliveryTabResponse> response) {
                Log.d(TAG, "Success");
                try {
                    if(response.isSuccessful()) {
                        DeliveryTabResponse.DeliveryRestaurants body = response.body().deliveryRestaurants;
                        if(body!=null) {
                            if(body.deliveryRestInfo.size()!=0) {
                                Log.d(TAG, "the body is not empty");
                                hasMoreResults = body.isHasMoreResults();
                                if(showMoreClicked) {
                                    //do nothing
                                    
                                }else {
                                    Util.deliveryRestInfos.clear();
                                }
                                if (hasMoreResults) {
                                    if(showMoreClicked) {
                                        Util.deliveryRestInfos.addAll(body.deliveryRestInfo);
                                        showMoreClicked = false;
                                        showMore.setVisibility(View.GONE);
                                    }else {
                                        showMore.setVisibility(View.VISIBLE);
                                        for (int i = 0; i < body.deliveryRestInfo.size(); i++) {
                                            Util.deliveryRestInfos = body.deliveryRestInfo;
                                        }
                                    }

                                    showMore.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            progressBar.setVisibility(View.VISIBLE);
                                            showMoreClicked = true;
                                            deliveryAdapter.notifyDataSetChanged();
                                            fetchDeliveryRest(1);
                                        }
                                    });
                                }else {
                                    showMore.setVisibility(View.GONE);
                                    for (int i = 0; i < body.deliveryRestInfo.size(); i++) {
                                        Util.deliveryRestInfos = body.deliveryRestInfo;
                                    }
                                }
                                rlNoDel.setVisibility(View.GONE);
                                mRecyclerView.setVisibility(View.VISIBLE);
                                deliveredBy.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.GONE);
                                if(deliveryAdapter == null) {
                                    deliveryAdapter = new DeliveryAdapter(getActivity());
                                }
                                mRecyclerView.setAdapter(deliveryAdapter);
                                if(mLayoutManager==null) {
                                    mLayoutManager = new LinearLayoutManager(getActivity());
                                }
                                mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
                                setRecyclerViewLayoutManager(mCurrentLayoutManagerType);
                            }else {
                                progressBar.setVisibility(View.GONE);
                                rlNoDel.setVisibility(View.VISIBLE);
                                mRecyclerView.setVisibility(View.GONE);
                                deliveredBy.setVisibility(View.GONE);
                            }

                        }else{
                            progressBar.setVisibility(View.GONE);
                            rlNoDel.setVisibility(View.VISIBLE);
                            mRecyclerView.setVisibility(View.GONE);
                            deliveredBy.setVisibility(View.GONE);
                        }
                    }else {
                        progressBar.setVisibility(View.GONE);
                        String error = response.errorBody().string();
                        rlNoDel.setVisibility(View.VISIBLE);
                        mRecyclerView.setVisibility(View.GONE);
                        deliveredBy.setVisibility(View.GONE);
                        Log.d(TAG, "Error: " + error);
                    }
                }catch (IOException e) {
                    progressBar.setVisibility(View.GONE);
                    e.printStackTrace();
                    rlNoDel.setVisibility(View.VISIBLE);
                    mRecyclerView.setVisibility(View.GONE);
                    deliveredBy.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<DeliveryTabResponse> call, Throwable t) {
                Log.d(TAG, "Failure");
                progressBar.setVisibility(View.GONE);
                rlNoDel.setVisibility(View.VISIBLE);
                mRecyclerView.setVisibility(View.GONE);
                deliveredBy.setVisibility(View.GONE);
            }
        });
    }

    private enum LayoutManagerType {
        LINEAR_LAYOUT_MANAGER
    }
}
