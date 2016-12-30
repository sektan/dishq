package version1.dishq.dishq.fragments.homeScreenFragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import version1.dishq.dishq.R;
import version1.dishq.dishq.fragments.bottomSheetFragment.StatisticFragment;
import version1.dishq.dishq.server.Config;
import version1.dishq.dishq.server.Request.FavDishAddRemHelper;
import version1.dishq.dishq.server.Response.DishDataInfo;
import version1.dishq.dishq.server.RestApi;
import version1.dishq.dishq.util.DishqApplication;
import version1.dishq.dishq.util.Util;

/**
 * Created by dishq on 27-12-2016.
 * Package name version1.dishq.dishq.
 */

public class HomeScreenFragment extends Fragment {

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String TAG = "HomeScreenFragment";
    DishDataInfo dishDataInfo;
    private RelativeLayout rlHomeScreen;
    private ImageView vegTag, eggTag, nonVegTag, isSpicyTag, hasAlcoholTag;
    private TextView dishName, dishType;
    private Button foodTags, dineButton;
    private ToggleButton favButton;

    public HomeScreenFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            dishDataInfo = new DishDataInfo();
            this.dishDataInfo = dishDataInfo.toModel(bundle);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_home_screen, container, false);
        rlHomeScreen = (RelativeLayout) rootView.findViewById(R.id.rl_home_screen);
        vegTag = (ImageView) rootView.findViewById(R.id.veg_tag);
        eggTag = (ImageView) rootView.findViewById(R.id.egg_tag);
        nonVegTag = (ImageView) rootView.findViewById(R.id.non_veg_tag);
        isSpicyTag = (ImageView) rootView.findViewById(R.id.spicy_tag);
        hasAlcoholTag = (ImageView) rootView.findViewById(R.id.alcohol_tag);
        dishName = (TextView) rootView.findViewById(R.id.dish_name);
        dishType = (TextView) rootView.findViewById(R.id.dish_type);
        foodTags = (Button) rootView.findViewById(R.id.food_tags);
        favButton = (ToggleButton) rootView.findViewById(R.id.favourites);
        dineButton = (Button) rootView.findViewById(R.id.dining);
        setFont();

        if (dishDataInfo != null) {
            String imageUrl = dishDataInfo.getDishPhoto().get(0);
            Picasso.with(DishqApplication.getContext())
                    .load(imageUrl)
                    .into(new Target() {
                        @Override
                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                            rlHomeScreen.setBackground(new BitmapDrawable(bitmap));
                        }

                        @Override
                        public void onBitmapFailed(Drawable errorDrawable) {

                        }

                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) {

                        }
                    });
        }

        dineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Util.setGenericDishIdTab(dishDataInfo.getGenericDishId());
                new StatisticFragment().show(getActivity().getSupportFragmentManager(), "dialog");

            }
        });

        favButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                String source = "homescreen";
                if(isChecked) {
                    //The toggle is enabled
                    final FavDishAddRemHelper favDishAddRemHelper = new FavDishAddRemHelper(DishqApplication.getUniqueID(),
                            source, dishDataInfo.getGenericDishId(), 1);
                    RestApi restApi = Config.createService(RestApi.class);
                    Call<ResponseBody> call = restApi.addRemoveFavDish(DishqApplication.getAccessToken(), favDishAddRemHelper);
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            Log.d(TAG, "Success");
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Log.d(TAG, "Failure");
                        }
                    });

                }else {
                    //The toggle is disabled
                    final FavDishAddRemHelper favDishAddRemHelper = new FavDishAddRemHelper(DishqApplication.getUniqueID(),
                            source, dishDataInfo.getGenericDishId(), 0);
                    RestApi restApi = Config.createService(RestApi.class);
                    Call<ResponseBody> call = restApi.addRemoveFavDish(DishqApplication.getAccessToken(), favDishAddRemHelper);
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            Log.d(TAG, "Success");
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Log.d(TAG, "Failure");
                        }
                    });
                }
            }
        });

        if (this.dishDataInfo != null) {
            dishName.setText(String.valueOf(dishDataInfo.getDishName()));
            Boolean isVeg = dishDataInfo.getVeg();
            Boolean hasEgg = dishDataInfo.getHasEgg();
            Boolean isSpicy = dishDataInfo.getSpicy();
            Boolean hasAlcohol = dishDataInfo.getHasAlcohol();
            if (isVeg) {
                if (hasEgg) {
                    eggTag.setVisibility(View.VISIBLE);
                    vegTag.setVisibility(View.GONE);
                    nonVegTag.setVisibility(View.GONE);
                } else {

                    eggTag.setVisibility(View.GONE);
                    vegTag.setVisibility(View.VISIBLE);
                    nonVegTag.setVisibility(View.GONE);
                }
            } else {
                eggTag.setVisibility(View.GONE);
                vegTag.setVisibility(View.GONE);
                nonVegTag.setVisibility(View.VISIBLE);
            }

            if(isSpicy) {
                isSpicyTag.setVisibility(View.VISIBLE);
            }else {
                isSpicyTag.setVisibility(View.GONE);
            }

            if(hasAlcohol) {
                hasAlcoholTag.setVisibility(View.VISIBLE);
            }else {
                hasAlcoholTag.setVisibility(View.GONE);
            }
        }


        StringBuilder sb = new StringBuilder();
        if (dishDataInfo != null) {
            for (String s : dishDataInfo.getCuisineText()) {
                if (sb.length() > 0) {
                    sb.append(',' + " ");
                }
                sb.append(s);
            }
        }
        String dishTypeText = sb.toString();
        dishType.setText(String.valueOf(dishTypeText));
        String dishTags = "";
        if (dishDataInfo != null) {
            for (String s : dishDataInfo.getTags()) {
                dishTags += s + " ";
            }
        }
        foodTags.setText(String.valueOf(dishTags));

        return rootView;
    }

    protected void setFont() {
        dishName.setTypeface(Util.opensanslight);

        foodTags.setTypeface(Util.opensanslight);
    }

}
