package version1.dishq.dishq.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import version1.dishq.dishq.OnClickCallbacks;
import version1.dishq.dishq.R;
import version1.dishq.dishq.fragments.dialogfragment.filters.models.quickfilters.FoodMoodFilter;
import version1.dishq.dishq.util.Util;

/**
 * Created by kavin.prabhu on 30/12/16.
 */

public class FilterMoodRecyclerAdapter extends RecyclerView.Adapter<FilterMoodRecyclerAdapter.MoodViewHolder> {

    private final Context context;
    private final List<FoodMoodFilter> filterList;
    private boolean isItemAlreadySelected = false;
    private int selectedPos = -1, lastSelectedPos = -1;
    private OnClickCallbacks.OnClickMoodFilterItemCallback itemCallback;

    public FilterMoodRecyclerAdapter(Context context, List<FoodMoodFilter> filterList,
                                     OnClickCallbacks.OnClickMoodFilterItemCallback itemCallback) {
        this.context = context;
        this.filterList = filterList;
        this.itemCallback = itemCallback;

        if(Util.getMoodPosition() > -1){
            this.selectedPos = Util.getMoodPosition();
            this.isItemAlreadySelected = true;
        }
    }

    public void clearSelection(){
        this.selectedPos = -1; this.lastSelectedPos = -1;
    }

    @Override
    public MoodViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_filter_item, parent, false);
        return new MoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MoodViewHolder holder, int position) {

        holder.textFilterResults.setText(filterList.get(position).getName());

        if (selectedPos == position) {
            holder.textFilterResults.setBackgroundResource(R.drawable.filter_mood_item_state_selected);
            holder.textFilterResults.setTextColor(ContextCompat.getColor(context, R.color.black));
        } else if (lastSelectedPos != -1) {
            lastSelectedPos = -1;
            holder.textFilterResults.setBackgroundResource(R.drawable.filter_mood_item_state_unselected);
            holder.textFilterResults.setTextColor(ContextCompat.getColor(context, R.color.white));
        } else {
            holder.textFilterResults.setBackgroundResource(R.drawable.filter_mood_item_state_unselected);
            holder.textFilterResults.setTextColor(ContextCompat.getColor(context, R.color.white));
        }
    }

    public int getSelectedPos() {
        return this.selectedPos;
    }

    public void setSelectedPos(int selectedPos) {
        this.selectedPos = selectedPos;
    }

    @Override
    public int getItemCount() {
        return filterList.size();
    }

    class MoodViewHolder extends RecyclerView.ViewHolder {

        TextView textFilterResults;
        ViewGroup layoutTextHolder;

        MoodViewHolder(final View itemView) {
            super(itemView);

            textFilterResults = (TextView) itemView.findViewById(R.id.text_filter_results);
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