package version1.dishq.dishq.fragments.onboardingFragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.mixpanel.android.mpmetrics.MixpanelAPI;
import com.wefika.flowlayout.FlowLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import version1.dishq.dishq.R;
import version1.dishq.dishq.custom.CustomViewPager;
import version1.dishq.dishq.custom.OnSwipeTouchListener;
import version1.dishq.dishq.modals.FavCuisinesModal;
import version1.dishq.dishq.modals.lists.FavCuisineSelect;
import version1.dishq.dishq.ui.OnBoardingActivity;
import version1.dishq.dishq.util.Constants;
import version1.dishq.dishq.util.DishqApplication;
import version1.dishq.dishq.util.Util;

/**
 * Created by dishq on 16-12-2016.
 * Package name version1.dishq.dishq.
 */

public class TastePrefFragment3 extends Fragment {

    Button favCuisine;
    TextView pickThree;
    CheckedTextView child;
    private MixpanelAPI mixpanel = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //MixPanel Instantiation
        mixpanel = MixpanelAPI.getInstance(getActivity(), getResources().getString(R.string.mixpanel_token));

        View v = inflater.inflate(R.layout.fragment_taste_pref_third, container, false);
        setTags(v);

        v.setOnTouchListener(new OnSwipeTouchListener(getActivity()) {
            public void onSwipeRight() {
                DishqApplication.setHomeCuisineSelected(false);
                OnBoardingActivity.pager.setCurrentItem(1);
            }

            public void onSwipeLeft() {
                if (DishqApplication.favCuisineCount == 3) {
                    OnBoardingActivity.pager.setCurrentItem(3);
                }
            }
        });
        try {
            final JSONObject properties = new JSONObject();
            properties.put("Onboarding Screen 3", "onboarding");
            mixpanel.track("Onboarding Screen 3", properties);
        } catch (final JSONException e) {
            throw new RuntimeException("Could not encode hour of the day in JSON");
        }
        return v;
    }

    //For linking to xml ids of views
    protected void setTags(View view) {
        favCuisine = (Button) view.findViewById(R.id.your_home_cuisine);
        if (favCuisine != null) {
            favCuisine.setTypeface(Util.opensanslight);
        }

        pickThree = (TextView) view.findViewById(R.id.pick_three);
        if (pickThree != null) {
            pickThree.setTypeface(Util.opensanslight);
        }

        FlowLayout favCuisineContainer = (FlowLayout) view.findViewById(R.id.fav_cuisine_container);
        favCuisineContainer.removeAllViews();
        for (FavCuisinesModal model : Util.favCuisinesModals) {
            child = (CheckedTextView) LayoutInflater.from(getContext()).inflate(R.layout.simple_selectable_list_item, favCuisineContainer, false);
            child.setText(model.getFavCuisName());
            child.setTypeface(Util.opensansregular);
            child.setTag(model);

            if (model.getFavCuisCurrentSelect()) {
                child.setChecked(true);
            }
            favCuisineContainer.addView(child);
            child.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    CheckedTextView view = (CheckedTextView) v;
                    view.setChecked(!view.isChecked());
                    FavCuisinesModal model = (FavCuisinesModal) view.getTag();
                    if (view.isChecked()) {
                        Log.d("TasteFragment3", model.getFavCuisName());
                        model.setFavCuisCurrentSelect(true);
                        DishqApplication.favCuisineCount++;
                        DishqApplication.setFavCuisineCount(DishqApplication.getFavCuisineCount());
                        Util.favCuisineSelects.add(new FavCuisineSelect(model.getFavCuisClassName(), model.getFavCuisEntityId()));

                        if (DishqApplication.favCuisineCount == 3) {
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    DishqApplication.getPrefs().edit().putInt(Constants.IS_FRAGMENT_SEEN, 3).apply();
                                    DishqApplication.setFragmentSeen(3);
                                    showNext();
                                }
                            }, 400);
                        }
                    } else {
                        DishqApplication.favCuisineCount--;
                        DishqApplication.setFavCuisineCount(DishqApplication.getFavCuisineCount());
                        //DishqApplication.favCuisineSelects = new ArrayList<>();
                        Util.favCuisineSelects.remove(new FavCuisineSelect(model.getFavCuisClassName(), model.getFavCuisEntityId()));
                        model.setFavCuisCurrentSelect(false);
                    }
                }
            });
        }
    }

    void showNext() {
        Log.d("showNext is selected", "count of fav dishes:" + DishqApplication.getFavCuisineCount());
        Log.d("Next page", "next page is shown");
        if (OnBoardingActivity.pager.getCurrentItem() == 2) {
            OnBoardingActivity.pager.setCurrentItem(3);
        }
    }

    @Override
    public void onDestroy() {
        mixpanel.flush();
        super.onDestroy();
    }
}
