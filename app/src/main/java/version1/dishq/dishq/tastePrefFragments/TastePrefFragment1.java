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
import version1.dishq.dishq.util.Util;

/**
 * Created by dishq on 16-12-2016.
 * Package name version1.dishq.dishq.
 */

public class TastePrefFragment1 extends Fragment implements View.OnClickListener {

    private TextView hiName, aboutPrefs, vegetarian, eggetarian, nonVeg;
    private Button areYou;
    private ImageView vegDish, eggDish, nonVegDish;
    private FoodChoicesModal foodChoicesModal;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_taste_pref_first, container, false);
        setTags(v);

        return v;
    }

    //For linking to xml ids of views
    protected void setTags(View view) {
        hiName = (TextView) view.findViewById(R.id.hi_name);
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

    //For setting the font of the text visivle to the user
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.veg_dish:
                foodChoicesModal.setFoodChoiceCurrSel(true);
                vegetarian.setText(foodChoicesModal.getFoodChoiceName());
                Util.setFoodChoiceSelected(foodChoicesModal.getFoodChoiceValue());
                showNext();
            break;
            case R.id.egg_dish:
                foodChoicesModal.setFoodChoiceCurrSel(true);
                eggetarian.setText(foodChoicesModal.getFoodChoiceName());
                Util.setFoodChoiceSelected(foodChoicesModal.getFoodChoiceValue());
                showNext();
                break;
            case R.id.non_veg_dish:
                foodChoicesModal.setFoodChoiceCurrSel(true);
                nonVeg.setText(foodChoicesModal.getFoodChoiceName());
                Util.setFoodChoiceSelected(foodChoicesModal.getFoodChoiceValue());
                showNext();
        }
    }

    void showNext() {

    }
}
