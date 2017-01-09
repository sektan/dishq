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

public class DelMenuDishDialogFragment extends DialogFragment implements View.OnClickListener{

    TextView delMenuDishName, delMenuDishType;
    private ImageView vegTag, eggTag, nonVegTag, isSpicyTag, hasAlcoholTag;
    private Button delMenuFoodTags;
    private RelativeLayout rlDelMenuView;
    private FrameLayout delMenuFrame;

    public DelMenuDishDialogFragment() {

    }

    @Override
    public void onClick(View view) {

    }

    public static DelMenuDishDialogFragment getInstance() {
        return new DelMenuDishDialogFragment();
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

        rlDelMenuView = (RelativeLayout) rootView.findViewById(R.id.rl_fav_dish);
        delMenuFrame = (FrameLayout) rootView.findViewById(R.id.fav_frame);
        delMenuDishName = (TextView) rootView.findViewById(R.id.fav_dish_name);
        delMenuDishName.setTypeface(Util.opensanslight);
        delMenuDishType = (TextView) rootView.findViewById(R.id.fav_dish_type);
        vegTag = (ImageView) rootView.findViewById(R.id.veg_tag);
        eggTag = (ImageView) rootView.findViewById(R.id.egg_tag);
        nonVegTag = (ImageView) rootView.findViewById(R.id.non_veg_tag);
        isSpicyTag = (ImageView) rootView.findViewById(R.id.spicy_tag);
        hasAlcoholTag = (ImageView) rootView.findViewById(R.id.alcohol_tag);
        delMenuFoodTags = (Button) rootView.findViewById(R.id.fav_food_tags);

        delMenuFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
            }
        });

        ArrayList<String> imageUrls = Util.deliveryMenuInfos.get(Util.getFavPosition()).getDelMenuPhotoPopup();
        String imageUrl = imageUrls.get(0);
        Picasso.with(getContext())
                .load(imageUrl)
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        rlDelMenuView.setBackground(new BitmapDrawable(bitmap));
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                });

        delMenuDishName.setText(Util.deliveryMenuInfos.get(Util.getFavPosition()).getDelMenuDishName());

        int favDishNature = Util.deliveryMenuInfos.get(Util.getFavPosition()).getDelMenuDishNature();
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
        if (Util.deliveryMenuInfos != null) {
            for (String s : Util.deliveryMenuInfos.get(Util.getFavPosition()).getDelMenuTags()) {
                dishTags += s + " ";
            }
        }
        delMenuFoodTags.setText(String.valueOf(dishTags));

        return rootView;
    }

}
