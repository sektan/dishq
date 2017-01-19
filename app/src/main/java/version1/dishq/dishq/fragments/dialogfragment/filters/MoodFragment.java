package version1.dishq.dishq.fragments.dialogfragment.filters;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import version1.dishq.dishq.OnClickCallbacks;
import version1.dishq.dishq.R;
import version1.dishq.dishq.adapters.FilterMoodRecyclerAdapter;
import version1.dishq.dishq.fragments.dialogfragment.filters.models.quickfilters.FoodMoodFilter;

/**
 * A simple {@link Fragment} subclass.
 */
public class MoodFragment extends Fragment implements OnClickCallbacks.OnClickMoodFilterItemCallback {

    RecyclerView recyclerView;
    FilterMoodRecyclerAdapter recyclerAdapter;
    List<FoodMoodFilter> foodMoodFilterList;
    TextView textView;
    FoodMoodFilter foodMoodFilter = null;


    public MoodFragment() {
        // Required empty public constructor
    }

    public static MoodFragment getInstance() {
        return new MoodFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mood, container, false);

        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        textView = (TextView) view.findViewById(R.id.filter_mood_no_item_text);

        recyclerView = (RecyclerView) view.findViewById(R.id.filter_mood_recycler_view);
        recyclerView.setHasFixedSize(true);

        setRecyclerAdapter();
    }

    private void setRecyclerAdapter() {
        if (getParentFragment() != null && getParentFragment() instanceof FiltersDialogFragment) {
            foodMoodFilterList = ((FiltersDialogFragment) getParentFragment()).foodMoodFilterList;
            if (foodMoodFilterList != null && foodMoodFilterList.size() > 0) {
                recyclerAdapter = new FilterMoodRecyclerAdapter(getActivity(), foodMoodFilterList, this);

                recyclerView.setVisibility(View.VISIBLE);
                recyclerView.setAdapter(recyclerAdapter);
                recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
            } else {
                textView.setVisibility(View.VISIBLE);
            }
        } else {
            textView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onItemClicked(int position, boolean isAnyItemSelected) {
        if (foodMoodFilterList != null && foodMoodFilterList.size() > 0) {

            this.foodMoodFilter = foodMoodFilterList.get(position);

            // Toggle the apply button depends on the selection
            if (getParentFragment() != null) {
                if (getParentFragment() instanceof FiltersDialogFragment) {
                    if (isAnyItemSelected)
                        ((FiltersDialogFragment) getParentFragment()).toggleApplyButton(true);
                    else
                        ((FiltersDialogFragment) getParentFragment()).toggleApplyButton(false);
                }
            }
        }
    }

    public FoodMoodFilter getSelectedItem() {
        return this.foodMoodFilter;
    }

    public void clearSelection() {
        setRecyclerAdapter();
        this.foodMoodFilter = null;
    }
}