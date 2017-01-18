package version1.dishq.dishq.fragments.onboardingFragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mixpanel.android.mpmetrics.MixpanelAPI;

import org.json.JSONException;
import org.json.JSONObject;

import version1.dishq.dishq.R;
import version1.dishq.dishq.custom.CustomViewPager;
import version1.dishq.dishq.custom.OnSwipeTouchListener;
import version1.dishq.dishq.ui.OnBoardingActivity;
import version1.dishq.dishq.util.Constants;
import version1.dishq.dishq.util.DishqApplication;
import version1.dishq.dishq.util.Util;

/**
 * Created by dishq on 16-12-2016.
 * Package name version1.dishq.dishq.
 */

public class TastePrefFragment1 extends Fragment {

    private TextView hiName, aboutPrefs, vegetarian, eggetarian, nonVeg;
    private Button areYou;
    private RelativeLayout vegDish, eggDish, nonVegDish;
    private ImageView vegTick, eggTick, nonVegTick;
    private FrameLayout vegSelection, eggSelection, nonVegSelection;
    private MixpanelAPI mixpanel = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //MixPanel Instantiation
        mixpanel = MixpanelAPI.getInstance(getActivity(), getResources().getString(R.string.mixpanel_token));
        View v = inflater.inflate(R.layout.fragment_taste_pref_first, container, false);
        v.setOnTouchListener(new OnSwipeTouchListener(getActivity()) {
            public void onSwipeRight() {
                //do nothing
            }

            public void onSwipeLeft() {
                if(DishqApplication.getFoodChoiceSelected()!=0) {
                    OnBoardingActivity.pager.setCurrentItem(1);
                }
            }
        });
        try {
            final JSONObject properties = new JSONObject();
            properties.put("Onboarding Screen 1", "onboarding");
            mixpanel.track("Onboarding Screen 1", properties);
        } catch (final JSONException e) {
            throw new RuntimeException("Could not encode hour of the day in JSON");
        }
        setTags(v);
        return v;
    }

    //For linking to xml ids of views
    protected void setTags(View view) {
        hiName = (TextView) view.findViewById(R.id.hi_name);
        hiName.setText("Hi " + DishqApplication.getUserName());
        aboutPrefs = (TextView) view.findViewById(R.id.tell_about_prefs);
        vegetarian = (TextView) view.findViewById(R.id.vegetarian);
        eggetarian = (TextView) view.findViewById(R.id.eggetarian);
        nonVeg = (TextView) view.findViewById(R.id.non_veg);
        areYou = (Button) view.findViewById(R.id.are_you);
        setTypeFace();
        vegDish = (RelativeLayout) view.findViewById(R.id.veg_dish);
        eggDish = (RelativeLayout) view.findViewById(R.id.egg_dish);
        nonVegDish = (RelativeLayout) view.findViewById(R.id.non_veg_dish);
        vegTick = (ImageView) view.findViewById(R.id.veg_tick);
        eggTick = (ImageView) view.findViewById(R.id.egg_tick);
        nonVegTick = (ImageView) view.findViewById(R.id.non_veg_tick);
        vegSelection = (FrameLayout) view.findViewById(R.id.veg_selection);
        eggSelection = (FrameLayout) view.findViewById(R.id.egg_selection);
        nonVegSelection = (FrameLayout) view.findViewById(R.id.non_veg_selection);

        //Setting the onClickListeners
        vegDish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vegTick.setVisibility(View.VISIBLE);
                nonVegTick.setVisibility(View.GONE);
                eggTick.setVisibility(View.GONE);
                vegSelection.setVisibility(View.VISIBLE);
                eggSelection.setVisibility(View.GONE);
                nonVegSelection.setVisibility(View.GONE);
                DishqApplication.getPrefs().edit().putInt(Constants.FOOD_CHOICE_SELECTED, 1).apply();
                DishqApplication.setFoodChoiceSelected(1);
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        DishqApplication.getPrefs().edit().putInt(Constants.IS_FRAGMENT_SEEN, 1).apply();
                        DishqApplication.setFragmentSeen(1);
                        showNext();
                    }
                }, 400);
            }
        });
        eggDish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Util.foodChoicesModals != null) {
                    eggTick.setVisibility(View.VISIBLE);
                    vegTick.setVisibility(View.GONE);
                    nonVegTick.setVisibility(View.GONE);
                    vegSelection.setVisibility(View.GONE);
                    eggSelection.setVisibility(View.VISIBLE);
                    nonVegSelection.setVisibility(View.GONE);
                    DishqApplication.getPrefs().edit().putInt(Constants.FOOD_CHOICE_SELECTED, 2).apply();
                    DishqApplication.setFoodChoiceSelected(2);
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            DishqApplication.getPrefs().edit().putInt(Constants.IS_FRAGMENT_SEEN, 1).apply();
                            DishqApplication.setFragmentSeen(1);
                            showNext();
                        }
                    }, 400);
                }
            }
        });
        nonVegDish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Util.foodChoicesModals != null) {
                    nonVegTick.setVisibility(View.VISIBLE);
                    vegTick.setVisibility(View.GONE);
                    eggTick.setVisibility(View.GONE);
                    vegSelection.setVisibility(View.GONE);
                    eggSelection.setVisibility(View.GONE);
                    nonVegSelection.setVisibility(View.VISIBLE);
                    DishqApplication.getPrefs().edit().putInt(Constants.FOOD_CHOICE_SELECTED, 1).apply();
                    DishqApplication.setFoodChoiceSelected(3);
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            DishqApplication.getPrefs().edit().putInt(Constants.IS_FRAGMENT_SEEN, 1).apply();
                            DishqApplication.setFragmentSeen(1);
                            showNext();
                        }
                    }, 400);
                }
            }
        });
    }

    //For setting the font of the text visible to the user
    protected void setTypeFace() {
        hiName.setTypeface(Util.opensanslight);
        aboutPrefs.setTypeface(Util.opensanslight);
        vegetarian.setTypeface(Util.opensansregular);
        eggetarian.setTypeface(Util.opensansregular);
        nonVeg.setTypeface(Util.opensansregular);
        areYou.setTypeface(Util.opensanslight);
    }

    void showNext() {
        // if(DishqApplication.getFoodChoiceSelected()!=0) {
        if (OnBoardingActivity.pager.getCurrentItem() == 0) {
            //OnBoardingActivity.pager.setPagingEnabled(CustomViewPager.SwipeDirection.BOTH);
            OnBoardingActivity.pager.setCurrentItem(1);
        }
    }

    @Override
    public void onDestroy() {
        mixpanel.flush();
        super.onDestroy();
    }

}
