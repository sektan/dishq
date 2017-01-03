package version1.dishq.dishq.fragments.bottomSheetFragment;

import android.app.Dialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import version1.dishq.dishq.R;
import version1.dishq.dishq.util.Util;

/**
 * Created by dishq on 29-12-2016.
 * Package name version1.dishq.dishq.
 */

public class StatisticFragment extends BottomSheetDialogFragment {

    private BottomSheetBehavior mBehavior;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        View rootView = View.inflate(getContext(), R.layout.bottom_sheet_main, null);

        viewPager = (ViewPager) rootView.findViewById(R.id.viewpager);
        tabLayout = (TabLayout) rootView.findViewById(R.id.tabs);
        if (viewPager != null && tabLayout != null) {
            initViewPager();
        }

        dialog.setContentView(rootView);
        mBehavior = BottomSheetBehavior.from((View) rootView.getParent());
        return dialog;
    }

    private void initViewPager() {
        BottomSheetAdapter viewAdapter = new BottomSheetAdapter(getFragmentManager());
        viewAdapter.addFrag(new DineoutFragment());
        viewAdapter.addFrag(new DeliveryFragment());
        viewAdapter.addFrag(new RecipeFragment());
        viewPager.setAdapter(viewAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.d("page", position + " ");
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

            @Override
            public void onPageSelected(int position) {
                Log.d("page", position + " ");
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

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
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
    }

    private class BottomSheetAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();

        BottomSheetAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        void addFrag(Fragment fragment) {
            mFragmentList.add(fragment);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return null;
        }


    }

}
