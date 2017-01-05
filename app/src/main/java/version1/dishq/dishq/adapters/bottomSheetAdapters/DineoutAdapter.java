package version1.dishq.dishq.adapters.bottomSheetAdapters;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import version1.dishq.dishq.R;
import version1.dishq.dishq.server.Response.DineoutTabResponse;
import version1.dishq.dishq.util.DishqApplication;
import version1.dishq.dishq.util.Util;

/**
 * Created by dishq on 03-01-2017.
 * Package name version1.dishq.dishq.
 */

public class DineoutAdapter extends RecyclerView.Adapter<DineoutAdapter.DineoutRestInfoAdapter> {

    public DineoutAdapter () {
    }

    @Override
    public DineoutRestInfoAdapter onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.cardview_dineout, viewGroup, false);
        return new DineoutRestInfoAdapter(itemView);
    }

    @Override
    public void onBindViewHolder(final DineoutAdapter.DineoutRestInfoAdapter holder, int position) {
        DineoutTabResponse.DineoutRestInfo dineoutRestInfo = Util.dineoutRestInfos.get(position);
        String imageUrl = dineoutRestInfo.getDineRestPhoto().get(0);
        Picasso.with(DishqApplication.getContext())
                .load(imageUrl)
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        holder.rlDineout.setBackground(new BitmapDrawable(bitmap));
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                });
        holder.dineRestName.setText(dineoutRestInfo.getDineRestName());
        String dineRestAddress = "";
        for (String s : dineoutRestInfo.getDineRestAddr())
            dineRestAddress += s;
        holder.dineRestAddr.setText(dineRestAddress);
        StringBuilder sb = new StringBuilder();
        for (String s : dineoutRestInfo.getDineCuisineText()) {
            if (sb.length() > 0) {
                sb.append(',' + " ");
            }
            sb.append(s);
        }
        String dineCusineText = sb.toString();
        holder.dineRestCuisine.setText(dineCusineText);
        int dinePriceLvl = dineoutRestInfo.getDineoutPriceLvl();
        if (dinePriceLvl == 1) {
            holder.dineRup1.setTextColor(ContextCompat.getColor(DishqApplication.getContext(), R.color.rupeeGreen));
        }else if(dinePriceLvl == 2) {
            holder.dineRup1.setTextColor(ContextCompat.getColor(DishqApplication.getContext(), R.color.rupeeGreen));
            holder.dineRup2.setTextColor(ContextCompat.getColor(DishqApplication.getContext(), R.color.rupeeGreen));
        }else if(dinePriceLvl == 3) {
            holder.dineRup1.setTextColor(ContextCompat.getColor(DishqApplication.getContext(), R.color.rupeeGreen));
            holder.dineRup2.setTextColor(ContextCompat.getColor(DishqApplication.getContext(), R.color.rupeeGreen));
            holder.dineRup3.setTextColor(ContextCompat.getColor(DishqApplication.getContext(), R.color.rupeeGreen));
        }else if (dinePriceLvl == 4) {
            holder.dineRup1.setTextColor(ContextCompat.getColor(DishqApplication.getContext(), R.color.rupeeGreen));
            holder.dineRup2.setTextColor(ContextCompat.getColor(DishqApplication.getContext(), R.color.rupeeGreen));
            holder.dineRup3.setTextColor(ContextCompat.getColor(DishqApplication.getContext(), R.color.rupeeGreen));
            holder.dineRup4.setTextColor(ContextCompat.getColor(DishqApplication.getContext(), R.color.rupeeGreen));
        }

        holder.dineDriveTime.setText(dineoutRestInfo.getDineDriveTime());
    }


    @Override
    public int getItemCount() {
        return Util.dineoutRestInfos.size();
    }

    public static class DineoutRestInfoAdapter extends RecyclerView.ViewHolder {

        protected TextView dineRestName, dineRestAddr, dineRestCuisine, dineRup1,
                dineRup2, dineRup3, dineRup4, dineDriveTime;

        protected RelativeLayout rlDineout;

        public DineoutRestInfoAdapter(View view) {
            super(view);

            dineRestName = (TextView) view.findViewById(R.id.dineout_rest_name);
            dineRestName.setTypeface(Util.opensanssemibold);
            dineRestAddr = (TextView) view.findViewById(R.id.dineout_rest_addr);
            dineRestAddr.setTypeface(Util.opensansregular);
            dineRestCuisine = (TextView) view.findViewById(R.id.dineout_rest_cuisine);
            dineRup1 = (TextView) view.findViewById(R.id.dineout_rup_1);
            dineRup2 = (TextView) view.findViewById(R.id.dineout_rup_2);
            dineRup3 = (TextView) view.findViewById(R.id.dineout_rup_3);
            dineRup4 = (TextView) view.findViewById(R.id.dineout_rup_4);
            dineDriveTime = (TextView) view.findViewById(R.id.dineout_drive_time);
            dineDriveTime.setTypeface(Util.opensanssemibold);
            rlDineout = (RelativeLayout) view.findViewById(R.id.cv_rl_dineout);
        }
    }
}
