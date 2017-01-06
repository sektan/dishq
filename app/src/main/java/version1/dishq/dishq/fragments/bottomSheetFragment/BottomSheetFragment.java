package version1.dishq.dishq.fragments.bottomSheetFragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import version1.dishq.dishq.R;
import version1.dishq.dishq.util.Util;

/**
 * Created by dishq on 29-12-2016.
 * Package name version1.dishq.dishq.
 */

public class BottomSheetFragment extends BottomSheetDialogFragment {

    private BottomSheetAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private BottomSheetBehavior.BottomSheetCallback mBottomSheetCallBack = new BottomSheetBehavior.BottomSheetCallback() {
        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
            if(newState == BottomSheetBehavior.STATE_HIDDEN) {
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
        View rootView = inflater.inflate(R.layout.bottom_sheet_main, container);

        viewPager = (ViewPager) rootView.findViewById(R.id.viewpager);
        tabLayout = (TabLayout) rootView.findViewById(R.id.tabs);
        if (viewPager != null && tabLayout != null) {
            initViewPager();
        }


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
        if(behavior != null && behavior instanceof BottomSheetBehavior) {
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(mBottomSheetCallBack);
        }
    }

    private void initViewPager() {
        adapter = new BottomSheetAdapter(getChildFragmentManager());
        viewPager.setAdapter(adapter);
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
                if (Util.getDefaultTab().equals("dineout")) {
                    TabLayout.Tab tab = tabLayout.getTabAt(0);
                    if (tab != null) {
                        tab.select();
                    }
                    tabLayout.getTabAt(0).setIcon(R.drawable.dineout_active);
                    tabLayout.getTabAt(1).setIcon(R.drawable.delivery_inactive);
                    tabLayout.getTabAt(2).setIcon(R.drawable.cooking_inactive);
                } else if (Util.getDefaultTab().equals("delivery")) {
                    TabLayout.Tab tab = tabLayout.getTabAt(1);
                    if (tab != null) {
                        tab.select();
                    }
                    tabLayout.getTabAt(0).setIcon(R.drawable.dineout_inactive);
                    tabLayout.getTabAt(1).setIcon(R.drawable.delivery_active);
                    tabLayout.getTabAt(2).setIcon(R.drawable.cooking_inactive);
                } else if (Util.getDefaultTab().equals("recipe")) {
                    TabLayout.Tab tab = tabLayout.getTabAt(2);
                    if (tab != null) {
                        tab.select();
                    }
                    tabLayout.getTabAt(0).setIcon(R.drawable.dineout_inactive);
                    tabLayout.getTabAt(1).setIcon(R.drawable.delivery_inactive);
                    tabLayout.getTabAt(2).setIcon(R.drawable.cooking_active);
                }

                viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                        Log.d("page", position + " ");
                        if (tabLayout != null) {
                            if (position == 0) {
                                tabLayout.getTabAt(0).setIcon(R.drawable.dineout_active);
                                tabLayout.getTabAt(1).setIcon(R.drawable.delivery_inactive);
                                tabLayout.getTabAt(2).setIcon(R.drawable.cooking_inactive);
                            } else if (position == 1) {
                                tabLayout.getTabAt(0).setIcon(R.drawable.dineout_inactive);
                                tabLayout.getTabAt(1).setIcon(R.drawable.delivery_active);
                                tabLayout.getTabAt(2).setIcon(R.drawable.cooking_inactive);
                            } else if (position == 2) {
                                tabLayout.getTabAt(0).setIcon(R.drawable.dineout_inactive);
                                tabLayout.getTabAt(1).setIcon(R.drawable.delivery_inactive);
                                tabLayout.getTabAt(2).setIcon(R.drawable.cooking_active);
                            }
                        }
                    }

                    @Override
                    public void onPageSelected(int position) {
                        Log.d("page", position + " ");
                        if (tabLayout != null) {
                            if (position == 0) {
                                tabLayout.getTabAt(0).setIcon(R.drawable.dineout_active);
                                tabLayout.getTabAt(1).setIcon(R.drawable.delivery_inactive);
                                tabLayout.getTabAt(2).setIcon(R.drawable.cooking_inactive);
                            } else if (position == 1) {
                                tabLayout.getTabAt(0).setIcon(R.drawable.dineout_inactive);
                                tabLayout.getTabAt(1).setIcon(R.drawable.delivery_active);
                                tabLayout.getTabAt(2).setIcon(R.drawable.cooking_inactive);
                            } else if (position == 2) {
                                tabLayout.getTabAt(0).setIcon(R.drawable.dineout_inactive);
                                tabLayout.getTabAt(1).setIcon(R.drawable.delivery_inactive);
                                tabLayout.getTabAt(2).setIcon(R.drawable.cooking_active);
                            }
                        }
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });
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
                    frag = new DineoutFragment();
                    break;
                case 1:
                    frag = new DeliveryFragment();
                    break;
                case 2:
                    frag = new RecipeFragment();
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
}
