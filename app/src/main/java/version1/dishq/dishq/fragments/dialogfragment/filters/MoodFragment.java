package version1.dishq.dishq.fragments.dialogfragment.filters;


import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import version1.dishq.dishq.OnClickCallbacks;
import version1.dishq.dishq.R;
import version1.dishq.dishq.adapters.FilterMoodRecyclerAdapter;
import version1.dishq.dishq.fragments.dialogfragment.filters.models.quickfilters.FoodMoodFilter;
import version1.dishq.dishq.util.Util;

/**
 * A simple {@link Fragment} subclass.
 */
public class MoodFragment extends Fragment implements OnClickCallbacks.OnClickMoodFilterItemCallback {

    RecyclerView recyclerView;
    FilterMoodRecyclerAdapter recyclerAdapter = null;
    List<FoodMoodFilter> foodMoodFilterList;
    TextView textView;
    ProgressBar progressBar;
    View view;

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
        Log.d("FilterFragment", "Mood fragment is created");
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        return view;
    }

    public FilterMoodRecyclerAdapter getRecyclerAdapter() {
        return recyclerAdapter;
    }

    public void setRecyclerAdapter(FilterMoodRecyclerAdapter recyclerAdapter) {
        this.recyclerAdapter = recyclerAdapter;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);



            textView = (TextView) view.findViewById(R.id.filter_mood_no_item_text);
            TextView feelingText = (TextView) view.findViewById(R.id.how_are_you_feeling);
            feelingText.setTypeface(Util.opensansregular);

            recyclerView = (RecyclerView) view.findViewById(R.id.filter_mood_recycler_view);
            recyclerView.setHasFixedSize(true);

            setRecyclerAdapter();
        }
    }

    private void setRecyclerAdapter() {
        if (getParentFragment() != null && getParentFragment() instanceof FiltersDialogFragment) {
            foodMoodFilterList = ((FiltersDialogFragment) getParentFragment()).foodMoodFilterList;
            if (foodMoodFilterList != null && foodMoodFilterList.size() > 0) {
                if (recyclerAdapter == null) {
                    recyclerAdapter = new FilterMoodRecyclerAdapter(getActivity(), foodMoodFilterList, this);
                }
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

            // Toggle the apply button depends on the selection
            if (getParentFragment() != null) {
                if (getParentFragment() instanceof FiltersDialogFragment) {
                    if (isAnyItemSelected) {
                        ((FiltersDialogFragment) getParentFragment()).toggleResetButton(true);
                        ((FiltersDialogFragment) getParentFragment()).toggleApplyButton(true, true);
                    }else {
                        if(FiltersDialogFragment.getInstance().getQuickFiltersFragment()== null || FiltersDialogFragment.getInstance().getQuickFiltersFragment().getSelectedItem()==null) {
                            ((FiltersDialogFragment) getParentFragment()).toggleResetButton(false);
                            ((FiltersDialogFragment) getParentFragment()).toggleApplyButton(false, true);
                        }else {
                            ((FiltersDialogFragment) getParentFragment()).toggleResetButton(true);
                            ((FiltersDialogFragment) getParentFragment()).toggleApplyButton(true, true);
                        }
                    }
                }
            }
        }
    }

    public List<FoodMoodFilter> getFoodMoodFilterList() {
        return foodMoodFilterList;
    }

    public void setFoodMoodFilterList(List<FoodMoodFilter> foodMoodFilterList) {
        this.foodMoodFilterList = foodMoodFilterList;
    }

    public void clearSelection(boolean isBeingShown) {
        if (this.recyclerAdapter != null) {
            this.recyclerAdapter.clearSelection();
        }

        if (isBeingShown) {
            setRecyclerAdapter();
        }
    }
}
