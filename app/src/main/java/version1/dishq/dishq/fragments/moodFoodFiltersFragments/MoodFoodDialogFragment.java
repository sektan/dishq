package version1.dishq.dishq.fragments.moodFoodFiltersFragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import version1.dishq.dishq.R;
import version1.dishq.dishq.server.Config;
import version1.dishq.dishq.server.Response.MoodFoodFiltersResponse;
import version1.dishq.dishq.server.RestApi;
import version1.dishq.dishq.ui.HomeActivity;
import version1.dishq.dishq.util.DishqApplication;
import version1.dishq.dishq.util.Util;

/**
 * Created by dishq on 07-02-2017.
 * Package name version1.dishq.dishq.
 */

public class MoodFoodDialogFragment extends DialogFragment implements View.OnClickListener {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Button buttonReset, buttonApply;
    private Boolean isMoodItemSelected = false;
    private Boolean isFoodItemSelected = false;
    private Boolean isSearchFoodItemSelected = false;
    ProgressBar progressBar;
    private Fragment frag = null;
    private View viewFrag = null;
    private int quickFilterPosition = -1;
    private int moodPosition = -1;
    private String FilterName = "";
    private String MoodName = "";
    private int moodFilterId = -1, filterEntityId = -1;
    private String filterClassName = "";
    private Boolean resetIsEnabled = false;
    private MoodFilterFragment moodFilterFragment;
    private FoodFilterFragment foodFilterFragment;
    private static final String TAG = "MoodFoodFilters";

    //Empty Constructor
    public MoodFoodDialogFragment() {
    }

    //Getting new instance of the Fragment
    public static MoodFoodDialogFragment getInstance() {
        return new MoodFoodDialogFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedinstanceState) {
        return inflater.inflate(R.layout.dialog_fragment_mood_food_filter, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        getDialog().getWindow().setLayout(width, dpToPx(480));
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /**
         * Setting the dialog close button
         */
        assert view != null;
        ImageView imageClose = (ImageView) view.findViewById(R.id.filter_image_close);
        imageClose.setOnClickListener(this);

        // Initially the progressbar visibility is set GONE in XML
        progressBar = (ProgressBar) view.findViewById(R.id.filter_progress_bar);
        progressBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(getActivity(), R.color.rupeeGreen), PorterDuff.Mode.MULTIPLY);

        /**
         * Setting the Reset Button
         */
        buttonReset = (Button) view.findViewById(R.id.filter_button_reset);
        buttonReset.setTypeface(Util.opensanssemibold);
        buttonReset.setOnClickListener(this);

        // Enable reset button
        if (!Util.getMoodName().equals("")) {
            toggleResetButton(true);
        } else if (!Util.getFilterName().equals("")) {
            toggleResetButton(true);
        } else {
            toggleResetButton(false);
        }
        /**********************************************************************************/

        /**
         * Setting the Apply Button
         */
        buttonApply = (Button) view.findViewById(R.id.filter_button_apply);
        buttonApply.setTypeface(Util.opensanssemibold);
        buttonApply.setOnClickListener(this);
        // Initially disable the buttons and click events

        if (getFoodItemSelected()) {
            toggleApplyButton(true);
        } else if (getMoodItemSelected()) {
            toggleApplyButton(true);
        } else if (getSearchFoodItemSelected()) {
            toggleApplyButton(true);
        } else {
            toggleApplyButton(false);
        }
        /**********************************************************************************/

        viewPager = (ViewPager) view.findViewById(R.id.filter_viewpager);
        tabLayout = (TabLayout) view.findViewById(R.id.filters_tab_layout);
        fetchFilters();
    }

    public static void closeKeyboard(Context c, IBinder windowToken) {
        InputMethodManager mgr = (InputMethodManager) c.getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(windowToken, 0);
    }

    /**
     * All the click events
     * @param view onClick events registered from the Filters dialogBox
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.filter_image_close:
                getDialog().dismiss();
                break;

            case R.id.filter_button_reset:
                setResetIsEnabled(true);

                if(moodFilterFragment!=null)
                moodFilterFragment.clearSelection(true);
                if(foodFilterFragment!=null)
                foodFilterFragment.clearSelection(true);
                setMoodFilterId(-1);
                setMoodName("");
                setFilterName("");
                setFilterClassName("");
                setFilterEntityId(-1);
                setQuickFilterPosition(-1);
                setMoodPosition(-1);
                toggleResetButton(false);
                toggleApplyButton(true);
                break;

            case R.id.filter_button_apply:
                if (getFoodItemSelected()) {
                    setFoodItemSelected(false);
                } else if (getMoodItemSelected()) {
                    setMoodItemSelected(false);
                } else if (getSearchFoodItemSelected()) {
                    Util.setItemSetFromSearch(true);
                    setSearchFoodItemSelected(false);
                }

                Util.setPageNumber(1);
                Util.setMoodFilterId(getMoodFilterId());
                Util.setMoodName(getMoodName());
                Util.setFilterName(getFilterName());
                Util.setFilterClassName(getFilterClassName());
                Util.setFilterEntityId(getFilterEntityId());
                Util.setQuickFilterPosition(getQuickFilterPosition());
                Util.setMoodPosition(getMoodPosition());

                Util.setHomeRefreshRequired(true);
                Util.setCurrentPage(0);
                Intent intent = new Intent(getActivity(), HomeActivity.class);
                startActivity(intent);
                dismiss();
                break;
        }
    }

    /**
     * Initializing the ViewPager
     */
    private void initViewPager() {
        FragmentManager manager = getChildFragmentManager();
        FiltersAdapter adapter = new FiltersAdapter(manager);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(1);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                TabLayout.Tab tab = tabLayout.getTabAt(0);
                if (tab != null) {
                    tab.select();
                }
                tabLayout.getTabAt(0).setText("MOOD");
                tabLayout.getTabAt(1).setText("FOOD");
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                closeKeyboard(getActivity(), viewPager.getWindowToken());
            }

