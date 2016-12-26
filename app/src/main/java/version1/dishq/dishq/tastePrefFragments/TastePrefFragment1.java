package version1.dishq.dishq.tastePrefFragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import version1.dishq.dishq.R;
import version1.dishq.dishq.modals.FoodChoicesModal;
import version1.dishq.dishq.ui.OnBoardingActivity;
import version1.dishq.dishq.util.DishqApplication;
import version1.dishq.dishq.util.Util;

/**
 * Created by dishq on 16-12-2016.
 * Package name version1.dishq.dishq.
 */

public class TastePrefFragment1 extends Fragment {

    private TextView hiName, aboutPrefs, vegetarian, eggetarian, nonVeg;
    private Button areYou;
    private ImageView vegDish, eggDish, nonVegDish, vegTick, eggTick, nonVegTick;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_taste_pref_first, container, false);
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
        vegDish = (ImageView) view.findViewById(R.id.veg_dish);
        eggDish = (ImageView) view.findViewById(R.id.egg_dish);
        nonVegDish = (ImageView) view.findViewById(R.id.non_veg_dish);
        vegTick = (ImageView) view.findViewById(R.id.veg_tick);
        eggTick = (ImageView) view.findViewById(R.id.egg_tick);
        nonVegTick = (ImageView) view.findViewById(R.id.non_veg_tick);
        vegDish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if(Util.foodChoicesModals!=null) {
                vegTick.setVisibility(View.VISIBLE);
                nonVegTick.setVisibility(View.GONE);
                eggTick.setVisibility(View.GONE);
                //settingOnClick(1, vegetarian);
                Util.setFoodChoiceSelected(1);
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        showNext();
                    }
                }, 1000);
                //}
            }
        });
        eggDish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Util.foodChoicesModals != null) {
                    eggTick.setVisibility(View.VISIBLE);
                    vegTick.setVisibility(View.GONE);
                    nonVegTick.setVisibility(View.GONE);
                    //settingOnClick(3, eggetarian);
                    Util.setFoodChoiceSelected(3);
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            showNext();
                        }
                    }, 1000);
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
                    //settingOnClick(2, nonVeg);
                    Util.setFoodChoiceSelected(2);
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            showNext();
                        }
                    }, 1000);
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

    public static TastePrefFragment1 newInstance(String text) {
        TastePrefFragment1 f = new TastePrefFragment1();
        Bundle b = new Bundle();
        b.putString("msg", text);
        f.setArguments(b);
        return f;
    }

    protected void settingOnClick(int n, TextView textView) {
        for (FoodChoicesModal modal : Util.foodChoicesModals) {
            for (int i = 0; i < Util.foodChoicesModals.size(); i++) {
                if (modal.getFoodChoiceValue() == n) {
                    modal.setFoodChoiceCurrSel(true);
                    textView.setText(modal.getFoodChoiceName());
                    Util.setFoodChoiceSelected(modal.getFoodChoiceValue());
                }
            }

        }

    }

    void showNext() {
        if (Util.getFoodChoiceSelected() != 0) {
            if (OnBoardingActivity.pager.getCurrentItem() == 0) {
                //OnBoardingActivity.pager.setPagingEnabled(CustomViewPager.SwipeDirection.DOWN);
                OnBoardingActivity.pager.setCurrentItem(1);
            }
        }

    }
}
