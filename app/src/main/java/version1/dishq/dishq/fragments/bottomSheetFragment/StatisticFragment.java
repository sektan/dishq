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
import android.view.View;

import version1.dishq.dishq.R;

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
        viewPager.setAdapter(viewAdapter);
        viewPager.setOffscreenPageLimit(10);
        tabLayout.setupWithViewPager(viewPager);
    }

    private class BottomSheetAdapter extends FragmentPagerAdapter {

        private int[] imageResId = {
            R.drawable.dineout_inactive,
                R.drawable.delivery_inactive,
                R.drawable.cooking_inactive
        };

        BottomSheetAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int pos) {
            switch (pos) {
                case 0:
                    return DineoutFragment.newInstance("FirstFragment, Instance 1");
                case 1:
                    return DeliveryFragment.newInstance("SecondFragment, Instance 2");
                case 2:
                    return RecipeFragment.newInstance("ThirdFragment, Instance 3");
                default:
                    return DineoutFragment.newInstance("FirstFragment, Default");
            }
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Drawable image = ContextCompat.getDrawable(getContext(), imageResId[position]);
            image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
            SpannableString sb = new SpannableString(" ");
            ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BOTTOM);
            sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            return sb;
        }
    }

}
