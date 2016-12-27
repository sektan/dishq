package version1.dishq.dishq.tastePrefFragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckedTextView;

import com.wefika.flowlayout.FlowLayout;

import version1.dishq.dishq.R;
import version1.dishq.dishq.modals.AllergyModal;
import version1.dishq.dishq.modals.lists.DontEatSelect;
import version1.dishq.dishq.ui.HomeActivity;
import version1.dishq.dishq.ui.OnBoardingActivity;
import version1.dishq.dishq.util.DishqApplication;
import version1.dishq.dishq.util.Util;

/**
 * Created by dishq on 16-12-2016.
 * Package name version1.dishq.dishq.
 */

public class TastePrefFragment4 extends Fragment {

    private FlowLayout allergyContainer;
    CheckedTextView child;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_taste_pref_fourth, container, false);
        setTags(v);

        return v;
    }

    //For linking to xml ids of views
    protected void setTags(View view) {
        allergyContainer = (FlowLayout) view.findViewById(R.id.allergy_container);
        allergyContainer.removeAllViews();
        for (AllergyModal model : Util.allergyModals) {
            child = (CheckedTextView) LayoutInflater.from(getContext()).inflate(R.layout.simple_selectable_list_item, allergyContainer, false);
            child.setText(model.getAllergyName());
            child.setTag(model);

            allergyContainer.addView(child);
            child.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    CheckedTextView view = (CheckedTextView) v;
                    view.setChecked(!view.isChecked());
                    AllergyModal model = (AllergyModal) view.getTag();
                    if (view.isChecked()) {
                        Log.d("Name of selected item", model.getAllergyName());
                        model.setAllergyCurrentlySelect(true);
                        Util.favCuisineCount++;
                        Util.dontEatSelects.add(new DontEatSelect(model.getAllergyClassName(), model.getAllergyEntityId()));
                    }else {
                        model.setAllergyCurrentlySelect(false);
                    }
                }
            });
            if (model.getAllergyCurrentlySelect()) {
                child.setChecked(true);
            }

        }
    }

    public static TastePrefFragment4 newInstance(String text) {
        TastePrefFragment4 f = new TastePrefFragment4();
        Bundle b = new Bundle();
        b.putString("msg", text);
        f.setArguments(b);
        return f;
    }

}
