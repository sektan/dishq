package version1.dishq.dishq.adapters.filtersAdapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import version1.dishq.dishq.OnClickCallbacks;
import version1.dishq.dishq.R;
import version1.dishq.dishq.custom.OnClickFilterCallbacks;
import version1.dishq.dishq.util.Util;

/**
 * Created by dishq on 07-02-2017.
 * Package name version1.dishq.dishq.
 */

public class FoodFilterRecyclerAdapter extends RecyclerView.Adapter<FoodFilterRecyclerAdapter.FoodFilterViewHolder> {

    private final Context context;
    private boolean isItemAlreadySelected = false;

    private int selectedPos = -1, lastSelectedPos = -1;
    private OnClickFilterCallbacks.OnClickFoodFilterItemCallback itemCallback;

    public FoodFilterRecyclerAdapter(Context context, OnClickFilterCallbacks.OnClickFoodFilterItemCallback itemCallback) {
        this.context = context;
        this.itemCallback = itemCallback;

        if(Util.getQuickFilterPosition() > - 1) {
            this.selectedPos = Util.getQuickFilterPosition();
            this.isItemAlreadySelected = true;
        }
    }

    public void clearSelection(){
        this.selectedPos = -1; this.lastSelectedPos = -1;
    }

    @Override
    public FoodFilterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_filter_item, parent, false);
        return new FoodFilterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FoodFilterViewHolder holder, int position) {
        holder.textFilterResults.setText(Util.foodFiltersInfos.get(position).getFoodFilterName());

        if (selectedPos == position) {
            holder.textFilterResults.setBackgroundResource(R.drawable.filter_quick_item_state_selected);
            holder.textFilterResults.setTextColor(ContextCompat.getColor(context, R.color.black));
        } else if (lastSelectedPos != -1) {
            lastSelectedPos = -1;
            holder.textFilterResults.setBackgroundResource(R.drawable.filter_quick_item_state_unselected);
            holder.textFilterResults.setTextColor(ContextCompat.getColor(context, R.color.white));
        } else {
            holder.textFilterResults.setBackgroundResource(R.drawable.filter_quick_item_state_unselected);
            holder.textFilterResults.setTextColor(ContextCompat.getColor(context, R.color.white));
        }
    }

    public int getSelectedPos() {
        return selectedPos;
    }

    public void setSelectedPos(int selectedPos) {
        this.selectedPos = selectedPos;
    }

    @Override
    public int getItemCount() {
        return Util.foodFiltersInfos.size();
    }

    class FoodFilterViewHolder extends RecyclerView.ViewHolder {

        TextView textFilterResults;
        ViewGroup layoutTextHolder;

        FoodFilterViewHolder(final View itemView) {
            super(itemView);

            textFilterResults = (TextView) itemView.findViewById(R.id.text_filter_results);
            textFilterResults.setTypeface(Util.opensansregular);
            layoutTextHolder = (ViewGroup) itemView.findViewById(R.id.layout_text_holder);

            layoutTextHolder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // If the clicked item is already selected then remove the selection

                    if (selectedPos == getAdapterPosition()) {
                        selectedPos = lastSelectedPos = -1;
                        isItemAlreadySelected = false;
                        notifyItemChanged(getAdapterPosition());

                        if (itemCallback != null) {
                            itemCallback.onItemClicked(getAdapterPosition(), false);
                        }
                    } else {
                        // If the any item is already selected then swap the positions and notify both
                        if (isItemAlreadySelected) {
                            lastSelectedPos = selectedPos;
                            notifyItemChanged(lastSelectedPos);

                            selectedPos = getAdapterPosition();
                            notifyItemChanged(selectedPos);
                        } else {
                            // If its a first selection then select that item
                            isItemAlreadySelected = true;
                            selectedPos = getAdapterPosition();
                            notifyItemChanged(getAdapterPosition());
                        }

                        if (itemCallback != null) {
                            itemCallback.onItemClicked(getAdapterPosition(), true);
                        }
                    }
                }
            });
        }
    }
}
