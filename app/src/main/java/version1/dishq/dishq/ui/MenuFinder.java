package version1.dishq.dishq.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import version1.dishq.dishq.R;
import version1.dishq.dishq.adapters.menuFinderAdapters.MenuFinderNearbyRestAdapter;
import version1.dishq.dishq.adapters.menuFinderAdapters.MenuFinderRestAdapter;
import version1.dishq.dishq.fragments.bottomSheetFragment.DineoutFragment;
import version1.dishq.dishq.server.Config;
import version1.dishq.dishq.server.Response.MenuFinderNearbyRestResponse;
import version1.dishq.dishq.server.RestApi;
import version1.dishq.dishq.util.DishqApplication;
import version1.dishq.dishq.util.Util;

/**
 * Created by dishq on 09-01-2017.
 * Package name version1.dishq.dishq.
 */

public class MenuFinder extends AppCompatActivity {

    private static final String TAG = "MenuFinder";
    RecyclerView recyclerView;
    protected LayoutManagerType mCurrentLayoutManagerType;
    protected RecyclerView.LayoutManager mLayoutManager;
    private ProgressDialog progressDialog = null;
    private AutoCompleteTextView autoCompleteTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog = new ProgressDialog(MenuFinder.this);
        progressDialog.show();
        setContentView(R.layout.activity_menu_finder);
        Toolbar toolbar = (Toolbar) findViewById(R.id.menu_finder_toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Menu Finder");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        showNearbyRest();
        autoCompleteTextView= (AutoCompleteTextView)findViewById(R.id.filter_quick_search_auto_complete);
        ArrayAdapter<MenuFinderRestAdapter> adapter = new ArrayAdapter<>
                (this, R.layout.cardview_dineout);
        autoCompleteTextView.setThreshold(2);
        autoCompleteTextView.setAdapter(adapter);
        recyclerView = (RecyclerView) findViewById(R.id.menu_finder_recycler_view);

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void showNearbyRest() {
        RestApi restApi = Config.createService(RestApi.class);
        String currentLatitude = "12.92923258";
        String currentLongitude = "77.63082482";
        Call<MenuFinderNearbyRestResponse> call = restApi.getNearbyRestaurants(DishqApplication.getUniqueID(),
                DishqApplication.getUserID(), currentLatitude, currentLongitude);
        call.enqueue(new Callback<MenuFinderNearbyRestResponse>() {
            @Override
            public void onResponse(Call<MenuFinderNearbyRestResponse> call,
                                   Response<MenuFinderNearbyRestResponse> response) {
                Log.d(TAG, "Success");
                try {
                    if(response.isSuccessful()) {
                        ArrayList<MenuFinderNearbyRestResponse.NearbyRestInfo> body = response.body().nearbyRestInfos;
                        if(body!=null) {
                            Log.d(TAG, "body is not null");
                            Util.nearbyRestInfos.clear();
                            for(int i = 0; i <body.size(); i++) {
                                Util.nearbyRestInfos = body;
                            }
                            progressDialog.dismiss();
                            MenuFinderNearbyRestAdapter adapter = new MenuFinderNearbyRestAdapter(MenuFinder.this);
                            recyclerView.setAdapter(adapter);
                            mLayoutManager = new LinearLayoutManager(MenuFinder.this);
                            mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
                            setRecyclerViewLayoutManager(mCurrentLayoutManagerType);
                            adapter.notifyDataSetChanged();
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
            public void onFailure(Call<MenuFinderNearbyRestResponse> call, Throwable t) {
                Log.d(TAG, "Failure");
                progressDialog.dismiss();
            }
        });
    }

    public void setRecyclerViewLayoutManager(LayoutManagerType layoutManagerType) {
        int scrollPosition = 0;

        // If a layout manager has already been set, get current scroll position.
        if (recyclerView.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) recyclerView.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition();
        }
        switch (layoutManagerType) {
            case LINEAR_LAYOUT_MANAGER:
                mLayoutManager = new LinearLayoutManager(MenuFinder.this);
                mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
                break;
            default:
                mLayoutManager = new LinearLayoutManager(MenuFinder.this);
                mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
        }

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.scrollToPosition(scrollPosition);
    }

    private enum LayoutManagerType {
        LINEAR_LAYOUT_MANAGER
    }

}
