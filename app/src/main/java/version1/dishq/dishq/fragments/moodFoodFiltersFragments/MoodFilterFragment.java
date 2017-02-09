package version1.dishq.dishq.fragments.moodFoodFiltersFragments;

import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import version1.dishq.dishq.R;
import version1.dishq.dishq.adapters.filtersAdapters.MoodFilterRecyclerAdapter;
import version1.dishq.dishq.custom.OnClickFilterCallbacks;
import version1.dishq.dishq.util.Util;

/**
 * Created by dishq on 07-02-2017.
 * Package name version1.dishq.dishq.
 */

public class MoodFilterFragment extends Fragment implements
        OnClickFilterCallbacks.OnClickMoodFilterItemCallback {

    private static final String TAG = "MoodFilterFragment";
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private MoodFilterRecyclerAdapter recyclerAdapter = null;

    public MoodFilterFragment() {
        // Required empty public constructor
    }

    public static MoodFilterFragment getInstance() {
        return new MoodFilterFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Util.hideKeyboard(getActivity());
        return inflater.inflate(R.layout.fragment_mood_filter, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Util.hideKeyboard(getActivity());
        // Initially the progressbar visibility is set GONE in XML
        progressBar = (ProgressBar) view.findViewById(R.id.filter_progress_bar);
        progressBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(getActivity(), R.color.rupeeGreen), PorterDuff.Mode.MULTIPLY);

        TextView feelingText = (TextView) view.findViewById(R.id.how_are_you_feeling);
        feelingText.setTypeface(Util.opensansregular);

        recyclerView = (RecyclerView) view.findViewById(R.id.filter_mood_recycler_view);
        recyclerView.setHasFixedSize(true);

        setRecyclerAdapter();
    }

    public static void closeKeyboard(Context c, IBinder windowToken) {
        InputMethodManager mgr = (InputMethodManager) c.getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(windowToken, 0);
    }

    private void setRecyclerAdapter() {
        if (recyclerAdapter == null) {
            recyclerAdapter = new MoodFilterRecyclerAdapter(getActivity(), this);
        }
        if (recyclerView != null) {
            recyclerView.setVisibility(View.VISIBLE);
            if (progressBar != null && progressBar.isShown())
                progressBar.setVisibility(View.GONE);
            recyclerView.setAdapter(recyclerAdapter);
            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        }
    }

    public void clearSelection(boolean isBeingShown) {
        if (this.recyclerAdapter != null) {
            this.recyclerAdapter.clearSelection();
        }

        if (isBeingShown) {
            setRecyclerAdapter();
        }
    }

    @Override
    public void onItemClicked(int position, boolean isAnyItemSelected) {

        if (getParentFragment() != null) {
            if (getParentFragment() instanceof MoodFoodDialogFragment) {
                if (isAnyItemSelected) {

                    ((MoodFoodDialogFragment) getParentFragment()).setMoodPosition(recyclerAdapter.getSelectedPos());
                    ((MoodFoodDialogFragment) getParentFragment()).setMoodFilterId(Util.moodFiltersInfos.get(position).getMoodFilterId());
                    ((MoodFoodDialogFragment) getParentFragment()).setMoodName(Util.moodFiltersInfos.get(position).getMoodFilterName());
                    ((MoodFoodDialogFragment) getParentFragment()).setMoodItemSelected(true);
                    ((MoodFoodDialogFragment) getParentFragment()).toggleApplyButton(true);
                    ((MoodFoodDialogFragment) getParentFragment()).toggleResetButton(true);
                } else if (((MoodFoodDialogFragment) getParentFragment()).getFoodItemSelected()) {
                    ((MoodFoodDialogFragment) getParentFragment()).toggleApplyButton(true);
                    ((MoodFoodDialogFragment) getParentFragment()).toggleResetButton(true);
                    ((MoodFoodDialogFragment) getParentFragment()).setMoodItemSelected(false);
                    ((MoodFoodDialogFragment) getParentFragment()).setMoodPosition(-1);
                    ((MoodFoodDialogFragment) getParentFragment()).setMoodFilterId(-1);
                    ((MoodFoodDialogFragment) getParentFragment()).setMoodName("");
                } else if (((MoodFoodDialogFragment) getParentFragment()).getSearchFoodItemSelected()) {
                    ((MoodFoodDialogFragment) getParentFragment()).toggleApplyButton(true);
                    ((MoodFoodDialogFragment) getParentFragment()).toggleResetButton(true);
                    ((MoodFoodDialogFragment) getParentFragment()).setMoodItemSelected(false);
                    ((MoodFoodDialogFragment) getParentFragment()).setMoodPosition(-1);
                    ((MoodFoodDialogFragment) getParentFragment()).setMoodFilterId(-1);
                    ((MoodFoodDialogFragment) getParentFragment()).setMoodName("");
                } else {
                    if (!Util.getMoodName().equals("")) {
                        ((MoodFoodDialogFragment) getParentFragment()).toggleResetButton(true);
                    } else if (!Util.getFilterName().equals("")) {
                        ((MoodFoodDialogFragment) getParentFragment()).toggleResetButton(true);
                    } else {
                        ((MoodFoodDialogFragment) getParentFragment()).toggleResetButton(false);
                    }
                    ((MoodFoodDialogFragment) getParentFragment()).toggleApplyButton(false);
                    ((MoodFoodDialogFragment) getParentFragment()).setMoodItemSelected(false);
                    ((MoodFoodDialogFragment) getParentFragment()).setMoodPosition(-1);
                    ((MoodFoodDialogFragment) getParentFragment()).setMoodFilterId(-1);
                    ((MoodFoodDialogFragment) getParentFragment()).setMoodName("");
                }
            }
        }
    }
}
