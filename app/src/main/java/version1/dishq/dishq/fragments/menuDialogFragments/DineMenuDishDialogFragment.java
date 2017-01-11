package version1.dishq.dishq.fragments.menuDialogFragments;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;

import version1.dishq.dishq.R;
import version1.dishq.dishq.util.Util;

/**
 * Created by dishq on 09-01-2017.
 * Package name version1.dishq.dishq.
 */

public class DineMenuDishDialogFragment extends DialogFragment implements View.OnClickListener{

    TextView dineMenuDishName, dineMenuDishType;
    private ImageView vegTag, eggTag, nonVegTag, isSpicyTag, hasAlcoholTag;
    private Button dineMenuFoodTags;
    private RelativeLayout rlDineMenuView;
    private FrameLayout dineMenuFrame;

    public DineMenuDishDialogFragment() {

    }

    @Override
    public void onClick(View view) {

    }

    public static DineMenuDishDialogFragment getInstance() {
        return new DineMenuDishDialogFragment();
    }

    @Override
    public void onResume() {
        getDialog().getWindow().setLayout(dpToPx(380), dpToPx(480));
        super.onResume();
    }

    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.dialog_fragment_dish, container, false);

        rlDineMenuView = (RelativeLayout) rootView.findViewById(R.id.rl_fav_dish);
        dineMenuFrame = (FrameLayout) rootView.findViewById(R.id.fav_frame);
        dineMenuDishName = (TextView) rootView.findViewById(R.id.fav_dish_name);
        dineMenuDishName.setTypeface(Util.opensanslight);
        dineMenuDishType = (TextView) rootView.findViewById(R.id.fav_dish_type);
        vegTag = (ImageView) rootView.findViewById(R.id.veg_tag);
        eggTag = (ImageView) rootView.findViewById(R.id.egg_tag);
        nonVegTag = (ImageView) rootView.findViewById(R.id.non_veg_tag);
        isSpicyTag = (ImageView) rootView.findViewById(R.id.spicy_tag);
        hasAlcoholTag = (ImageView) rootView.findViewById(R.id.alcohol_tag);
        dineMenuFoodTags = (Button) rootView.findViewById(R.id.fav_food_tags);

        dineMenuFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
            }
        });

        ArrayList<String> imageUrls = Util.dineoutMenuInfos.get(Util.getFavPosition()).getDineMenuPhotoPopup();
        String imageUrl = imageUrls.get(0);
        Picasso.with(getContext())
                .load(imageUrl)
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        rlDineMenuView.setBackground(new BitmapDrawable(bitmap));
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                });

        dineMenuDishName.setText(Util.dineoutMenuInfos.get(Util.getFavPosition()).getDineMenuDishName());

        int favDishNature = Util.dineoutMenuInfos.get(Util.getFavPosition()).getDineMenuDishNature();
        if (favDishNature == 1) {
            eggTag.setVisibility(View.GONE);
            vegTag.setVisibility(View.VISIBLE);
            nonVegTag.setVisibility(View.GONE);

        } else if (favDishNature == 2) {
            eggTag.setVisibility(View.VISIBLE);
            vegTag.setVisibility(View.GONE);
            nonVegTag.setVisibility(View.GONE);
        } else {
            eggTag.setVisibility(View.GONE);
            vegTag.setVisibility(View.GONE);
            nonVegTag.setVisibility(View.VISIBLE);
        }

        String dishTags = "";
        if (Util.dineoutMenuInfos != null) {
            for (String s : Util.dineoutMenuInfos.get(Util.getFavPosition()).getDineMenuTags()) {
                dishTags += s + " ";
            }
        }
        dineMenuFoodTags.setText(String.valueOf(dishTags));

        return rootView;
    }

}