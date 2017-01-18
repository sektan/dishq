package version1.dishq.dishq.ui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import version1.dishq.dishq.R;
import version1.dishq.dishq.adapters.menuFinderAdapters.MenuFinderNearbyRestAdapter;
import version1.dishq.dishq.server.Config;
import version1.dishq.dishq.server.Response.MenuFinderNearbyRestResponse;
import version1.dishq.dishq.server.Response.MenuFinderRestSuggestResponse;
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
    ListView listView;
    protected LayoutManagerType mCurrentLayoutManagerType;
    protected RecyclerView.LayoutManager mLayoutManager;
    private EditText txtAutoComplete;
    private ProgressBar progressBar;
    private String str="";
    private TextView nearbyTextView, mfTextView;
    private MyAdapter myAdapter;
    private LinearLayout norestaurant;
    private RelativeLayout rlNearByRest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_menu_finder);
        Toolbar toolbar = (Toolbar) findViewById(R.id.menu_finder_toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Menu Finder");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        showNearbyRest();
        setTags();
    }

    protected void setTags() {
        listView = (ListView) findViewById(R.id.suggest_recyclerview);
        recyclerView = (RecyclerView) findViewById(R.id.menu_finder_recycler_view);
        txtAutoComplete = (EditText) findViewById(R.id.menufinder_autosuggest);
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(txtAutoComplete.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
        rlNearByRest = (RelativeLayout) findViewById(R.id.rl_nearby_rest);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        mfTextView = (TextView) findViewById(R.id.menufinder_no_results_text);
        //nearbyTextView = (TextView) findViewById(R.id.text_nearby_restaurants);
        norestaurant = (LinearLayout) findViewById(R.id.noresult);
        setFunctionality();
    }

    protected void setFunctionality() {
        try {
            txtAutoComplete.setInputType(InputType.TYPE_CLASS_TEXT);
            txtAutoComplete.requestFocus();
            InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            mgr.showSoftInput(txtAutoComplete, InputMethodManager.SHOW_FORCED);
            txtAutoComplete.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    final int DRAWABLE_RIGHT = 2;
                    if (txtAutoComplete.getText().length() > 1) {
                        if (event.getAction() == MotionEvent.ACTION_UP) {
                            if (event.getRawX() >= (txtAutoComplete.getRight() - txtAutoComplete.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                                // your action here
                                txtAutoComplete.setText("");
                                Util.menuFinderRestInfos.clear();
                                myAdapter.notifyDataSetChanged();
                                return true;
                            }
                        }
                    }
                    return false;
                }
            });

            txtAutoComplete.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                    str = s.toString();
                    if (s.length() > 1) {
                        txtAutoComplete.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.menufinder_search, 0, R.drawable.menufinder_cross, 0);
                        if (!Util.checkAndShowNetworkPopup(MenuFinder.this)) {
                            progressBar.setVisibility(View.VISIBLE);
                            fetchRestaurant(str);
                            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                        }
                    } else {
                        txtAutoComplete.setCompoundDrawablesWithIntrinsicBounds(R.drawable.menufinder_search, 0, 0, 0);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                   getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                }
            });

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                }
            });
        }catch (Exception e) {
            e.printStackTrace();
        }
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
                            MenuFinderNearbyRestAdapter adapter = new MenuFinderNearbyRestAdapter(MenuFinder.this);
                            recyclerView.setAdapter(adapter);
                            mLayoutManager = new LinearLayoutManager(MenuFinder.this);
                            mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
                            setRecyclerViewLayoutManager(mCurrentLayoutManagerType);
                            adapter.notifyDataSetChanged();
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
            public void onFailure(Call<MenuFinderNearbyRestResponse> call, Throwable t) {
                Log.d(TAG, "Failure");
            }
        });
    }

    private void fetchRestaurant(String query) {
        RestApi restApi = Config.createService(RestApi.class);
        Call<MenuFinderRestSuggestResponse> call = restApi.getRestSuggestion(query, DishqApplication.getUniqueID(),
                DishqApplication.getUserID());
        call.enqueue(new Callback<MenuFinderRestSuggestResponse>() {
            @Override
            public void onResponse(Call<MenuFinderRestSuggestResponse> call, Response<MenuFinderRestSuggestResponse> response) {
                progressBar.setVisibility(View.GONE);
                Log.d(TAG, "Success");
                try {
                    if(response.isSuccessful()) {
                        ArrayList<MenuFinderRestSuggestResponse.MenuFinderRestInfo> body = response.body().menuFinderRestInfos;
                        if(body!=null) {
                            if(body.size()!=0) {
                                Log.d(TAG, "body is not null");
                                norestaurant.setVisibility(View.GONE);
                                Util.menuFinderRestInfos.clear();
                                recyclerView.setVisibility(View.GONE);
                                for (int i = 0; i < body.size(); i++) {
                                    Util.menuFinderRestInfos = body;
                                }
                                myAdapter = new MyAdapter(MenuFinder.this);
                                listView.setVisibility(View.VISIBLE);
                                listView.setAdapter(myAdapter);

                            }else {
                                Util.menuFinderRestInfos.clear();
                                norestaurant.setVisibility(View.VISIBLE);
                                listView.setVisibility(View.GONE);
                            }
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
            public void onFailure(Call<MenuFinderRestSuggestResponse> call, Throwable t) {
                Log.d(TAG, "Failure");
                if(t instanceof SocketTimeoutException) {
                    progressBar.setVisibility(View.GONE);
                    Log.e("timeout", "timeout" + t.getMessage());
                    AlertDialog.Builder builder = new AlertDialog.Builder(MenuFinder.this);
                    builder.setMessage("Your Internet connection is slow. Please find a better connection.").setCancelable(false)
                            .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    fetchRestaurant(str);
                                }
                            });
                }
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

    class MyAdapter extends BaseAdapter {
        LayoutInflater layoutInflater;
        Context context;
        protected TextView mfRestName, mfRestAddr, mfRestCuisine, mfRup1,
                mfRup2, mfRup3, mfRup4, mfDriveTime;

        protected RelativeLayout rlNearbyRest;

        ImageView cardBgImage;

        MyAdapter(Context context) {
            this.context = context;
            layoutInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return Util.menuFinderRestInfos.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = null;
            if(view == null) {
                view = layoutInflater.inflate(R.layout.cardview_dineout, parent, false);
                mfRestName = (TextView) view.findViewById(R.id.dineout_rest_name);
                mfRestName.setTypeface(Util.opensanssemibold);
                mfRestAddr = (TextView) view.findViewById(R.id.dineout_rest_addr);
                mfRestAddr.setTypeface(Util.opensansregular);
                mfRestCuisine = (TextView) view.findViewById(R.id.dineout_rest_cuisine);
                mfRup1 = (TextView) view.findViewById(R.id.dineout_rup_1);
                mfRup2 = (TextView) view.findViewById(R.id.dineout_rup_2);
                mfRup3 = (TextView) view.findViewById(R.id.dineout_rup_3);
                mfRup4 = (TextView) view.findViewById(R.id.dineout_rup_4);
                mfDriveTime = (TextView) view.findViewById(R.id.dineout_drive_time);
                mfDriveTime.setTypeface(Util.opensanssemibold);
                rlNearbyRest = (RelativeLayout) view.findViewById(R.id.cv_rl_dineout);
                cardBgImage = (ImageView) view.findViewById(R.id.card_bg_image);
                cardBgImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
            }

            ArrayList<String> imageUrls = Util.menuFinderRestInfos.get(position).getMfPhotoThumbnail();
            String imageUrl = imageUrls.get(0);
            Picasso.with(DishqApplication.getContext())
                    .load(imageUrl)
                    .fit()
                    .centerCrop()
                    .noPlaceholder()
                    .into(cardBgImage);
            final int posn = position;
            rlNearbyRest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Util.setDineRestId(Util.menuFinderRestInfos.get(posn).getMfRestId());
                    Intent intent = new Intent(context, DineoutMenuActivity.class);
                    context.startActivity(intent);
                }
            });
            mfRestName.setText(Util.menuFinderRestInfos.get(position).getMfRestName());
            String mfRestAddress = "";
            if(Util.menuFinderRestInfos.get(position).getMfRestAddr()!=null) {
                for (String s : Util.menuFinderRestInfos.get(position).getMfRestAddr()) {
                    mfRestAddress += s;
                }
            }
            mfRestAddr.setText(mfRestAddress);
            StringBuilder sb = new StringBuilder();
            if(Util.menuFinderRestInfos.get(position).getMfRestCuisineText()!=null) {
                for (String s : Util.menuFinderRestInfos.get(position).getMfRestCuisineText()) {
                    if (sb.length() > 0) {
                        sb.append(',' + " ");
                    }
                    sb.append(s);
                }
            }
            String mfCusineText = sb.toString();
            mfRestCuisine.setText(mfCusineText);
            int mfPriceLvl = Util.menuFinderRestInfos.get(position).getMfPriceLvl();
            if (mfPriceLvl == 1) {
                mfRup1.setTextColor(ContextCompat.getColor(DishqApplication.getContext(), R.color.rupeeGreen));
            }else if(mfPriceLvl == 2) {
                mfRup1.setTextColor(ContextCompat.getColor(DishqApplication.getContext(), R.color.rupeeGreen));
                mfRup2.setTextColor(ContextCompat.getColor(DishqApplication.getContext(), R.color.rupeeGreen));
            }else if(mfPriceLvl == 3) {
                mfRup1.setTextColor(ContextCompat.getColor(DishqApplication.getContext(), R.color.rupeeGreen));
                mfRup2.setTextColor(ContextCompat.getColor(DishqApplication.getContext(), R.color.rupeeGreen));
                mfRup3.setTextColor(ContextCompat.getColor(DishqApplication.getContext(), R.color.rupeeGreen));
            }else if (mfPriceLvl == 4) {
                mfRup1.setTextColor(ContextCompat.getColor(DishqApplication.getContext(), R.color.rupeeGreen));
                mfRup2.setTextColor(ContextCompat.getColor(DishqApplication.getContext(), R.color.rupeeGreen));
                mfRup3.setTextColor(ContextCompat.getColor(DishqApplication.getContext(), R.color.rupeeGreen));
                mfRup4.setTextColor(ContextCompat.getColor(DishqApplication.getContext(), R.color.rupeeGreen));
            }

            mfDriveTime.setText(Util.menuFinderRestInfos.get(position).getMfDriveTime());
            return view;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Util.setHomeRefreshRequired(false);
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
