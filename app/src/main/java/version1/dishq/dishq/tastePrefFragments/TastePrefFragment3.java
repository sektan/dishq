package version1.dishq.dishq.tastePrefFragments;

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
import version1.dishq.dishq.modals.FavCuisinesModal;
import version1.dishq.dishq.ui.OnBoardingActivity;
import version1.dishq.dishq.util.DishqApplication;
import version1.dishq.dishq.util.Util;

/**
 * Created by dishq on 16-12-2016.
 * Package name version1.dishq.dishq.
 */

public class TastePrefFragment3 extends Fragment {

    private FlowLayout favCuisineContainer;
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
        favCuisineContainer = (FlowLayout) view.findViewById(R.id.fav_cuisine_container);
        favCuisineContainer.removeAllViews();
        for (FavCuisinesModal model : Util.favCuisinesModals) {
            child = (CheckedTextView) LayoutInflater.from(getContext()).inflate(R.layout.simple_selectable_list_item, favCuisineContainer, false);
            child.setText(model.getFavCuisName());
            child.setTag(model);
            if (model.getFavCuisCurrentSelect()) {
                child.setChecked(true);
            }
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

            favCuisineContainer.addView(child);
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

    public static TastePrefFragment3 newInstance(String text) {
        TastePrefFragment3 f = new TastePrefFragment3();
        Bundle b = new Bundle();
        b.putString("msg", text);
        f.setArguments(b);
        return f;
    }

    void showNext() {
        Log.d("showNext is selected", "count of fav dishes:" + Util.favCuisineCount);
        Log.d("Next page", "next page is shown");
        if (OnBoardingActivity.pager.getCurrentItem() == 2) {
            //OnBoardingActivity.pager.setPagingEnabled(CustomViewPager.SwipeDirection.DOWN);
            OnBoardingActivity.pager.setCurrentItem(3);

        }

    }
}
