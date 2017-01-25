package version1.dishq.dishq.fragments.bottomSheetFragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mixpanel.android.mpmetrics.MixpanelAPI;

import org.json.JSONException;
import org.json.JSONObject;

import biz.laenger.android.vpbs.BottomSheetUtils;
import version1.dishq.dishq.R;
import version1.dishq.dishq.util.DishqApplication;
import version1.dishq.dishq.util.Util;

/**
 * Created by dishq on 29-12-2016.
 * Package name version1.dishq.dishq.
 */

public class BottomSheetFragment extends BottomSheetDialogFragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private MixpanelAPI mixpanel = null;

    private BottomSheetBehavior.BottomSheetCallback mBottomSheetCallBack = new BottomSheetBehavior.BottomSheetCallback() {
        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                dismiss();
            }
        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {

        }
    };


    @NonNull
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //MixPanel Instantiation
        mixpanel = MixpanelAPI.getInstance(getActivity(), getResources().getString(R.string.mixpanel_token));

        View rootView = inflater.inflate(R.layout.bottom_sheet_main, container);
        viewPager = (ViewPager) rootView.findViewById(R.id.viewpager);
        tabLayout = (TabLayout) rootView.findViewById(R.id.tabs);
        if (viewPager != null && tabLayout != null) {
            initViewPager();
        }

        final ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.d("page", position + " ");
                if (tabLayout != null) {
                    if (position == 0) {
                        tabLayout.getTabAt(0).setIcon(R.drawable.dineout_active);
                        tabLayout.getTabAt(2).setIcon(R.drawable.delivery_inactive);
                        tabLayout.getTabAt(1).setIcon(R.drawable.cooking_inactive);
                    } else if (position == 1) {
                        tabLayout.getTabAt(0).setIcon(R.drawable.dineout_inactive);
                        tabLayout.getTabAt(2).setIcon(R.drawable.delivery_active);
                        tabLayout.getTabAt(1).setIcon(R.drawable.cooking_inactive);
                    } else if (position == 2) {
                        tabLayout.getTabAt(0).setIcon(R.drawable.dineout_inactive);
                        tabLayout.getTabAt(2).setIcon(R.drawable.delivery_inactive);
                        tabLayout.getTabAt(1).setIcon(R.drawable.cooking_active);
                    }
                }
            }

            @Override
            public void onPageSelected(int position) {
                Log.d("page", position + " ");
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        };
        viewPager.addOnPageChangeListener(pageChangeListener);
        viewPager.post(new Runnable() {
            @Override
            public void run() {
                pageChangeListener.onPageSelected(viewPager.getCurrentItem());
            }
        });
        return rootView;
    }

    @Override
    public void onViewCreated(View contentView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(contentView, savedInstanceState);
    }

    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View contentView = View.inflate(getContext(), R.layout.bottom_sheet_main, null);
        dialog.setContentView(contentView);
        CoordinatorLayout.LayoutParams layoutParams =
                (CoordinatorLayout.LayoutParams) ((View) contentView.getParent()).getLayoutParams();
        CoordinatorLayout.Behavior behavior = layoutParams.getBehavior();
        if (behavior != null && behavior instanceof BottomSheetBehavior) {
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(mBottomSheetCallBack);
        }
    }

    private void initViewPager() {
        FragmentManager manager = getChildFragmentManager();
        BottomSheetAdapter adapter = new BottomSheetAdapter(manager);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(10);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                if (Util.getDefaultTab().equals("dineout")) {
                    TabLayout.Tab tab = tabLayout.getTabAt(0);
                    if (tab != null) {
                        tab.select();
                    }
                    tabLayout.getTabAt(0).setIcon(R.drawable.dineout_active);
                    tabLayout.getTabAt(2).setIcon(R.drawable.delivery_inactive);
                    tabLayout.getTabAt(1).setIcon(R.drawable.cooking_inactive);

                } else if (Util.getDefaultTab().equals("delivery")) {
                    TabLayout.Tab tab = tabLayout.getTabAt(1);
                    if (tab != null) {
                        tab.select();
                    }
                    tabLayout.getTabAt(0).setIcon(R.drawable.dineout_inactive);
                    tabLayout.getTabAt(2).setIcon(R.drawable.delivery_inactive);
                    tabLayout.getTabAt(1).setIcon(R.drawable.cooking_active);

                } else if (Util.getDefaultTab().equals("recipe")) {
                    TabLayout.Tab tab = tabLayout.getTabAt(2);
                    if (tab != null) {
                        tab.select();
                    }
                    tabLayout.getTabAt(0).setIcon(R.drawable.dineout_inactive);
                    tabLayout.getTabAt(1).setIcon(R.drawable.cooking_inactive);
                    tabLayout.getTabAt(2).setIcon(R.drawable.delivery_active);
                }
            }
        });
    }

    private class BottomSheetAdapter extends FragmentPagerAdapter {

        BottomSheetAdapter(FragmentManager fm) {
            super(fm);
        }


        @Override
        public Fragment getItem(int position) {
            Fragment frag = null;
            switch (position) {
                case 0:
                    try {
                        final JSONObject properties = new JSONObject();
                        properties.put("Dineout restaurants", "bottomsheet");
                        mixpanel.track("Dineout restaurants", properties);
                    } catch (final JSONException e) {
                        throw new RuntimeException("Could not encode hour of the day in JSON");
                    }
                    frag = new DineoutFragment();
                    break;
                case 1:
                    try {
                        final JSONObject properties = new JSONObject();
                        properties.put("Delivery restaurants", "bottomsheet");
                        mixpanel.track("Delivery restaurants", properties);
                    } catch (final JSONException e) {
                        throw new RuntimeException("Could not encode hour of the day in JSON");
                    }
                    frag = new RecipeFragment();
                    break;
                case 2:
                    try {
                        final JSONObject properties = new JSONObject();
                        properties.put("Recipe", "bottomsheet");
                        mixpanel.track("Recipe", properties);
                    } catch (final JSONException e) {
                        throw new RuntimeException("Could not encode hour of the day in JSON");
                    }
                    frag = new DeliveryFragment();
                    break;
            }
            return frag;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return null;
        }
    }

    @Override
    public void onDestroy() {
        mixpanel.flush();
        super.onDestroy();
    }
}
