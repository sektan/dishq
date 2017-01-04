package version1.dishq.dishq.fragments.tastePrefFragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;

import com.wefika.flowlayout.FlowLayout;

import version1.dishq.dishq.R;
import version1.dishq.dishq.adapters.CustomViewPager;
import version1.dishq.dishq.modals.HomeCuisinesModal;
import version1.dishq.dishq.modals.lists.HomeCuisineSelect;
import version1.dishq.dishq.ui.OnBoardingActivity;
import version1.dishq.dishq.util.Util;

/**
 * Created by dishq on 16-12-2016.
 * Package name version1.dishq.dishq.
 */

public class TastePrefFragment2 extends Fragment {

    CheckedTextView child;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_taste_pref_second, container, false);
        setTags(v);

        return v;
    }

    //For linking to xml ids of views
    protected void setTags(View view) {
        setTypeFace();
        FlowLayout homeCuisineContainer = (FlowLayout) view.findViewById(R.id.home_cuisine_container);
        homeCuisineContainer.removeAllViews();

        final View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckedTextView view = (CheckedTextView) v;
                view.setChecked(!view.isChecked());
                HomeCuisinesModal model = (HomeCuisinesModal) view.getTag();
                if (view.isChecked()) {
                    Log.d("Name of selected item", model.getHomeCuisName());
                    model.setHomeCuisCurrentSelect(true);
                    Util.homeCuisineSelected = true;
                    Util.homeCuisineSelects.add(new HomeCuisineSelect(model.getHomeCuisClassName(), model.getHomeCuisEntityId()));
                }else{
                    model.setHomeCuisCurrentSelect(false);
                    Util.homeCuisineSelected = false;
                }
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        showNext();
                    }
                }, 1000);

            }
        };
        for (HomeCuisinesModal model : Util.homeCuisinesModals) {
            child = (CheckedTextView) LayoutInflater.from(getContext()).inflate(R.layout.simple_selectable_list_item, homeCuisineContainer, false);
            child.setText(model.getHomeCuisName());
            child.setTag(model);
            child.setOnClickListener(clickListener);
            child.setChecked(false);
            if(child.isChecked()){
                model.setHomeCuisCurrentSelect(true);
            }
            else{
                model.setHomeCuisCurrentSelect(false);
            }
            homeCuisineContainer.addView(child);
            if (Util.homeCuisineSelected) {
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        showNext();
                    }
                }, 1000);
            }
        }
    }

    //For setting the font of the text visible to the user
    protected void setTypeFace() {

    }

    public static TastePrefFragment2 newInstance(String text) {
        TastePrefFragment2 f = new TastePrefFragment2();
        Bundle b = new Bundle();
        b.putString("msg", text);
        f.setArguments(b);
        return f;
    }

    void showNext() {
        if (Util.homeCuisineSelected) {
            if (OnBoardingActivity.pager.getCurrentItem() == 1) {
                OnBoardingActivity.pager.setPagingEnabled(CustomViewPager.SwipeDirection.all);
                OnBoardingActivity.pager.setCurrentItem(2);
            }else {
                OnBoardingActivity.pager.setPagingEnabled(CustomViewPager.SwipeDirection.top);
                OnBoardingActivity.pager.setCurrentItem(1);
            }
        }

    }
}