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
import com.wefika.flowlayout.FlowLayout;

import java.util.ArrayList;

import version1.dishq.dishq.R;
import version1.dishq.dishq.custom.OnSwipeListener;
import version1.dishq.dishq.modals.HomeCuisinesModal;
import version1.dishq.dishq.modals.lists.HomeCuisineSelect;
import version1.dishq.dishq.util.Constants;
import version1.dishq.dishq.util.DishqApplication;
import version1.dishq.dishq.util.Util;

/**
 * Created by dishq on 16-12-2016.
 * Package name version1.dishq.dishq.
 */

public class TastePrefFragment2 extends Fragment {

    Button homeCuisine;
    TextView pickOne;
    CheckedTextView child;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (DishqApplication.getFragmentSeen() >=2) {
            showNext();
        }
        View v = inflater.inflate(R.layout.fragment_taste_pref_second, container, false);
        setTags(v);

        return v;
    }

    //For linking to xml ids of views
    protected void setTags(View view) {
        homeCuisine = (Button) view.findViewById(R.id.your_home_cuisine);
        pickOne = (TextView) view.findViewById(R.id.pick_one);
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
                    DishqApplication.getPrefs().edit().putBoolean(Constants.HOME_CUISINE_SELECTED, true).apply();
                    DishqApplication.setHomeCuisineSelected(true);
                    DishqApplication.homeCuisineSelects = new ArrayList<>();
                    DishqApplication.homeCuisineSelects.add(new HomeCuisineSelect(model.getHomeCuisClassName(), model.getHomeCuisEntityId()));
                    Gson gson = new Gson();
                    String json = gson.toJson(DishqApplication.homeCuisineSelects);
                    DishqApplication.getPrefs().edit().putString(Constants.HOME_CUISINE_SELECTS, json).apply();
                }else{
                    model.setHomeCuisCurrentSelect(false);
                    DishqApplication.homeCuisineSelects = new ArrayList<>();
                    DishqApplication.homeCuisineSelects.remove(new HomeCuisineSelect(model.getHomeCuisClassName(), model.getHomeCuisEntityId()));
                    Gson gson = new Gson();
                    String json = gson.toJson(DishqApplication.homeCuisineSelects);
                    DishqApplication.getPrefs().edit().putString(Constants.HOME_CUISINE_SELECTS, json).apply();
                    DishqApplication.getPrefs().edit().putBoolean(Constants.HOME_CUISINE_SELECTED, false).apply();
                    DishqApplication.setHomeCuisineSelected(false);
                }
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (DishqApplication.getHomeCuisineSelected()) {
                            DishqApplication.getPrefs().edit().putInt(Constants.IS_FRAGMENT_SEEN, 2).apply();
                            DishqApplication.setFragmentSeen(2);
                            showNext();
                        }
                    }
                }, 400);
            }
        };
        for (HomeCuisinesModal model : Util.homeCuisinesModals) {
            child = (CheckedTextView) LayoutInflater.from(getContext()).inflate(R.layout.simple_selectable_list_item, homeCuisineContainer, false);
            child.setText(model.getHomeCuisName());
            child.setTypeface(Util.opensansregular);
            child.setTag(model);
            if(!DishqApplication.getHomeCuisineSelected()) {
                child.setOnClickListener(clickListener);
            }
            child.setChecked(false);
            if(child.isChecked()){
                model.setHomeCuisCurrentSelect(true);
            }
            else{
                model.setHomeCuisCurrentSelect(false);
            }
            homeCuisineContainer.addView(child);
            if (DishqApplication.getHomeCuisineSelected()) {
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                            DishqApplication.getPrefs().edit().putInt(Constants.IS_FRAGMENT_SEEN, 2).apply();
                            DishqApplication.setFragmentSeen(2);
                            showNext();
                    }
                }, 400);
            }
        }
    }

    //For setting the font of the text visible to the user
    protected void setTypeFace() {
        homeCuisine.setTypeface(Util.opensanslight);
        pickOne.setTypeface(Util.opensanslight);
    }

    //Calling the next Fragment
    void showNext() {
        if (DishqApplication.getHomeCuisineSelected()) {
            Fragment fragment = new TastePrefFragment3();
            FragmentManager fm = getActivity().getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.setCustomAnimations(R.anim.enter_from_bottom, R.anim.exit_from_top, R.anim.enter_from_top, R.anim.exit_from_bottom);
            ft.replace(R.id.onboarding_screen3, fragment);
            ft.addToBackStack(null);
            ft.commit();
        }

    }

    private class OnboardingSwipeListener extends OnSwipeListener {
        @Override
        public boolean onSwipe(Direction direction) {
            return true;
        }
    }
}
