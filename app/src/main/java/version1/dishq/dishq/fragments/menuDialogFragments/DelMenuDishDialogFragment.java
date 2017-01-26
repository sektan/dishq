package version1.dishq.dishq.fragments.menuDialogFragments;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
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
    protected ImageView photoPopUp;

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
        int width = getResources().getDisplayMetrics().widthPixels;
        getDialog().getWindow().setLayout(width, dpToPx(480));
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

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
        delMenuDishName.setTypeface(Util.opensansregular);
        delMenuDishType = (TextView) rootView.findViewById(R.id.fav_dish_type);
        delMenuDishType.setTypeface(Util.opensansregular);
        vegTag = (ImageView) rootView.findViewById(R.id.veg_tag);
        eggTag = (ImageView) rootView.findViewById(R.id.egg_tag);
        nonVegTag = (ImageView) rootView.findViewById(R.id.non_veg_tag);
        isSpicyTag = (ImageView) rootView.findViewById(R.id.spicy_tag);
        hasAlcoholTag = (ImageView) rootView.findViewById(R.id.alcohol_tag);
        delMenuFoodTags = (Button) rootView.findViewById(R.id.fav_food_tags);
        photoPopUp = (ImageView) rootView.findViewById(R.id.popup_photo);
        photoPopUp.setScaleType(ImageView.ScaleType.FIT_CENTER);

        delMenuFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
            }
        });

        ArrayList<String> imageUrls = Util.deliveryMenuInfos.get(Util.getFavPosition()).getDelMenuPhotoPopup();
        Picasso.with(getContext())
                .load(imageUrls.get(0))
                .fit()
                .centerCrop()
                .into(photoPopUp);

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

        if (Util.deliveryMenuInfos.get(Util.getFavPosition()).getDelMenuIsSpicy()) {
            isSpicyTag.setVisibility(View.VISIBLE);
        } else {
            isSpicyTag.setVisibility(View.GONE);
        }

        if (Util.deliveryMenuInfos.get(Util.getFavPosition()).getDelMenuHasAlcohol()) {
            hasAlcoholTag.setVisibility(View.VISIBLE);
        } else {
            hasAlcoholTag.setVisibility(View.GONE);
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
