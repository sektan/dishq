package version1.dishq.dishq.fragments.dialogfragment.filters;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.List;

import version1.dishq.dishq.R;
import version1.dishq.dishq.fragments.dialogfragment.filters.models.quickfilters.Data;
import version1.dishq.dishq.fragments.dialogfragment.filters.models.quickfilters.FoodMoodFilter;
import version1.dishq.dishq.fragments.dialogfragment.filters.models.quickfilters.QuickFilter;
import version1.dishq.dishq.fragments.dialogfragment.filters.models.searchfilters.Datum;
import version1.dishq.dishq.ui.HomeActivity;
import version1.dishq.dishq.util.Util;

/**
 * Created by kavin.prabhu on 28/12/16.
 *
 */

public class FiltersDialogFragment extends DialogFragment implements View.OnClickListener {

    public List<FoodMoodFilter> foodMoodFilterList;
    public List<QuickFilter> quickFilterList;
    FilterPresenter presenter;
    FrameLayout fragmentContainer;
    TabLayout tabLayout;
    ProgressBar progressBar;
    Button buttonReset, buttonApply;
    ImageView imageClose;
    MoodFragment moodFragment;
    QuickFiltersFragment quickFiltersFragment;

    public FiltersDialogFragment() {
    }

    public static FiltersDialogFragment getInstance() {
        return new FiltersDialogFragment();
    }

    private static final String TAG = "FiltersDialogFragment";
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new FilterPresenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_fragment_filter, container, false);
    }

    @Override
    public void onResume() {
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        getDialog().getWindow().setLayout(width, dpToPx(480));
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        super.onResume();
    }

    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initially the visibility of container is GONE in XML
        fragmentContainer = (FrameLayout) view.findViewById(R.id.filter_container);

        imageClose = (ImageView) view.findViewById(R.id.filter_image_close);
        imageClose.setOnClickListener(this);

        // Initially the progressbar visibility is set GONE in XML
        progressBar = (ProgressBar) view.findViewById(R.id.filter_progress_bar);
        progressBar.getIndeterminateDrawable().setColorFilter(Color.BLACK, PorterDuff.Mode.MULTIPLY);

        buttonReset = (Button) view.findViewById(R.id.filter_button_reset);
        buttonReset.setOnClickListener(this);
        // Initially disable the buttons and click events
        buttonReset.setEnabled(false);

        buttonApply = (Button) view.findViewById(R.id.filter_button_apply);
        buttonApply.setOnClickListener(this);
        // Initially disable the buttons and click events
        buttonApply.setEnabled(false);

        tabLayout = (TabLayout) view.findViewById(R.id.filters_tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("MOOD"), 0, true);
        tabLayout.addTab(tabLayout.newTab().setText("FILTERS"), 1);
        // Initially disable the tab click events
        tabLayout.setClickable(false);
        tabLayout.setEnabled(false);

        presenter.getFilterResults(new FilterPresenter.FilterResultsCallback() {
            @Override
            public void onSuccess(Data data) {
                if (progressBar != null && progressBar.isShown())
                    progressBar.setVisibility(View.GONE);

                fragmentContainer.setVisibility(View.VISIBLE);

                foodMoodFilterList = data.getFoodMoodFilters();
                quickFilterList = data.getQuickFilters();

                initializeFragments();
            }

            @Override
            public void onFailure() {
                if (progressBar != null && progressBar.isShown())
                    progressBar.setVisibility(View.GONE);

                initializeFragments();
            }
        });
    }

    private void setCurrentTabFragment(int position) {
        switch (position) {
            case 0:
                replaceFragment(moodFragment);
                break;

            case 1:
                replaceFragment(quickFiltersFragment);
                break;
        }
    }

    private void replaceFragment(Fragment fragment) {
        if (getActivity() != null) {
            FragmentManager fm = getChildFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.filter_container, fragment, fragment.getTag());
            if (getActivity() != null)
                ft.commit();
        }
    }

    @Override
    public void onClick(View v) {
        Fragment fragment = getChildFragmentManager().findFragmentById(R.id.filter_container);
        switch (v.getId()) {
            case R.id.filter_button_reset:
                if (fragment instanceof MoodFragment) {
                    ((MoodFragment) fragment).clearSelection();
                    Util.setMoodPosition(-1);
                } else if (fragment instanceof QuickFiltersFragment) {
                    ((QuickFiltersFragment) fragment).clearSelection();
                    Util.setFilterName("");
                }
                // Set the apply button disabled
                toggleApplyButton(false);
                break;

            case R.id.filter_button_apply:
                if (fragment instanceof MoodFragment) {
                    FoodMoodFilter moodFilter = ((MoodFragment) fragment).getSelectedItem();

                    Util.setMoodFilterId(moodFilter.getFoodMoodId());
                   // Toast.makeText(getActivity(), moodFilter.getName() + ", " + moodFilter.getFoodMoodId(), Toast.LENGTH_SHORT).show();
                } else if (fragment instanceof QuickFiltersFragment) {
                    Object selectedItem = ((QuickFiltersFragment) fragment).getSelectedItem();
                    if (selectedItem instanceof Datum) {
                        Util.setFilterName(((Datum) selectedItem).getName());
                        Util.setFilterClassName(((Datum) selectedItem).getClassName());
                        Util.setFilterEntityId(((Datum) selectedItem).getEntityId());
//                        Toast.makeText(getActivity(), ((Datum) selectedItem).getName() + ", "
//                                + ((Datum) selectedItem).getClassName() + ", "
//                                + ((Datum) selectedItem).getEntityId(), Toast.LENGTH_SHORT).show();
                    } else if (selectedItem instanceof QuickFilter) {

                        Util.setFilterName(((QuickFilter) selectedItem).getName());
                        Util.setFilterClassName(((QuickFilter) selectedItem).getClassName());
                        Util.setFilterEntityId(((QuickFilter) selectedItem).getEntityId());
//                        Toast.makeText(getActivity(), ((QuickFilter) selectedItem).getName() + ", "
//                                + ((QuickFilter) selectedItem).getClassName() + ", "
//                                + ((QuickFilter) selectedItem).getEntityId(), Toast.LENGTH_SHORT).show();
                    }
                }
                Util.setHomeRefreshRequired(true);
                Intent intent = new Intent(getActivity(), HomeActivity.class);
                startActivity(intent);
                dismiss();
                break;

            case R.id.filter_image_close:
                getDialog().dismiss();
                break;
        }
    }

    public void initializeFragments() {
        moodFragment = MoodFragment.getInstance();
        quickFiltersFragment = QuickFiltersFragment.getInstance();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                setCurrentTabFragment(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        int selectedTabPosition = tabLayout.getSelectedTabPosition();
        switch (selectedTabPosition) {
            case 0:
                setCurrentTabFragment(0);
                break;

            case 1:
                setCurrentTabFragment(1);
                break;

            default:
                setCurrentTabFragment(0);
                break;
        }

        tabLayout.setClickable(true);
        tabLayout.setEnabled(true);

        // Enable reset button
        buttonReset.setEnabled(true);
    }

    public void toggleApplyButton(boolean isEnabled) {
        buttonApply.setEnabled(isEnabled);
    }

}
