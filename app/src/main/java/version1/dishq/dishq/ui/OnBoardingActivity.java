package version1.dishq.dishq.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import version1.dishq.dishq.BaseActivity;
import version1.dishq.dishq.R;
import version1.dishq.dishq.custom.CustomViewPager;
import version1.dishq.dishq.fragments.onboardingFragments.TastePrefFragment1;
import version1.dishq.dishq.fragments.onboardingFragments.TastePrefFragment2;
import version1.dishq.dishq.fragments.onboardingFragments.TastePrefFragment3;
import version1.dishq.dishq.fragments.onboardingFragments.TastePrefFragment4;
import version1.dishq.dishq.modals.AllergyModal;
import version1.dishq.dishq.modals.FavCuisinesModal;
import version1.dishq.dishq.modals.FoodChoicesModal;
import version1.dishq.dishq.modals.HomeCuisinesModal;
import version1.dishq.dishq.server.Config;
import version1.dishq.dishq.server.Response.TastePrefData;
import version1.dishq.dishq.server.RestApi;
import version1.dishq.dishq.util.DishqApplication;
import version1.dishq.dishq.util.Util;

/**
 * Created by dishq on 15-12-2016.
 * Package name version1.dishq.dishq.
 */

public class OnBoardingActivity extends BaseActivity {

    private String TAG = "OnBoardingActivity";
    public static CustomViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);
        pager = (CustomViewPager) findViewById(R.id.customViewPager);
        pager.setPagingEnabled(CustomViewPager.SwipeDirection.NONE);
        fetchTastePref();

    }

    @Override
    protected void onResume() {
        super.onResume();
        hideSoftKeyboard();
    }

    @Override
    protected void onPause() {
        super.onPause();
        hideSoftKeyboard();
    }

    public void hideSoftKeyboard() {
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    public void fetchTastePref() {
        RestApi restApi = Config.createService(RestApi.class);
        int userId = DishqApplication.getUserID();
        Call<TastePrefData> request = restApi.fetchTastePref(DishqApplication.getUniqueID(), userId);
        request.enqueue(new Callback<TastePrefData>() {
            @Override
            public void onResponse(Call<TastePrefData> call, Response<TastePrefData> response) {
                Log.d(TAG, "Success");
                try {
                    if (response.isSuccessful()) {
                        TastePrefData.FoodPreferencesInfo body = response.body().foodPreferencesinfo;
                        if (body != null) {
                            Log.d(TAG, "");
                            Util.allergyModals.clear();
                            Util.foodChoicesModals.clear();
                            Util.favCuisinesModals.clear();
                            Util.homeCuisinesModals.clear();
                            Log.d(TAG, "Length of Food Allergies: " + body.foodAllergiesInfos.size());
                            for (int i = 0; i < body.foodAllergiesInfos.size(); i++) {
                                Util.allergyModals.add(new AllergyModal(body.foodAllergiesInfos.get(i).getAllergyClassName(),
                                        body.foodAllergiesInfos.get(i).getAllergyCurrentlySelect(), body.foodAllergiesInfos.get(i).getAllergyEntityId(),
                                        body.foodAllergiesInfos.get(i).getAllergyName(), body.foodAllergiesInfos.get(i).getAllergyFoodChoice()));
                                Log.d(TAG, "Data has been filled: " + Util.allergyModals.size());
                            }
                            for (int j = 0; j < body.foodChoicesInfos.size(); j++) {
                                Util.foodChoicesModals.add(new FoodChoicesModal(body.foodChoicesInfos.get(j).getFoodChoiceCurrSel(),
                                        body.foodChoicesInfos.get(j).getFoodChoiceName(), body.foodChoicesInfos.get(j).getFoodChoiceValue()));
                                Log.d(TAG, "Data has been filled: " + Util.foodChoicesModals.size());
                            }
                            for (int k = 0; k < body.favCuisineInfos.size(); k++) {
                                Util.favCuisinesModals.add(new FavCuisinesModal(body.favCuisineInfos.get(k).getFavCuisClassName(),
                                        body.favCuisineInfos.get(k).getFavCuisCurrentSelect(), body.favCuisineInfos.get(k).getFavCuisEntityId(),
                                        body.favCuisineInfos.get(k).getFavCuisName()));
                                Log.d(TAG, "Data has been filled: " + Util.favCuisinesModals.size());
                            }
                            for (int l = 0; l < body.homeCuisineInfos.size(); l++) {
                                Util.homeCuisinesModals.add(new HomeCuisinesModal(body.homeCuisineInfos.get(l).getHomeCuisClassName(),
                                        body.homeCuisineInfos.get(l).getHomeCuisCurrentSelect(), body.homeCuisineInfos.get(l).getHomeCuisEntityId(),
                                        body.homeCuisineInfos.get(l).getHomeCuisName()));
                                Log.d(TAG, "Data has been filled: " + Util.homeCuisinesModals.size());
                            }
//                            PageListener pageListener = new PageListener();
//                            pager.addOnPageChangeListener(pageListener);
                            MyPagerAdapter adapter = new MyPagerAdapter(getSupportFragmentManager());
                            pager.setAdapter(adapter);
                        }
                    } else {
                        String error = response.errorBody().string();
                        Log.d(TAG, error);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<TastePrefData> call, Throwable t) {
                Log.d(TAG, "Failure");
            }
        });
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {

        MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int pos) {
            Fragment frag = null;

            switch (pos) {
                case 0:
                    frag = new TastePrefFragment1();
                    break;
                case 1:
                    frag = new TastePrefFragment2();
                    break;
                case 2:
                    frag = new TastePrefFragment3();
                    break;
                case 3:
                    frag = new TastePrefFragment4();
                    break;
            }
            return frag;
        }

        @Override
        public int getCount() {
            return 4;
        }
    }

}
