package version1.dishq.dishq.adapters.bottomSheetAdapters;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import version1.dishq.dishq.R;
import version1.dishq.dishq.server.Response.DeliveryTabResponse;
import version1.dishq.dishq.util.DishqApplication;
import version1.dishq.dishq.util.Util;

/**
 * Created by dishq on 03-01-2017.
 * Package name version1.dishq.dishq.
 */

public class DeliveryAdapter extends RecyclerView.Adapter<DeliveryAdapter.DeliveryRestInfoAdapter>{

    private ArrayList<DeliveryTabResponse.DeliveryRestInfo> deliveryRestInfos;

    public DeliveryAdapter (ArrayList<DeliveryTabResponse.DeliveryRestInfo> deliveryRestInfos) {
        this.deliveryRestInfos = deliveryRestInfos;
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
        DeliveryTabResponse.DeliveryRestInfo deliveryRestInfo = Util.deliveryRestInfos.get(position);
        ArrayList<DeliveryTabResponse.DeliveryDishData> delPhotos = deliveryRestInfo.deliveryDishDatas;
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
    }

    @Override
    public int getItemCount() {
        return Util.deliveryRestInfos.size();
    }

    public static class DeliveryRestInfoAdapter extends RecyclerView.ViewHolder {

        protected TextView delRestName, delRestCuisine, delRup1,
                delRup2, delRup3, delRup4, delDriveTime;

        protected RelativeLayout rlDelivery;

        public DeliveryRestInfoAdapter(View view) {
            super(view);

            delRestName = (TextView) view.findViewById(R.id.del_rest_name);
            delRestCuisine = (TextView) view.findViewById(R.id.del_rest_cuisine);
            delRup1 = (TextView) view.findViewById(R.id.del_rup_1);
            delRup2 = (TextView) view.findViewById(R.id.del_rup_2);
            delRup3 = (TextView) view.findViewById(R.id.del_rup_3);
            delRup4 = (TextView) view.findViewById(R.id.del_rup_4);
            delDriveTime = (TextView) view.findViewById(R.id.del_drive_time);
            rlDelivery = (RelativeLayout) view.findViewById(R.id.cv_rl_del);
        }
    }
}
