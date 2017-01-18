package version1.dishq.dishq.adapters.favGridViewAdapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mixpanel.android.mpmetrics.MixpanelAPI;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import version1.dishq.dishq.R;
import version1.dishq.dishq.fragments.bottomSheetFragment.BottomSheetFragment;
import version1.dishq.dishq.fragments.favPageFragment.FavDishDialogFragment;
import version1.dishq.dishq.server.Config;
import version1.dishq.dishq.server.Request.FavDishAddRemHelper;
import version1.dishq.dishq.server.RestApi;
import version1.dishq.dishq.util.DishqApplication;
import version1.dishq.dishq.util.Util;

import static version1.dishq.dishq.util.DishqApplication.getContext;

/**
 * Created by dishq on 08-01-2017.
 * Package name version1.dishq.dishq.
 */

public class FavGridViewAdapter extends RecyclerView.Adapter<FavGridViewAdapter.FavGridViewInfoAdapter> {

    private static final String TAG = "FavGridViewAdapter";
    private Context context;
    private MixpanelAPI mixpanel = null;

    public FavGridViewAdapter(Context context) {
        this.context = context;
    }

    @Override
    public FavGridViewInfoAdapter onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.cardview_favourites, viewGroup, false);
        //MixPanel Instantiation
        mixpanel = MixpanelAPI.getInstance(context, context.getResources().getString(R.string.mixpanel_token));

        return new FavGridViewInfoAdapter(itemView);
    }

    @Override
    public void onBindViewHolder(final FavGridViewInfoAdapter holder, int position) {
        ArrayList<String> imageUrls = Util.favouriteDishesInfos.get(position).favDishPhoto;
        String imageUrl = imageUrls.get(0);
        Picasso.with(getContext())
                .load(imageUrl)
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        holder.rlFavCarview.setBackground(new BitmapDrawable(bitmap));
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                });

        final int favPos = position;
        holder.favFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    final JSONObject properties = new JSONObject();
                    properties.put("Dish popup in favourites list", "favourites");
                    mixpanel.track("Dish popup in favourites list", properties);
                } catch (final JSONException e) {
                    throw new RuntimeException("Could not encode hour of the day in JSON");
                }
                Util.setFavPosition(favPos);
                FragmentActivity activity = (FragmentActivity) (context);
                FragmentManager fm = activity.getSupportFragmentManager();
                FavDishDialogFragment dialogFragment = FavDishDialogFragment.getInstance();
                dialogFragment.show(fm, "fav_dialog_fragment");
            }
        });
        holder.favDishName.setText(Util.favouriteDishesInfos.get(position).getFavDishName());
        int favDishNature = Util.favouriteDishesInfos.get(position).getFavDishNature();
        if (favDishNature == 1) {
            holder.eggTag.setVisibility(View.GONE);
            holder.vegTag.setVisibility(View.VISIBLE);
            holder.nonVegTag.setVisibility(View.GONE);

        } else if (favDishNature == 2) {
            holder.eggTag.setVisibility(View.VISIBLE);
            holder.vegTag.setVisibility(View.GONE);
            holder.nonVegTag.setVisibility(View.GONE);
        } else {
            holder.eggTag.setVisibility(View.GONE);
            holder.vegTag.setVisibility(View.GONE);
            holder.nonVegTag.setVisibility(View.VISIBLE);
        }

        final int favGenericDishId = Util.favouriteDishesInfos.get(position).getFavDishGenericDishId();
        holder.favButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String source = "favActivity";

                try {
                    final JSONObject properties = new JSONObject();
                    properties.put("Dislike", "favourites");
                    mixpanel.track("Dislike", properties);
                } catch (final JSONException e) {
                    throw new RuntimeException("Could not encode hour of the day in JSON");
                }

                removeDishFromFav(source, favGenericDishId);
                Util.favouriteDishesInfos.remove(favPos);
                notifyItemRemoved(favPos);
                notifyItemRangeChanged(favPos, getItemCount());

            }
        });


        final String favRecipeUrl = Util.favouriteDishesInfos.get(position).getFavDishRecipeUrl();
        holder.favEatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    final JSONObject properties = new JSONObject();
                    properties.put("Dineoptions button in favourites", "favourites");
                    mixpanel.track("Dineoptions button in favourites", properties);
                } catch (final JSONException e) {
                    throw new RuntimeException("Could not encode hour of the day in JSON");
                }
                Util.setGenericDishIdTab(favGenericDishId);
                Util.setRecipeUrl(favRecipeUrl);

                BottomSheetDialogFragment bottomSheetDialogFragment = new BottomSheetFragment();
                bottomSheetDialogFragment.show(((FragmentActivity) context).getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
            }
        });
    }


    @Override
    public int getItemCount() {
        return Util.favouriteDishesInfos.size();
    }

    static class FavGridViewInfoAdapter extends RecyclerView.ViewHolder {

        TextView favDishName;
        private ImageView vegTag, eggTag, nonVegTag, isSpicyTag, hasAlcoholTag;
        private Button favButton, favEatButton;
        private RelativeLayout rlFavCarview;
        private FrameLayout favFrame;

        FavGridViewInfoAdapter(View view) {
            super(view);

            rlFavCarview = (RelativeLayout) view.findViewById(R.id.cv_rl_favs);
            favFrame = (FrameLayout) view.findViewById(R.id.fav_frame);
            favDishName = (TextView) view.findViewById(R.id.fav_dish_name);
            favDishName.setTypeface(Util.opensanslight);
            vegTag = (ImageView) view.findViewById(R.id.veg_tag);
            eggTag = (ImageView) view.findViewById(R.id.egg_tag);
            nonVegTag = (ImageView) view.findViewById(R.id.non_veg_tag);
            isSpicyTag = (ImageView) view.findViewById(R.id.spicy_tag);
            hasAlcoholTag = (ImageView) view.findViewById(R.id.alcohol_tag);
            favButton = (Button) view.findViewById(R.id.fav_favourites);
            favEatButton = (Button) view.findViewById(R.id.fav_dining);

        }
    }

    private void removeDishFromFav(String source, int favGenericDishId) {
        final FavDishAddRemHelper favDishAddRemHelper = new FavDishAddRemHelper(DishqApplication.getUniqueID(),
                source, favGenericDishId, 0);
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
