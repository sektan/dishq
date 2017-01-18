package version1.dishq.dishq.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import version1.dishq.dishq.R;
import version1.dishq.dishq.util.DishqApplication;
import version1.dishq.dishq.util.Util;

/**
 * Created by dishq on 18-01-2017.
 * Package name version1.dishq.dishq.
 */

public class HorizontalAdapter extends RecyclerView.Adapter<HorizontalAdapter.HorizontalHolder> {

    Context context;

    public HorizontalAdapter(Context context) {
        this.context = context;
    }

    @Override
    public HorizontalHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_imageview_horizontal, parent, false);
        return new HorizontalHolder(itemView);
    }

    @Override
    public void onBindViewHolder(HorizontalHolder holder, int position) {
        Picasso.with(DishqApplication.getContext())
                .load(Util.dishSmallPic.get(position))
                .fit()
                .centerCrop()
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return Util.dishSmallPic.size();
    }

    public class HorizontalHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        public HorizontalHolder(View view) {
            super(view);
            imageView=(ImageView) view.findViewById(R.id.feedback_images);
        }
    }
}
