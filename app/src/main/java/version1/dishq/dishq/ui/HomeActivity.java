package version1.dishq.dishq.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
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
import version1.dishq.dishq.BaseActivity;
import version1.dishq.dishq.R;
import version1.dishq.dishq.fragments.homeScreenFragment.HomeScreenFragment;
import version1.dishq.dishq.server.Config;
import version1.dishq.dishq.server.Response.DishDataInfo;
import version1.dishq.dishq.server.Response.HomeDishesResponse;
import version1.dishq.dishq.server.RestApi;
import version1.dishq.dishq.util.DishqApplication;
import version1.dishq.dishq.util.Util;

/**
 * Created by dishq on 14-12-2016.
 * Package name version1.dishq.dishq.
 */

public class HomeActivity extends BaseActivity {

    private static final String TAG = "HomeActivity";
    private int noOfPages = 0;
    public static ViewPager viewPager;
    private TextView greetingHeader, greetingContext;
    private Button greetingButton;
    private RelativeLayout rlGreeting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setTags();
        fetchHomeDishResults();
    }

    protected void setTags() {
        viewPager = (ViewPager) findViewById(R.id.homeViewPager);
        rlGreeting = (RelativeLayout) findViewById(R.id.rl_greeting);
        greetingHeader = (TextView) findViewById(R.id.greeting_heading);
        greetingContext = (TextView) findViewById(R.id.greeting_context);
        greetingButton = (Button) findViewById(R.id.greeting_button);
        setFonts();
    }

    protected void setFonts() {
        greetingHeader.setTypeface(Util.opensanslight);
        greetingContext.setTypeface(Util.opensanslight);
        greetingButton.setTypeface(Util.opensanssemibold);
    }

    public void greetingsShownView(HomeDishesResponse.HomeData body) {
        rlGreeting.setVisibility(View.VISIBLE);
        if(body!=null) {
            greetingHeader.setText(body.greetingsInfo.getGreetingMessage());
            greetingContext.setText(body.greetingsInfo.getContextMessage());
            greetingButton.setOnClickListener(onClickListener);
            rlGreeting.setOnClickListener(onClickListener);
        }
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            rlGreeting.setVisibility(View.GONE);
        }
    };

    public void fetchHomeDishResults() {
//        String latitude = Util.getLatitude();
//        String longitude = Util.getLongitude();
        String latitude = "12.92923258", longitude = "77.63082482";
        RestApi restApi = Config.createService(RestApi.class);
        Call<HomeDishesResponse> call = restApi.fetchPersonalDishes(DishqApplication.getAccessToken(), DishqApplication.getUniqueID(),
                latitude, longitude, -1, "", -1);
        call.enqueue(new Callback<HomeDishesResponse>() {
            @Override
            public void onResponse(Call<HomeDishesResponse> call, Response<HomeDishesResponse> response) {
                Log.d(TAG, "Success");
                try {
                    if(response.isSuccessful()) {
                        HomeDishesResponse.HomeData body = response.body().homeData;
                        if(body!=null) {
                            Log.d(TAG, "Body is not null");
                            Boolean showGreeting = body.getShowGreeting();
                            if(showGreeting) {
                                greetingsShownView(body);
                            }
                            Util.setDefaultTab(body.getDefaultTab());
                            for(int i = 0; i <body.dishDataInfos.size(); i++) {
                                    Util.dishDataModals = body.dishDataInfos;
                            }
                            noOfPages = body.dishDataInfos.size();
                            viewPager.setAdapter(new SectionsPagerAdapter(getSupportFragmentManager()));
                        }

                    }else {
                        String error = response.errorBody().string();
                        Log.d(TAG, error);
                    }
                }catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<HomeDishesResponse> call, Throwable t) {
                Log.d(TAG, "Failure");
            }
        });
    }


    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            HomeScreenFragment fragment = new HomeScreenFragment();
            fragment.setArguments(Util.dishDataModals.get(position).toBundle());
            // getItem is called to instantiate the fragment for the given page.
            // Return a HomeScreenFragment (defined as a static inner class below).
            return fragment;
        }

        @Override
        public int getCount() {
            // Show total pages.
            return noOfPages;
        }

    }
}
