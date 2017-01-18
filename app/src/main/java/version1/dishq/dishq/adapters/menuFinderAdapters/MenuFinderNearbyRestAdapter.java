package version1.dishq.dishq.adapters.menuFinderAdapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;

import version1.dishq.dishq.R;
import version1.dishq.dishq.ui.DineoutMenuActivity;
import version1.dishq.dishq.util.DishqApplication;
import version1.dishq.dishq.util.Util;

/**
 * Created by dishq on 12-01-2017.
 * Package name version1.dishq.dishq.
 */

public class MenuFinderNearbyRestAdapter extends RecyclerView.Adapter<MenuFinderNearbyRestAdapter.MenuFinderRestInfoAdapter> {

    Context context;
    public MenuFinderNearbyRestAdapter(Context context) {
        this.context = context;
    }

    @Override
    public MenuFinderRestInfoAdapter onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.cardview_dineout, viewGroup, false);
        return new MenuFinderRestInfoAdapter(itemView);
    }

    @Override
    public void onBindViewHolder(final MenuFinderRestInfoAdapter holder, final int position) {
        ArrayList<String> imageUrls = Util.nearbyRestInfos.get(position).getNearByRestPhoto();
        String imageUrl = imageUrls.get(0);
        Picasso.with(DishqApplication.getContext())
                .load(imageUrl)
                .into(holder.cardBgImage);
        holder.rlNearbyRest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Util.setDineRestId(Util.nearbyRestInfos.get(position).getNearByRestId());
                Intent intent = new Intent(context, DineoutMenuActivity.class);
                context.startActivity(intent);
            }
        });
        holder.mfRestName.setText(Util.nearbyRestInfos.get(position).getNearByRestName());
        String nearByRestAddress = "";
        if(Util.nearbyRestInfos.get(position).getNearByRestAddress()!=null) {
            for (String s : Util.nearbyRestInfos.get(position).getNearByRestAddress()) {
                nearByRestAddress += s;
            }
        }
        holder.mfRestAddr.setText(nearByRestAddress);
        StringBuilder sb = new StringBuilder();
        if(Util.nearbyRestInfos.get(position).getNearByRestCuisineText()!=null) {
            for (String s : Util.nearbyRestInfos.get(position).getNearByRestCuisineText()) {
                if (sb.length() > 0) {
                    sb.append(',' + " ");
                }
                sb.append(s);
            }
        }
        String dineCusineText = sb.toString();
        holder.mfRestCuisine.setText(dineCusineText);
        int dinePriceLvl = Util.nearbyRestInfos.get(position).getPriceLevel();
        if (dinePriceLvl == 1) {
            holder.mfRup1.setTextColor(ContextCompat.getColor(DishqApplication.getContext(), R.color.rupeeGreen));
        }else if(dinePriceLvl == 2) {
            holder.mfRup1.setTextColor(ContextCompat.getColor(DishqApplication.getContext(), R.color.rupeeGreen));
            holder.mfRup2.setTextColor(ContextCompat.getColor(DishqApplication.getContext(), R.color.rupeeGreen));
        }else if(dinePriceLvl == 3) {
            holder.mfRup1.setTextColor(ContextCompat.getColor(DishqApplication.getContext(), R.color.rupeeGreen));
            holder.mfRup2.setTextColor(ContextCompat.getColor(DishqApplication.getContext(), R.color.rupeeGreen));
            holder.mfRup3.setTextColor(ContextCompat.getColor(DishqApplication.getContext(), R.color.rupeeGreen));
        }else if (dinePriceLvl == 4) {
            holder.mfRup1.setTextColor(ContextCompat.getColor(DishqApplication.getContext(), R.color.rupeeGreen));
            holder.mfRup2.setTextColor(ContextCompat.getColor(DishqApplication.getContext(), R.color.rupeeGreen));
            holder.mfRup3.setTextColor(ContextCompat.getColor(DishqApplication.getContext(), R.color.rupeeGreen));
            holder.mfRup4.setTextColor(ContextCompat.getColor(DishqApplication.getContext(), R.color.rupeeGreen));
        }

        holder.mfDriveTime.setText(Util.nearbyRestInfos.get(position).getDriveTime());
    }


    @Override
    public int getItemCount() {
        return Util.nearbyRestInfos.size();
    }

    public static class MenuFinderRestInfoAdapter extends RecyclerView.ViewHolder {

        protected TextView mfRestName, mfRestAddr, mfRestCuisine, mfRup1,
                mfRup2, mfRup3, mfRup4, mfDriveTime;

        protected RelativeLayout rlNearbyRest;

        private ImageView cardBgImage;

        public MenuFinderRestInfoAdapter(View view) {
            super(view);

            mfRestName = (TextView) view.findViewById(R.id.dineout_rest_name);
            mfRestName.setTypeface(Util.opensanssemibold);
            mfRestAddr = (TextView) view.findViewById(R.id.dineout_rest_addr);
            mfRestAddr.setTypeface(Util.opensansregular);
            mfRestCuisine = (TextView) view.findViewById(R.id.dineout_rest_cuisine);
            mfRup1 = (TextView) view.findViewById(R.id.dineout_rup_1);
            mfRup2 = (TextView) view.findViewById(R.id.dineout_rup_2);
            mfRup3 = (TextView) view.findViewById(R.id.dineout_rup_3);
            mfRup4 = (TextView) view.findViewById(R.id.dineout_rup_4);
            mfDriveTime = (TextView) view.findViewById(R.id.dineout_drive_time);
            mfDriveTime.setTypeface(Util.opensanssemibold);
            rlNearbyRest = (RelativeLayout) view.findViewById(R.id.cv_rl_dineout);
            cardBgImage = (ImageView) view.findViewById(R.id.card_bg_image);
            cardBgImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
    }
}

