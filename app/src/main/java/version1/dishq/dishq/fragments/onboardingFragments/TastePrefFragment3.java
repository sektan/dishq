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
import version1.dishq.dishq.modals.FavCuisinesModal;
import version1.dishq.dishq.modals.lists.FavCuisineSelect;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (DishqApplication.getFragmentSeen() >=3) {
            showNext();
        }
        View v = inflater.inflate(R.layout.fragment_taste_pref_third, container, false);
        setTags(v);

        return v;
    }

    //For linking to xml ids of views
    protected void setTags(View view) {
        favCuisine = (Button) view.findViewById(R.id.your_home_cuisine);
        if(favCuisine!=null) {
            favCuisine.setTypeface(Util.opensanslight);
        }

        pickThree = (TextView) view.findViewById(R.id.pick_three);
        if(pickThree!=null){
            pickThree.setTypeface(Util.opensanslight);
        }

        FlowLayout favCuisineContainer = (FlowLayout) view.findViewById(R.id.fav_cuisine_container);
        favCuisineContainer.removeAllViews();
        for (FavCuisinesModal model : Util.favCuisinesModals) {
            child = (CheckedTextView) LayoutInflater.from(getContext()).inflate(R.layout.simple_selectable_list_item, favCuisineContainer, false);
//            if(!DishqApplication.getHomeCuisineSelected().equals(model.getFavCuisName())) {
                child.setText(model.getFavCuisName());
                child.setTypeface(Util.opensansregular);
                child.setTag(model);
           // }
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
                        Log.d("Name of selected item", model.getFavCuisName());
                        model.setFavCuisCurrentSelect(true);
                        DishqApplication.favCuisineCount++;
                        DishqApplication.setFavCuisineCount(DishqApplication.getFavCuisineCount());
                        DishqApplication.getPrefs().edit().putInt(Constants.FAV_CUISINE_COUNT, DishqApplication.getFavCuisineCount()).apply();
                        DishqApplication.favCuisineSelects = new ArrayList<FavCuisineSelect>();
                        DishqApplication.favCuisineSelects.add(new FavCuisineSelect(model.getFavCuisClassName(), model.getFavCuisEntityId()));
                        Gson gson = new Gson();
                        String json = gson.toJson(DishqApplication.getFavCuisineSelects());
                        DishqApplication.getPrefs().edit().putString(Constants.HOME_CUISINE_SELECTS, json).apply();

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
                        DishqApplication.favCuisineSelects = new ArrayList<FavCuisineSelect>();
                        DishqApplication.favCuisineSelects.remove(new FavCuisineSelect(model.getFavCuisClassName(), model.getFavCuisEntityId()));
                        model.setFavCuisCurrentSelect(false);
                    }

                }
            });
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
        }
    }

    void showNext() {
        Log.d("showNext is selected", "count of fav dishes:" + DishqApplication.getFavCuisineCount());
        Log.d("Next page", "next page is shown");
        if (DishqApplication.favCuisineCount == 3) {
            Fragment fragment = new TastePrefFragment4();
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.setCustomAnimations(R.anim.enter_from_bottom, R.anim.exit_from_top, R.anim.enter_from_top, R.anim.exit_from_bottom);
        ft.replace(R.id.onboarding_screen4, fragment);
        ft.addToBackStack(null);
        ft.commit();
    }
    }
}