            @Override
            public void onPageScrolled(int position, float offset, int offsetPixels) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    public void toggleResetButton(boolean isEnabled) {
        buttonReset.setEnabled(isEnabled);
    }

    public void toggleApplyButton(boolean isEnabled) {
        buttonApply.setEnabled(isEnabled);
    }

    /**
     * Setting up the adapter for the two fragments
     */
    private class FiltersAdapter extends FragmentPagerAdapter {

        public FiltersAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    moodFilterFragment = MoodFilterFragment.getInstance();
                    frag = moodFilterFragment;
                    //viewPager.setId(0);

                    break;
                case 1:
                    foodFilterFragment = FoodFilterFragment.getInstance();
                    frag = foodFilterFragment;
                    //viewPager.setId(1);
                    break;
            }
            return frag;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return null;
        }
    }

    /**
     * Making a retrofit call to retrieve Mood and Food Filters
     */
    protected void fetchFilters() {
        RestApi restApi = Config.createService(RestApi.class);
        Call<MoodFoodFiltersResponse> call = restApi.getMoodFoodFilters(DishqApplication.getAccessToken());
        call.enqueue(new Callback<MoodFoodFiltersResponse>() {
            @Override
            public void onResponse(Call<MoodFoodFiltersResponse> call, Response<MoodFoodFiltersResponse> response) {
                Log.d(TAG, "Success");
                try {
                    if (response.isSuccessful()) {
                        if (progressBar != null && progressBar.isShown())
                            progressBar.setVisibility(View.GONE);
                        MoodFoodFiltersResponse.MoodFoodFiltersInfo body = response.body().moodFoodFiltersInfo;
                        if (body != null) {
                            Util.moodFiltersInfos.clear();
                            Util.foodFiltersInfos.clear();
                            for (int i = 0; i < body.getMoodFiltersInfos().size(); i++) {
                                Util.moodFiltersInfos = body.getMoodFiltersInfos();
                            }
                            for (int j = 0; j < body.getFoodFiltersInfos().size(); j++) {
                                Util.foodFiltersInfos = body.getFoodFiltersInfos();
                            }
                            if (viewPager != null && tabLayout != null) {
                                initViewPager();
                            }
                        }
                    } else {
                        if (progressBar != null && progressBar.isShown())
                            progressBar.setVisibility(View.GONE);
                        String error = response.errorBody().string();
                        Log.d(TAG, "Error: " + error);
                    }
                } catch (IOException e) {
                    if (progressBar != null && progressBar.isShown())
                        progressBar.setVisibility(View.GONE);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<MoodFoodFiltersResponse> call, Throwable t) {
                if (progressBar != null && progressBar.isShown())
                    progressBar.setVisibility(View.GONE);
                Log.d(TAG, "Failure");
            }
        });
    }


    /**
     * Getters and setters
     */
    public Boolean getMoodItemSelected() {
        return isMoodItemSelected;
    }

    public void setMoodItemSelected(Boolean moodItemSelected) {
        isMoodItemSelected = moodItemSelected;
    }

    public Boolean getFoodItemSelected() {
        return isFoodItemSelected;
    }

    public void setFoodItemSelected(Boolean foodItemSelected) {
        isFoodItemSelected = foodItemSelected;
    }

    public Boolean getSearchFoodItemSelected() {
        return isSearchFoodItemSelected;
    }

    public void setSearchFoodItemSelected(Boolean searchFoodItemSelected) {
        isSearchFoodItemSelected = searchFoodItemSelected;
    }

    public Boolean getResetIsEnabled() {
        return resetIsEnabled;
    }

    public void setResetIsEnabled(Boolean resetIsEnabled) {
        this.resetIsEnabled = resetIsEnabled;
    }

    public int getMoodPosition() {
        return moodPosition;
    }

    public void setMoodPosition(int moodPosition) {
        this.moodPosition = moodPosition;
    }

    public int getQuickFilterPosition() {
        return quickFilterPosition;
    }

    public void setQuickFilterPosition(int quickFilterPosition) {
        this.quickFilterPosition = quickFilterPosition;
    }

    public String getFilterName() {
        return FilterName;
    }

    public void setFilterName(String filterName) {
        FilterName = filterName;
    }

    public String getMoodName() {
        return MoodName;
    }

    public void setMoodName(String moodName) {
        MoodName = moodName;
    }

    public int getMoodFilterId() {
        return moodFilterId;
    }

    public void setMoodFilterId(int moodFilterId) {
        this.moodFilterId = moodFilterId;
    }

    public int getFilterEntityId() {
        return filterEntityId;
    }

    public void setFilterEntityId(int filterEntityId) {
        this.filterEntityId = filterEntityId;
    }

    public String getFilterClassName() {
        return filterClassName;
    }

    public void setFilterClassName(String filterClassName) {
        this.filterClassName = filterClassName;
    }
/********************************************************************************/
}
