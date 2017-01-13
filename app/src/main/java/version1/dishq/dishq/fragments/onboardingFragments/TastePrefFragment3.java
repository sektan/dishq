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

import com.wefika.flowlayout.FlowLayout;

import version1.dishq.dishq.R;
import version1.dishq.dishq.modals.FavCuisinesModal;
import version1.dishq.dishq.modals.lists.FavCuisineSelect;
import version1.dishq.dishq.util.Util;

/**
 * Created by dishq on 16-12-2016.
 * Package name version1.dishq.dishq.
 */

public class TastePrefFragment3 extends Fragment {

    Button favCuisine;
    TextView pickThree;
    CheckedTextView child;
    int checkcount = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_taste_pref_third, container, false);
        setTags(v);

        return v;
    }

    //For linking to xml ids of views
    protected void setTags(View view) {
        favCuisine = (Button) view.findViewById(R.id.your_home_cuisine);
        favCuisine.setTypeface(Util.opensanslight);
        pickThree = (TextView) view.findViewById(R.id.pick_three);
        pickThree.setTypeface(Util.opensanslight);
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
                        Log.d("Name of selected item", model.getFavCuisName());
                        model.setFavCuisCurrentSelect(true);
                        Util.favCuisineCount++;
                        Util.favCuisineSelects.add(new FavCuisineSelect(model.getFavCuisClassName(), model.getFavCuisEntityId()));
                        checkcount++;
                        if (checkcount == 3) {
                            Util.setFavCuisinetotal(checkcount);
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    showNext();
                                }
                            }, 1000);
                        }
                    } else {
                        model.setFavCuisCurrentSelect(false);
                    }

                }
            });
            if (checkcount == 3) {
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

    void showNext() {
        Log.d("showNext is selected", "count of fav dishes:" + Util.favCuisineCount);
        Log.d("Next page", "next page is shown");
        if (checkcount == 3) {
            Fragment fragment = new TastePrefFragment4();
            FragmentManager fm = getActivity().getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            //ft.hide(getActivity().getSupportFragmentManager().findFragmentById(R.id.onboarding_screen3));
            ft.setCustomAnimations(R.anim.enter_from_bottom, R.anim.exit_from_top, R.anim.enter_from_top, R.anim.exit_from_bottom);
            ft.replace(R.id.onboarding_screen4, fragment);
            ft.addToBackStack(null);
            ft.commit();
        }

    }
}
