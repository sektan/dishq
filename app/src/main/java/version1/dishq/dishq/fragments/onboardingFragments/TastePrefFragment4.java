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

import com.google.gson.Gson;
import com.mixpanel.android.mpmetrics.MixpanelAPI;
import com.wefika.flowlayout.FlowLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import version1.dishq.dishq.R;
import version1.dishq.dishq.custom.CustomViewPager;
import version1.dishq.dishq.custom.OnSwipeTouchListener;
import version1.dishq.dishq.modals.AllergyModal;
import version1.dishq.dishq.modals.lists.DontEatSelect;
import version1.dishq.dishq.server.Config;
import version1.dishq.dishq.server.Request.UserPrefRequest;
import version1.dishq.dishq.server.RestApi;
import version1.dishq.dishq.ui.HomeActivity;
import version1.dishq.dishq.ui.OnBoardingActivity;
import version1.dishq.dishq.util.Constants;
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
    private MixpanelAPI mixpanel = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //MixPanel Instantiation
        mixpanel = MixpanelAPI.getInstance(getActivity(), getResources().getString(R.string.mixpanel_token));

        View v = inflater.inflate(R.layout.fragment_taste_pref_fourth, container, false);
        setTags(v);
        v.setOnTouchListener(new OnSwipeTouchListener(getActivity()) {
            public void onSwipeRight() {
                OnBoardingActivity.pager.setCurrentItem(2);
            }

        });

        try {
            final JSONObject properties = new JSONObject();
            properties.put("Onboarding Screen 4", "onboarding");
            mixpanel.track("Onboarding Screen 4", properties);
        } catch (final JSONException e) {
            throw new RuntimeException("Could not encode hour of the day in JSON");
        }

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
                try {
                    final JSONObject properties = new JSONObject();
                    properties.put("Done button", "onboarding");
                    mixpanel.track("Done button", properties);
                } catch (final JSONException e) {
                    throw new RuntimeException("Could not encode hour of the day in JSON");
                }
                OnBoardingActivity.pager.setPagingEnabled(CustomViewPager.SwipeDirection.NONE);
                sendUserPrefData();
            }
        });
        allergyContainer = (FlowLayout) view.findViewById(R.id.allergy_container);
        allergyContainer.removeAllViews();
        for (AllergyModal model : Util.allergyModals) {
            if (DishqApplication.getFoodChoiceSelected() < model.getAllergyFoodChoice()) {
                continue;
            }
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
                        DontEatSelect dontEatSelect = new DontEatSelect(model.getAllergyClassName(), model.getAllergyEntityId());
                        Util.dontEatSelects.add(dontEatSelect);
                        Util.getDontEatMaps().put(model.getAllergyName(), dontEatSelect);

                    } else if (!view.isChecked()) {
                        model.setAllergyCurrentlySelect(false);
                        Util.dontEatSelects.remove(Util.getDontEatMaps().get(model.getAllergyName()));
                        Util.getDontEatMaps().remove(model.getAllergyName());
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
        final UserPrefRequest userPrefRequest = new UserPrefRequest(DishqApplication.getFoodChoiceSelected(), Util.homeCuisineSelects,
                Util.favCuisineSelects, Util.dontEatSelects);
        Call<ResponseBody> request = restApi.sendUserPref(authorization, userPrefRequest);
        request.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d(TAG, "Success");
                DishqApplication.getPrefs().edit().putBoolean(Constants.ON_BOARDING_DONE, true).apply();
                DishqApplication.setOnBoardingDone(true);
                Intent startHomeActivity = new Intent(DishqApplication.getContext(), HomeActivity.class);
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

    @Override
    public void onDestroy() {
        mixpanel.flush();
        super.onDestroy();
    }
}
