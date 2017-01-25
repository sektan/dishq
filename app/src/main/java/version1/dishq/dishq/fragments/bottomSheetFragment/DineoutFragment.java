package version1.dishq.dishq.fragments.bottomSheetFragment;

import android.os.Bundle;
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
import version1.dishq.dishq.adapters.bottomSheetAdapters.DineoutAdapter;
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
    protected LayoutManagerType mCurrentLayoutManagerType;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected RecyclerView mRecyclerView;
    private Button showMore;
    private RelativeLayout rlNoDine;
    private TextView noDineText;
    private ProgressBar progressBar;
    private Boolean hasMoreResults = false;
    Boolean showMoreClicked = false;
    private DineoutAdapter dineoutAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_dineout, container, false);
        progressBar = (ProgressBar) v.findViewById(R.id.dine_bsf_progress);
        progressBar.setVisibility(View.VISIBLE);
        fetchDineoutRest(0);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.dineout_rest_cardlist);
        showMore = (Button) v.findViewById(R.id.dineout_show);
        showMore.setTypeface(Util.opensanssemibold);
        rlNoDine = (RelativeLayout) v.findViewById(R.id.rl_no_dine);
        noDineText = (TextView) v.findViewById(R.id.no_dine_text);
        noDineText.setTypeface(Util.opensansregular);
        if (hasMoreResults) {
            showMore.setVisibility(View.VISIBLE);
            showMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showMoreClicked = true;
                    dineoutAdapter.notifyDataSetChanged();
                    fetchDineoutRest(1);
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

    public void fetchDineoutRest(final int showMoreOptions) {

        int genericDishId = Util.getGenericDishIdTab();
        String latitude = "12.92923258", longitude = "77.63082482", source = "homescreen";
        RestApi restApi = Config.createService(RestApi.class);
        Call<DineoutTabResponse> call = restApi.addDineRestOptions(DishqApplication.getAccessToken(),
                genericDishId, DishqApplication.getUniqueID(), source, latitude,
                longitude, showMoreOptions);
        call.enqueue(new Callback<DineoutTabResponse>() {
            @Override
            public void onResponse(Call<DineoutTabResponse> call, Response<DineoutTabResponse> response) {
                Log.d(TAG, "Success");
                try {
                    if(response.isSuccessful()) {
                        DineoutTabResponse.DineoutRestaurants body = response.body().dineoutRestaurants;
                        if(body!=null) {
                            if (body.dineoutRestInfo.size() != 0) {
                                Log.d(TAG, "body is not null");
                                hasMoreResults = body.isHasMoreResults();
                                Util.dineoutRestInfos.clear();
                                for (int i = 0; i < body.dineoutRestInfo.size(); i++) {
                                    Util.dineoutRestInfos = body.dineoutRestInfo;
                                }
                                if (showMoreOptions == 1) {
                                    if (!showMoreClicked) {
                                        showMore.setVisibility(View.VISIBLE);
                                    } else {
                                        showMore.setVisibility(View.GONE);
                                    }
                                } else {
                                    showMore.setVisibility(View.GONE);
                                }
                                progressBar.setVisibility(View.GONE);
                                dineoutAdapter = new DineoutAdapter(getActivity());
                                mRecyclerView.setVisibility(View.VISIBLE);
                                mRecyclerView.setAdapter(dineoutAdapter);
                                mLayoutManager = new LinearLayoutManager(getActivity());
                                mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
                                setRecyclerViewLayoutManager(mCurrentLayoutManagerType);
                            }else {
                                progressBar.setVisibility(View.GONE);
                                rlNoDine.setVisibility(View.VISIBLE);
                                mRecyclerView.setVisibility(View.GONE);
                            }
                        }else {
                            progressBar.setVisibility(View.GONE);
                            rlNoDine.setVisibility(View.VISIBLE);
                            mRecyclerView.setVisibility(View.GONE);
                        }
                    }else {
                        progressBar.setVisibility(View.GONE);
                        rlNoDine.setVisibility(View.VISIBLE);
                        mRecyclerView.setVisibility(View.GONE);
                        String error = response.errorBody().string();
                        Log.d(TAG, "Error: " + error);
                    }
                }catch (IOException e) {
                    progressBar.setVisibility(View.GONE);
                    rlNoDine.setVisibility(View.VISIBLE);
                    mRecyclerView.setVisibility(View.GONE);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<DineoutTabResponse> call, Throwable t) {
                Log.d(TAG, "Failure");
                progressBar.setVisibility(View.GONE);
                rlNoDine.setVisibility(View.VISIBLE);
                mRecyclerView.setVisibility(View.GONE);
            }
        });
    }

    private enum LayoutManagerType {
        LINEAR_LAYOUT_MANAGER
    }
}
