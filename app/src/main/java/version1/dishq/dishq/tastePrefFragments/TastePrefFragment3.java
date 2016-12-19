package version1.dishq.dishq.tastePrefFragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;

import com.wefika.flowlayout.FlowLayout;

import version1.dishq.dishq.R;
import version1.dishq.dishq.modals.FavCuisinesModal;
import version1.dishq.dishq.ui.OnBoardingActivity;
import version1.dishq.dishq.util.DishqApplication;
import version1.dishq.dishq.util.Util;

/**
 * Created by dishq on 16-12-2016.
 * Package name version1.dishq.dishq.
 */

public class TastePrefFragment3 extends Fragment {

    private FavCuisinesModal favCuisinesModal;
    private FlowLayout favCuisineContainer;
    CheckedTextView child;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_taste_pref_third, container, false);
        setTags(v);

        return v;
    }

    //For linking to xml ids of views
    protected void setTags(View view) {
        favCuisineContainer = (FlowLayout) view.findViewById(R.id.fav_cuisine_container);
        favCuisineContainer.removeAllViews();
        for (FavCuisinesModal model : Util.favCuisinesModals) {
            child = (CheckedTextView) LayoutInflater.from(getContext()).inflate(R.layout.simple_selectable_list_item, favCuisineContainer, false);
            child.setText(model.getFavCuisName());
            child.setTag(model);
            if (model.getFavCuisCurrentSelect()) {
                child.setChecked(true);
            }
            if (child.isChecked()) {
                if (Util.favCuisineCount == 3) {
                    showNext();
                } else {
                    child.setBackgroundColor(DishqApplication.getContext().getResources().getColor(R.color.white));
                    child.setTextColor(DishqApplication.getContext().getResources().getColor(R.color.black));
                }
            }
            favCuisineContainer.addView(child);
            child.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    child.setBackgroundColor(DishqApplication.getContext().getResources().getColor(R.color.white));
                    child.setTextColor(DishqApplication.getContext().getResources().getColor(R.color.black));
                    Util.favCuisineCount++;
                    if (Util.favCuisineCount == 3) {
                        showNext();
                    }

                }
            });

        }

    }

    public static TastePrefFragment3 newInstance(String text) {
        TastePrefFragment3 f = new TastePrefFragment3();
        Bundle b = new Bundle();
        b.putString("msg", text);
        f.setArguments(b);
        return f;
    }

    void showNext() {
        if (Util.favCuisineCount == 3) {
            if (OnBoardingActivity.pager.getCurrentItem() == 1) {
                //OnBoardingActivity.pager.setPagingEnabled(CustomViewPager.SwipeDirection.DOWN);
                OnBoardingActivity.pager.setCurrentItem(2);
            }
        }

    }
}
