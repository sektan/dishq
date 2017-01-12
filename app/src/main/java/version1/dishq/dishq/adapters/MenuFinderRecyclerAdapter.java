package version1.dishq.dishq.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import version1.dishq.dishq.R;
import version1.dishq.dishq.server.Response.MenuFinderResponse;

/**
 * Created by kavin.prabhu on 09/01/17.
 */

public class MenuFinderRecyclerAdapter extends RecyclerView.Adapter<MenuFinderRecyclerAdapter.MenuFinderViewHolder> {

    private final Context context;
    private final List<MenuFinderResponse.Datum> datumList;

    public MenuFinderRecyclerAdapter(Context context, List<MenuFinderResponse.Datum> datumList) {
        this.context = context;
        this.datumList = datumList;
    }

    @Override
    public MenuFinderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_menu_finder_item, parent, false);
        return new MenuFinderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MenuFinderViewHolder holder, int position) {
        Picasso.with(context).load(datumList.get(position).getPhoto().get(0)).into(holder.imageBackground);
        holder.textTitle.setText(datumList.get(position).getRestaurantName());
        holder.textLocation.setText(datumList.get(position).getRestaurantAddress().get(0));
        holder.textDistance.setText(datumList.get(position).getDriveTime());
    }

    @Override
    public int getItemCount() {
        return datumList.size();
    }

    public class MenuFinderViewHolder extends RecyclerView.ViewHolder {

        ImageView imageBackground, imageCost;
        TextView textTitle, textLocation, textRestaurantType, textDistance;

        public MenuFinderViewHolder(View itemView) {
            super(itemView);

            imageBackground = (ImageView) itemView.findViewById(R.id.menu_finder_item_bg);
            imageCost = (ImageView) itemView.findViewById(R.id.menu_finder_dish_price);

            textTitle = (TextView) itemView.findViewById(R.id.menu_finder_restaurant_name);
            textLocation = (TextView) itemView.findViewById(R.id.menu_finder_restaurant_location);
            textRestaurantType = (TextView) itemView.findViewById(R.id.menu_finder_restaurant_type);
            textDistance = (TextView) itemView.findViewById(R.id.menu_finder_restaurant_distance);
        }
    }
}
