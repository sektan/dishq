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

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import version1.dishq.dishq.R;
import version1.dishq.dishq.server.Response.DeliveryTabResponse;
import version1.dishq.dishq.ui.DeliveryMenuActivity;
import version1.dishq.dishq.util.DishqApplication;
import version1.dishq.dishq.util.Util;

/**
 * Created by dishq on 03-01-2017.
 * Package name version1.dishq.dishq.
 */

public class DeliveryAdapter extends RecyclerView.Adapter<DeliveryAdapter.DeliveryRestInfoAdapter>{

    private Context context;

    public DeliveryAdapter(Context context) {
        this.context = context;
    }

    @Override
    public DeliveryRestInfoAdapter onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.cardview_delivery, viewGroup, false);
        return new DeliveryRestInfoAdapter(itemView);
    }

    @Override
    public void onBindViewHolder(final DeliveryRestInfoAdapter holder, int position) {
        final DeliveryTabResponse.DeliveryRestInfo deliveryRestInfo = Util.deliveryRestInfos.get(position);
        ArrayList<DeliveryTabResponse.DeliveryDishData> delPhotos = deliveryRestInfo.getDeliveryDishDatas();
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
                .into(holder.dishImage1);
        Picasso.with(DishqApplication.getContext())
                .load(imageUrl2)
                .into(holder.dishImage2);
        Picasso.with(DishqApplication.getContext())
                .load(imageUrl3)
                .into(holder.dishImage3);
        holder.rlDelivery.setBackgroundColor(ContextCompat.getColor(DishqApplication.getContext(), R.color.black));
        holder.delRestName.setText(deliveryRestInfo.getDelivRestName());

        int delPriceLvl = deliveryRestInfo.getDelivPriceLvl();
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

        holder.delDriveTime.setText(deliveryRestInfo.getDeliveryTime());

        holder.rlDelCardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Util.setDelRestId(deliveryRestInfo.getDelivRestId());
                Intent intent = new Intent(context, DeliveryMenuActivity.class);
                //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                //((Activity)context).finish();
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
            dishImage2 = (ImageView) view.findViewById(R.id.del_dish_image2);
            dishImage3 = (ImageView) view.findViewById(R.id.del_dish_image3);
            delRestName = (TextView) view.findViewById(R.id.del_rest_name);
            delRestCuisine = (TextView) view.findViewById(R.id.del_rest_cuisine);
            delRup1 = (TextView) view.findViewById(R.id.del_rup_1);
            delRup2 = (TextView) view.findViewById(R.id.del_rup_2);
            delRup3 = (TextView) view.findViewById(R.id.del_rup_3);
            delRup4 = (TextView) view.findViewById(R.id.del_rup_4);
            delDriveTime = (TextView) view.findViewById(R.id.del_drive_time);
            rlDelivery = (RelativeLayout) view.findViewById(R.id.cv_rl_del);
            rlDelCardview = (RelativeLayout) view.findViewById(R.id.rl_del);
        }
    }
}
