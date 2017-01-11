package version1.dishq.dishq.adapters.menuAdapters;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import version1.dishq.dishq.R;
import version1.dishq.dishq.fragments.menuDialogFragments.DelMenuDishDialogFragment;
import version1.dishq.dishq.server.Config;
import version1.dishq.dishq.server.Request.FavDishAddRemHelper;
import version1.dishq.dishq.server.RestApi;
import version1.dishq.dishq.util.DishqApplication;
import version1.dishq.dishq.util.Util;

import static version1.dishq.dishq.util.DishqApplication.getContext;

/**
 * Created by dishq on 09-01-2017.
 * Package name version1.dishq.dishq.
 */

public class DeliveryMenuMasonryAdapter extends RecyclerView.Adapter<DeliveryMenuMasonryAdapter.DelMenuInfoAdapter>{

    private static final String TAG = "DelMenuMasonryAdapter";
    private Context context;

    public DeliveryMenuMasonryAdapter(Context context) {
        this.context = context;
    }

    @Override
    public DelMenuInfoAdapter onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.cardview_menu, viewGroup, false);
        return new DelMenuInfoAdapter(view);
    }

    @Override
    public void onBindViewHolder(final DelMenuInfoAdapter holder, int position) {
        ArrayList<String> imageUrls = Util.deliveryMenuInfos.get(position).delMenuPhoto;
        String imageUrl = imageUrls.get(0);
        Picasso.with(getContext())
                .load(imageUrl)
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        holder.rlCard.setBackground(new BitmapDrawable(bitmap));
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                });

        final int delMenuPos = position;
        holder.delMenuFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Util.setFavPosition(delMenuPos);
                FragmentActivity activity = (FragmentActivity) (context);
                FragmentManager fm = activity.getSupportFragmentManager();
                DelMenuDishDialogFragment dialogFragment = DelMenuDishDialogFragment.getInstance();
                dialogFragment.show(fm, "fav_dialog_fragment");
            }
        });

        holder.delMenuDishName.setText(Util.deliveryMenuInfos.get(position).getDelMenuDishName());
        int delMenuDishNature = Util.deliveryMenuInfos.get(position).getDelMenuDishNature();
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

        final int delMenuGenericDishId = Util.deliveryMenuInfos.get(position).getDelMenuGenericDishId();
        holder.delMenuFav.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                String source = "deliverymenu";
                if(isChecked) {
                    addRemoveDishFromFav(source, delMenuGenericDishId, 1);
                }else {
                    addRemoveDishFromFav(source, delMenuGenericDishId, 0);
                }
            }
        });

        Resources res = context.getResources();
        String text = String.format(res.getString(R.string.cost_of_dish), Util.deliveryMenuInfos.get(position).getDelMenuDishPrice());
        holder.delMenuDishCost.setText(text);
    }

    @Override
    public int getItemCount() {
        return Util.deliveryMenuInfos.size();
    }

    static class DelMenuInfoAdapter extends RecyclerView.ViewHolder {

        private RelativeLayout rlCard;
        private ImageView vegTag, eggTag, nonVegTag, isSpicyTag, hasAlcoholTag;
        private TextView delMenuDishName, delMenuDishCost;
        private ToggleButton delMenuFav;
        private FrameLayout delMenuFrame;

        public DelMenuInfoAdapter(View view) {
            super(view);

            rlCard = (RelativeLayout) view.findViewById(R.id.cv_rl_menu);
            vegTag = (ImageView) view.findViewById(R.id.veg_tag);
            eggTag = (ImageView) view.findViewById(R.id.egg_tag);
            nonVegTag = (ImageView) view.findViewById(R.id.non_veg_tag);
            isSpicyTag = (ImageView) view.findViewById(R.id.spicy_tag);
            hasAlcoholTag = (ImageView) view.findViewById(R.id.alcohol_tag);
            delMenuDishName = (TextView) view.findViewById(R.id.menu_dish_name);
            delMenuDishCost = (TextView) view.findViewById(R.id.menu_cost);
            delMenuFav = (ToggleButton) view.findViewById(R.id.menu_favourites);
            delMenuFrame = (FrameLayout) view.findViewById(R.id.menu_frame);
        }
    }

    private void addRemoveDishFromFav(String source, int delGenericDishId, int checked) {
        final FavDishAddRemHelper favDishAddRemHelper = new FavDishAddRemHelper(DishqApplication.getUniqueID(),
                source, delGenericDishId, checked);
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
