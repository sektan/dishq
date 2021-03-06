package version1.dishq.dishq.adapters.bottomSheetAdapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mixpanel.android.mpmetrics.MixpanelAPI;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import version1.dishq.dishq.R;
import version1.dishq.dishq.server.Response.DeliveryTabResponse;
import version1.dishq.dishq.ui.DeliveryMenuActivity;
import version1.dishq.dishq.ui.DineoutMenuActivity;
import version1.dishq.dishq.util.DishqApplication;
import version1.dishq.dishq.util.Util;

/**
 * Created by dishq on 03-01-2017.
 * Package name version1.dishq.dishq.
 */

public class DeliveryAdapter extends RecyclerView.Adapter<DeliveryAdapter.DeliveryRestInfoAdapter>{

    private Context context;
    private MixpanelAPI mixpanel = null;

    public DeliveryAdapter(Context context) {
        this.context = context;
    }

    @Override
    public DeliveryRestInfoAdapter onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.cardview_delivery, viewGroup, false);
        //MixPanel Instantiation
        mixpanel = MixpanelAPI.getInstance(context, context.getResources().getString(R.string.mixpanel_token));

        return new DeliveryRestInfoAdapter(itemView);
    }

    @Override
    public void onBindViewHolder(final DeliveryRestInfoAdapter holder, int position) {
        ArrayList<DeliveryTabResponse.DeliveryDishData> delPhotos = Util.deliveryRestInfos.get(position).getDeliveryDishDatas();
        DeliveryTabResponse.DeliveryDishData listOfPhotos1 = delPhotos.get(0);
        DeliveryTabResponse.DeliveryDishData listOfPhotos2 = delPhotos.get(1);
        DeliveryTabResponse.DeliveryDishData listOfPhotos3 = delPhotos.get(2);
        ArrayList<String> photos1 = listOfPhotos1.getDelivPhoto();
        ArrayList<String> photos2 = listOfPhotos2.getDelivPhoto();
        ArrayList<String> photos3 = listOfPhotos3.getDelivPhoto();
        String imageUrl1 = photos1.get(0);
        Log.d("ImageUrls", "First image url: " + imageUrl1);
        String imageUrl2 = photos2.get(0);
        Log.d("ImageUrls", "Second image url: " + imageUrl2);
        String imageUrl3 = photos3.get(0);
        Log.d("ImageUrls", "Third image url: " + imageUrl3);
        Picasso.with(DishqApplication.getContext())
                .load(imageUrl1)
                .fit()
                .centerCrop()
                .into(holder.dishImage1);
        Picasso.with(DishqApplication.getContext())
                .load(imageUrl2)
                .fit()
                .centerCrop()
                .into(holder.dishImage2);
        Picasso.with(DishqApplication.getContext())
                .load(imageUrl3)
                .fit()
                .centerCrop()
                .into(holder.dishImage3);
        holder.delRestName.setText(Util.deliveryRestInfos.get(position).getDelivRestName());

        StringBuilder sb = new StringBuilder();
        if(Util.deliveryRestInfos.get(position).getDelivCuisineText()!=null) {
            for (String s : Util.deliveryRestInfos.get(position).getDelivCuisineText()) {
                if (sb.length() > 0) {
                    sb.append(',' + " ");
                }
                sb.append(s);
            }
        }
        String dineCusineText = sb.toString();
        holder.delRestCuisine.setText(dineCusineText);

        int delPriceLvl = Util.deliveryRestInfos.get(position).getDelivPriceLvl();
        if (delPriceLvl == 1) {
            holder.delRup1.setTextColor(ContextCompat.getColor(DishqApplication.getContext(), R.color.rupeeGreen));
        }else if(delPriceLvl == 2) {
            holder.delRup1.setTextColor(ContextCompat.getColor(DishqApplication.getContext(), R.color.rupeeGreen));
            holder.delRup2.setTextColor(ContextCompat.getColor(DishqApplication.getContext(), R.color.rupeeGreen));
        }else if(delPriceLvl == 3) {
            holder.delRup1.setTextColor(ContextCompat.getColor(DishqApplication.getContext(), R.color.rupeeGreen));
            holder.delRup2.setTextColor(ContextCompat.getColor(DishqApplication.getContext(), R.color.rupeeGreen));
            holder.delRup3.setTextColor(ContextCompat.getColor(DishqApplication.getContext(), R.color.rupeeGreen));
        }else if (delPriceLvl == 4) {
            holder.delRup1.setTextColor(ContextCompat.getColor(DishqApplication.getContext(), R.color.rupeeGreen));
            holder.delRup2.setTextColor(ContextCompat.getColor(DishqApplication.getContext(), R.color.rupeeGreen));
            holder.delRup3.setTextColor(ContextCompat.getColor(DishqApplication.getContext(), R.color.rupeeGreen));
            holder.delRup4.setTextColor(ContextCompat.getColor(DishqApplication.getContext(), R.color.rupeeGreen));
        }

        holder.delDriveTime.setText(Util.deliveryRestInfos.get(position).getDeliveryTime());

        final int restId = Util.deliveryRestInfos.get(position).getDelivRestId();
        holder.rlDelCardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    final JSONObject properties = new JSONObject();
                    properties.put("Delivery Dineout restaurants menu", "bottomsheet");
                    mixpanel.track("Delivery Dineout restaurants menu", properties);
                } catch (final JSONException e) {
                    throw new RuntimeException("Could not encode hour of the day in JSON");
                }
                //Util.setDelRestId(restId);
                Util.setDineRestId(restId);
                Intent intent = new Intent(context, DineoutMenuActivity.class);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return Util.deliveryRestInfos.size();
    }

    public static class DeliveryRestInfoAdapter extends RecyclerView.ViewHolder {

        protected TextView delRestName, delRestCuisine, delRup1,
                delRup2, delRup3, delRup4, delDriveTime;
        protected ImageView dishImage1, dishImage2, dishImage3;

        protected RelativeLayout rlDelivery, rlDelCardview;

        public DeliveryRestInfoAdapter(View view) {
            super(view);

            dishImage1 = (ImageView) view.findViewById(R.id.del_dish_image1);
            dishImage1.setScaleType(ImageView.ScaleType.CENTER_CROP);
            dishImage2 = (ImageView) view.findViewById(R.id.del_dish_image2);
            dishImage2.setScaleType(ImageView.ScaleType.CENTER_CROP);
            dishImage3 = (ImageView) view.findViewById(R.id.del_dish_image3);
            dishImage3.setScaleType(ImageView.ScaleType.CENTER_CROP);
            delRestName = (TextView) view.findViewById(R.id.del_rest_name);
            delRestName.setTypeface(Util.opensanssemibold);
            delRestCuisine = (TextView) view.findViewById(R.id.del_rest_cuisine);
            delRestCuisine.setTypeface(Util.opensansregular);
            delRup1 = (TextView) view.findViewById(R.id.del_rup_1);
            delRup2 = (TextView) view.findViewById(R.id.del_rup_2);
            delRup3 = (TextView) view.findViewById(R.id.del_rup_3);
            delRup4 = (TextView) view.findViewById(R.id.del_rup_4);
            delDriveTime = (TextView) view.findViewById(R.id.del_drive_time);
            delDriveTime.setTypeface(Util.opensanssemibold);
            rlDelivery = (RelativeLayout) view.findViewById(R.id.cv_rl_del);
            rlDelCardview = (RelativeLayout) view.findViewById(R.id.rl_del);
        }
    }
}
