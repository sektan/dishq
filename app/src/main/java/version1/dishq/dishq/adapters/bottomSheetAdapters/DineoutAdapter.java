package version1.dishq.dishq.adapters.bottomSheetAdapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
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

import com.mixpanel.android.mpmetrics.MixpanelAPI;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONException;
import org.json.JSONObject;

import version1.dishq.dishq.R;
import version1.dishq.dishq.server.Response.DineoutTabResponse;
import version1.dishq.dishq.ui.DineoutMenuActivity;
import version1.dishq.dishq.util.DishqApplication;
import version1.dishq.dishq.util.Util;

/**
 * Created by dishq on 03-01-2017.
 * Package name version1.dishq.dishq.
 */

public class DineoutAdapter extends RecyclerView.Adapter<DineoutAdapter.DineoutRestInfoAdapter> {

    Context context;
    private MixpanelAPI mixpanel = null;
    public DineoutAdapter(Context context) {
        this.context = context;
    }

    @Override
    public DineoutRestInfoAdapter onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.cardview_dineout, viewGroup, false);
        //MixPanel Instantiation
        mixpanel = MixpanelAPI.getInstance(context, context.getResources().getString(R.string.mixpanel_token));

        return new DineoutRestInfoAdapter(itemView);
    }

    @Override
    public void onBindViewHolder(final DineoutRestInfoAdapter holder, int position) {
        String imageUrl = Util.dineoutRestInfos.get(position).getDineRestPhoto().get(0);
        Picasso.with(DishqApplication.getContext())
                .load(imageUrl)
                .fit()
                .centerCrop()
                .into(holder.dineoutCardBgImage);

        /**
         * Setting the UI in case the restaurant is closed
         */
        if(Util.dineoutRestInfos.get(position).getDineIsOpenNow()) {
            holder.dineClosedFrame.setVisibility(View.GONE);
            holder.dineClosed.setVisibility(View.GONE);
        }else {
            holder.dineClosedFrame.setVisibility(View.VISIBLE);
            holder.dineClosed.setVisibility(View.VISIBLE);
        }

        final int restId = Util.dineoutRestInfos.get(position).getDineRestId();
        holder.rlDineout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    final JSONObject properties = new JSONObject();
                    properties.put("Dineout restaurant menu", "bottomsheet");
                    mixpanel.track("Dineout restaurant menu", properties);
                } catch (final JSONException e) {
                    throw new RuntimeException("Could not encode hour of the day in JSON");
                }
                Util.setDineRestId(restId);
                Intent intent = new Intent(context, DineoutMenuActivity.class);
                context.startActivity(intent);
            }
        });
        holder.dineRestName.setText(Util.dineoutRestInfos.get(position).getDineRestName());
        String dineRestAddress = "";
        if(Util.dineoutRestInfos.get(position).getDineRestAddr()!=null) {
            for (String s : Util.dineoutRestInfos.get(position).getDineRestAddr()) {
                dineRestAddress += s;
            }
        }
        holder.dineRestAddr.setText(dineRestAddress);
        StringBuilder sb = new StringBuilder();
        if(Util.dineoutRestInfos.get(position).getDineCuisineText()!=null) {
            for (String s : Util.dineoutRestInfos.get(position).getDineCuisineText()) {
                if (sb.length() > 0) {
                    sb.append(',' + " ");
                }
                sb.append(s);
            }
        }
        String dineCusineText = sb.toString();
        holder.dineRestCuisine.setText(dineCusineText);

        /**
         * Setting the price level for the restaurant
         */
        int dinePriceLvl = Util.dineoutRestInfos.get(position).getDineoutPriceLvl();
        if (dinePriceLvl == 1) {
            holder.dineRup1.setTextColor(ContextCompat.getColor(DishqApplication.getContext(), R.color.rupeeGreen));
            holder.dineRup2.setTextColor(ContextCompat.getColor(DishqApplication.getContext(), R.color.white));
            holder.dineRup3.setTextColor(ContextCompat.getColor(DishqApplication.getContext(), R.color.white));
            holder.dineRup4.setTextColor(ContextCompat.getColor(DishqApplication.getContext(), R.color.white));
        }else if(dinePriceLvl == 2) {
            holder.dineRup1.setTextColor(ContextCompat.getColor(DishqApplication.getContext(), R.color.rupeeGreen));
            holder.dineRup2.setTextColor(ContextCompat.getColor(DishqApplication.getContext(), R.color.rupeeGreen));
            holder.dineRup3.setTextColor(ContextCompat.getColor(DishqApplication.getContext(), R.color.white));
            holder.dineRup4.setTextColor(ContextCompat.getColor(DishqApplication.getContext(), R.color.white));
        }else if(dinePriceLvl == 3) {
            holder.dineRup1.setTextColor(ContextCompat.getColor(DishqApplication.getContext(), R.color.rupeeGreen));
            holder.dineRup2.setTextColor(ContextCompat.getColor(DishqApplication.getContext(), R.color.rupeeGreen));
            holder.dineRup3.setTextColor(ContextCompat.getColor(DishqApplication.getContext(), R.color.rupeeGreen));
            holder.dineRup4.setTextColor(ContextCompat.getColor(DishqApplication.getContext(), R.color.white));
        }else if (dinePriceLvl == 4) {
            holder.dineRup1.setTextColor(ContextCompat.getColor(DishqApplication.getContext(), R.color.rupeeGreen));
            holder.dineRup2.setTextColor(ContextCompat.getColor(DishqApplication.getContext(), R.color.rupeeGreen));
            holder.dineRup3.setTextColor(ContextCompat.getColor(DishqApplication.getContext(), R.color.rupeeGreen));
            holder.dineRup4.setTextColor(ContextCompat.getColor(DishqApplication.getContext(), R.color.rupeeGreen));
        }
        /***************************************************************************************************************/

        /**
         * Setting the drive time for the restaurant
         */
        holder.dineDriveTime.setText(Util.dineoutRestInfos.get(position).getDineDriveTime());
        /****************************************************************************************************************/

        /**
         * Setting the delivery time of the restaurant
         */
        holder.dineDeliveryTime.setText(Util.dineoutRestInfos.get(position).getDineDeliveryTime());

        /********************************************************************************************************************/

        String dineRestType = "";
        if(Util.dineoutRestInfos.get(position).getDineRestType()!=null) {
            for (String s : Util.dineoutRestInfos.get(position).getDineRestType()) {
                dineRestType += s;
            }
        }
        holder.dineRestType.setText(dineRestType);
    }


    @Override
    public int getItemCount() {
        return Util.dineoutRestInfos.size();
    }

    static class DineoutRestInfoAdapter extends RecyclerView.ViewHolder {

        TextView dineRestName, dineRestAddr, dineRestCuisine, dineRup1,
                dineRup2, dineRup3, dineRup4, dineDriveTime, dineRestType, dineDeliveryTime;
        RelativeLayout rlDineout;
        ImageView dineoutCardBgImage;
        FrameLayout dineClosedFrame;
        Button dineClosed;

        DineoutRestInfoAdapter(View view) {
            super(view);

            dineRestName = (TextView) view.findViewById(R.id.dineout_rest_name);
            dineRestName.setTypeface(Util.opensanssemibold);
            dineRestAddr = (TextView) view.findViewById(R.id.dineout_rest_addr);
            dineRestAddr.setTypeface(Util.opensansregular);
            dineRestCuisine = (TextView) view.findViewById(R.id.dineout_rest_cuisine);
            dineRestCuisine.setTypeface(Util.opensansregular);
            dineRup1 = (TextView) view.findViewById(R.id.dineout_rup_1);
            dineRup2 = (TextView) view.findViewById(R.id.dineout_rup_2);
            dineRup3 = (TextView) view.findViewById(R.id.dineout_rup_3);
            dineRup4 = (TextView) view.findViewById(R.id.dineout_rup_4);
            dineDriveTime = (TextView) view.findViewById(R.id.dineout_drive_time);
            dineDriveTime.setTypeface(Util.opensanssemibold);
            dineRestType = (TextView) view.findViewById(R.id.dineout_rest_type);
            dineRestType.setTypeface(Util.opensanssemibold);
            rlDineout = (RelativeLayout) view.findViewById(R.id.cv_rl_dineout);
            dineoutCardBgImage = (ImageView) view.findViewById(R.id.card_bg_image);
            dineoutCardBgImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
            dineDeliveryTime = (TextView) view.findViewById(R.id.dineout_delivery_time);
            dineDeliveryTime.setTypeface(Util.opensanssemibold);
            dineClosedFrame = (FrameLayout) view.findViewById(R.id.frame_dine_rest_closed);
            dineClosed = (Button) view.findViewById(R.id.dine_rest_closed_button);
            dineClosed.setTypeface(Util.opensanssemibold);
        }
    }
}
