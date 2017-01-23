package version1.dishq.dishq.adapters.menuAdapters;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.mixpanel.android.mpmetrics.MixpanelAPI;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import version1.dishq.dishq.R;
import version1.dishq.dishq.fragments.menuDialogFragments.DineMenuDishDialogFragment;
import version1.dishq.dishq.util.Util;

import static version1.dishq.dishq.util.DishqApplication.getContext;

/**
 * Created by dishq on 11-01-2017.
 * Package name version1.dishq.dishq.
 */

public class DineoutMenuMasonryAdapter extends RecyclerView.Adapter<DineoutMenuMasonryAdapter.DineMenuInfoAdapter>{

    private static final String TAG = "DineMenuMasonryAdapter";
    private Context context;
    private MixpanelAPI mixpanel = null;

    public DineoutMenuMasonryAdapter(Context context) {
        this.context = context;
    }

    @Override
    public DineMenuInfoAdapter onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.cardview_menu, viewGroup, false);
        //MixPanel Instantiation
        mixpanel = MixpanelAPI.getInstance(context, context.getResources().getString(R.string.mixpanel_token));

        return new DineMenuInfoAdapter(view);
    }

    @Override
    public void onBindViewHolder(final DineMenuInfoAdapter holder, int position) {
        ArrayList<String> imageUrls = Util.dineoutMenuInfos.get(position).dineMenuPhoto;
        Picasso.with(getContext())
                .load(imageUrls.get(0))
                .into(holder.cardBgImage);

        final int dineMenuPos = position;
        holder.dineMenuFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    final JSONObject properties = new JSONObject();
                    properties.put("Dish popout in dineout menu", "dineoutmenu");
                    mixpanel.track("Dish popout in dineout menu", properties);
                } catch (final JSONException e) {
                    throw new RuntimeException("Could not encode hour of the day in JSON");
                }
                Util.setFavPosition(dineMenuPos);
                FragmentActivity activity = (FragmentActivity) (context);
                FragmentManager fm = activity.getSupportFragmentManager();
                DineMenuDishDialogFragment dialogFragment = DineMenuDishDialogFragment.getInstance();
                dialogFragment.show(fm, "fav_dialog_fragment");
            }
        });

        holder.dineMenuDishName.setText(Util.dineoutMenuInfos.get(position).getDineMenuDishName());
        int delMenuDishNature = Util.dineoutMenuInfos.get(position).getDineMenuDishNature();
        if (delMenuDishNature == 1) {
            holder.eggTag.setVisibility(View.GONE);
            holder.vegTag.setVisibility(View.VISIBLE);
            holder.nonVegTag.setVisibility(View.GONE);

        } else if (delMenuDishNature == 2) {
            holder.eggTag.setVisibility(View.VISIBLE);
            holder.vegTag.setVisibility(View.GONE);
            holder.nonVegTag.setVisibility(View.GONE);
        } else {
            holder.eggTag.setVisibility(View.GONE);
            holder.vegTag.setVisibility(View.GONE);
            holder.nonVegTag.setVisibility(View.VISIBLE);
        }

        Boolean isFavourited = Util.dineoutMenuInfos.get(position).getAddedToFavdine();
        if(isFavourited) {
            holder.dineMenuFav.setChecked(true);
        }else {
            holder.dineMenuFav.setChecked(false);
        }

        if (Util.dineoutMenuInfos.get(position).getDineMenuIsSpicy()) {
            holder.isSpicyTag.setVisibility(View.VISIBLE);
        } else {
            holder.isSpicyTag.setVisibility(View.GONE);
        }

        if (Util.dineoutMenuInfos.get(position).getDineMenuHasAlcohol()) {
            holder.hasAlcoholTag.setVisibility(View.VISIBLE);
        } else {
            holder.hasAlcoholTag.setVisibility(View.GONE);
        }

        final int dineMenuGenericDishId = Util.dineoutMenuInfos.get(position).getDineMenuGenericDishId();
        holder.dineMenuFav.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                try {
                    final JSONObject properties = new JSONObject();
                    properties.put("Dish fav in dineout menu", "dineoutmenu");
                    mixpanel.track("Dish fav in dineout menu", properties);
                } catch (final JSONException e) {
                    throw new RuntimeException("Could not encode hour of the day in JSON");
                }
                String source = "deliverymenu";
                if(isChecked) {
                    Util.addRemoveDishFromFav(source, dineMenuGenericDishId, 1, TAG);
                }else {
                    Util.addRemoveDishFromFav(source, dineMenuGenericDishId, 0, TAG);
                }
            }
        });

        Resources res = context.getResources();
        String text = String.format(res.getString(R.string.cost_of_dish), Util.dineoutMenuInfos.get(position).getDineMenuDishPrice());
        holder.dineMenuDishCost.setText(text);
    }

    @Override
    public int getItemCount() {
        return Util.dineoutMenuInfos.size();
    }

    static class DineMenuInfoAdapter extends RecyclerView.ViewHolder {

        private RelativeLayout rlCard;
        private ImageView vegTag, eggTag, nonVegTag, isSpicyTag, hasAlcoholTag;
        private TextView dineMenuDishName, dineMenuDishCost;
        private ToggleButton dineMenuFav;
        private FrameLayout dineMenuFrame;
        private ImageView cardBgImage;

        DineMenuInfoAdapter(View view) {
            super(view);

            rlCard = (RelativeLayout) view.findViewById(R.id.cv_rl_menu);

            vegTag = (ImageView) view.findViewById(R.id.veg_tag);
            eggTag = (ImageView) view.findViewById(R.id.egg_tag);
            nonVegTag = (ImageView) view.findViewById(R.id.non_veg_tag);
            isSpicyTag = (ImageView) view.findViewById(R.id.spicy_tag);
            hasAlcoholTag = (ImageView) view.findViewById(R.id.alcohol_tag);
            dineMenuDishName = (TextView) view.findViewById(R.id.menu_dish_name);
            dineMenuDishName.setTypeface(Util.opensansregular);
            dineMenuDishCost = (TextView) view.findViewById(R.id.menu_cost);
            dineMenuDishCost.setTypeface(Util.opensansregular);
            dineMenuFav = (ToggleButton) view.findViewById(R.id.menu_favourites);
            dineMenuFrame = (FrameLayout) view.findViewById(R.id.menu_frame);
            cardBgImage = (ImageView) view.findViewById(R.id.cardmenu_bg_image);
            cardBgImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
    }


}
