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
import android.widget.Button;
import android.widget.FrameLayout;
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

        /**
         * Setting the UI in case the restaurant is closed
        */
        if(Util.nearbyRestInfos.get(position).getNbIsOpenNow()) {
            holder.mfClosedFrame.setVisibility(View.GONE);
            holder.mfClosed.setVisibility(View.GONE);
        }else {
            holder.mfClosedFrame.setVisibility(View.VISIBLE);
            holder.mfClosed.setVisibility(View.VISIBLE);
        }
        /**********************************************************************/

        /**
         * Setting the onClickListener for when the restaurant card is clicked
         */
        holder.rlNearbyRest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Util.setDineRestId(Util.nearbyRestInfos.get(position).getNearByRestId());
                Intent intent = new Intent(context, DineoutMenuActivity.class);
                context.startActivity(intent);
            }
        });
        /***********************************************************************************/

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

        /**
         * Setting the price level for the restaurant
         */
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
        /***************************************************************************************************************/

        /**
         * Setting the drive time for the restaurant
         */
        holder.mfDriveTime.setText(Util.nearbyRestInfos.get(position).getDriveTime());
        /****************************************************************************************************************/

        /**
         * Setting the delivery time of the restaurant
         */
        holder.mfDeliveryTime.setText(Util.nearbyRestInfos.get(position).getNbDeliveryTime());
        if(Util.nearbyRestInfos.get(position).getNbHasHomeDelivery()) {
            if(Util.nearbyRestInfos.get(position).getNbCanBeDelivered()) {
                holder.mfDeliveryTime.setTextColor(ContextCompat.getColor(DishqApplication.getContext(), R.color.white));
            }else {
                holder.mfDeliveryTime.setTextColor(ContextCompat.getColor(DishqApplication.getContext(), R.color.noDelivery));
            }

        }else {
            holder.mfDeliveryTime.setTextColor(ContextCompat.getColor(DishqApplication.getContext(), R.color.noDelivery));
        }
        /********************************************************************************************************************/


        String nearByRestType = "";
        if(Util.nearbyRestInfos.get(position).getNearByRestTypeText()!=null) {
            for (String s : Util.nearbyRestInfos.get(position).getNearByRestTypeText()) {
                nearByRestType += s + " ";
            }
        }
        holder.nearByRestTypeText.setText(nearByRestType);

    }


    @Override
    public int getItemCount() {
        return Util.nearbyRestInfos.size();
    }

    static class MenuFinderRestInfoAdapter extends RecyclerView.ViewHolder {

        TextView mfRestName, mfRestAddr, mfRestCuisine, mfRup1,
                mfRup2, mfRup3, mfRup4, mfDriveTime, nearByRestTypeText,
                mfDeliveryTime;

        RelativeLayout rlNearbyRest;

        private ImageView cardBgImage;

        FrameLayout mfClosedFrame;

        Button mfClosed;

        MenuFinderRestInfoAdapter(View view) {
            super(view);

            mfRestName = (TextView) view.findViewById(R.id.dineout_rest_name);
            mfRestName.setTypeface(Util.opensanssemibold);
            mfRestAddr = (TextView) view.findViewById(R.id.dineout_rest_addr);
            mfRestAddr.setTypeface(Util.opensansregular);
            nearByRestTypeText = (TextView) view.findViewById(R.id.dineout_rest_type);
            nearByRestTypeText.setTypeface(Util.opensansregular);
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
            mfDeliveryTime = (TextView) view.findViewById(R.id.dineout_delivery_time);
            mfDeliveryTime.setTypeface(Util.opensanssemibold);
            mfClosedFrame = (FrameLayout) view.findViewById(R.id.frame_dine_rest_closed);
            mfClosed = (Button) view.findViewById(R.id.dine_rest_closed_button);
            mfClosed.setTypeface(Util.opensansregular);
        }
    }
}

