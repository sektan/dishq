package version1.dishq.dishq.fragments.onboardingFragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.TextView;

import com.wefika.flowlayout.FlowLayout;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import version1.dishq.dishq.R;
import version1.dishq.dishq.modals.AllergyModal;
import version1.dishq.dishq.modals.lists.DontEatSelect;
import version1.dishq.dishq.server.Config;
import version1.dishq.dishq.server.Request.UserPrefRequest;
import version1.dishq.dishq.server.RestApi;
import version1.dishq.dishq.ui.HomeActivity;
import version1.dishq.dishq.util.DishqApplication;
import version1.dishq.dishq.util.Util;

/**
 * Created by dishq on 16-12-2016.
 * Package name version1.dishq.dishq.
 */

public class TastePrefFragment4 extends Fragment {

    private final static String TAG = "TastePrefFragment4";
    CheckedTextView child;
    Button allergyCuisine, doneButton;
    TextView optional;
    private FlowLayout allergyContainer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_taste_pref_fourth, container, false);
        setTags(v);

        return v;
    }

    //For linking to xml ids of views
    protected void setTags(View view) {
        doneButton = (Button) view.findViewById(R.id.done);
        allergyCuisine = (Button) view.findViewById(R.id.allergy_cuisine);
        optional = (TextView) view.findViewById(R.id.optional);
        setTypeFace();
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendUserPrefData();
            }
        });
        allergyContainer = (FlowLayout) view.findViewById(R.id.allergy_container);
        allergyContainer.removeAllViews();
        for (AllergyModal model : Util.allergyModals) {
            child = (CheckedTextView) LayoutInflater.from(getContext()).inflate(R.layout.simple_selectable_list_item, allergyContainer, false);
            child.setText(model.getAllergyName());
            child.setTypeface(Util.opensansregular);
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

    //For setting the font of the text visible to the user
    protected void setTypeFace() {
        if (allergyCuisine != null) {
            allergyCuisine.setTypeface(Util.opensanslight);
        }
        if (optional != null) {
            optional.setTypeface(Util.opensanslight);
        }
        doneButton.setTypeface(Util.opensanssemibold);
    }

    public void sendUserPrefData() {
        RestApi restApi = Config.createService(RestApi.class);
        String authorization = DishqApplication.getAccessToken();
        final UserPrefRequest userPrefRequest = new UserPrefRequest(Util.getFoodChoiceSelected(), Util.homeCuisineSelects,
                Util.favCuisineSelects, Util.dontEatSelects);
        Call<ResponseBody> request = restApi.sendUserPref(authorization, userPrefRequest);
        request.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d(TAG, "Success");
                Intent startHomeActivity = new Intent(getActivity(), HomeActivity.class);
                startHomeActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                getActivity().finish();
                startActivity(startHomeActivity);

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d(TAG, "Failure");
            }
        });
    }

}
