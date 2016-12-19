package version1.dishq.dishq.tastePrefFragments;

import android.os.Bundle;
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

public class TastePrefFragment1 extends Fragment implements View.OnClickListener {

    private TextView hiName, aboutPrefs, vegetarian, eggetarian, nonVeg;
    private Button areYou;
    private ImageView vegDish, eggDish, nonVegDish;

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
        vegDish.setOnClickListener(this);
        eggDish.setOnClickListener(this);
        nonVegDish.setOnClickListener(this);
    }

    //For setting the font of the text visible to the user
    protected void setTypeFace() {
        hiName.setTypeface(Util.opensanslight);
        aboutPrefs.setTypeface(Util.opensanslight);
        vegetarian.setTypeface(Util.opensansregular);
        eggetarian.setTypeface(Util.opensansregular);
        nonVeg.setTypeface(Util.opensansregular);areYou.setTypeface(Util.opensanslight);
    }

    public static TastePrefFragment1 newInstance(String text) {
        TastePrefFragment1 f = new TastePrefFragment1();
        Bundle b = new Bundle();
        b.putString("msg", text);
        f.setArguments(b);
        return f;
    }

    protected void settingOnClick(int n, TextView textView) {
        for (int i = 0; i < Util.foodChoicesModals.size(); i++) {
            if (Util.foodChoicesModals.get(i).getFoodChoiceValue() == n) {
                Util.foodChoicesModals.get(i).setFoodChoiceCurrSel(true);
                textView.setText(Util.foodChoicesModals.get(i).getFoodChoiceName());
                Util.setFoodChoiceSelected(Util.foodChoicesModals.get(i).getFoodChoiceValue());
            }
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.veg_dish:
                if(Util.foodChoicesModals!=null) {
                    settingOnClick(1, vegetarian);
                    showNext();
                }
                break;
            case R.id.egg_dish:
                if(Util.foodChoicesModals!=null) {
                    settingOnClick(2, eggetarian);
                    showNext();
                }
                break;
            case R.id.non_veg_dish:
                if(Util.foodChoicesModals!=null){
                    settingOnClick(3, nonVeg);
                    showNext();
                }

                break;
        }
    }

    void showNext() {
        if(Util.getFoodChoiceSelected()!=0) {
            if (OnBoardingActivity.pager.getCurrentItem() == 0) {
                //OnBoardingActivity.pager.setPagingEnabled(CustomViewPager.SwipeDirection.DOWN);
                OnBoardingActivity.pager.setCurrentItem(1);
            }
        }

    }
}
