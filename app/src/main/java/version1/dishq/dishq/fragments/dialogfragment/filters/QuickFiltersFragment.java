package version1.dishq.dishq.fragments.dialogfragment.filters;


import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mixpanel.android.mpmetrics.MixpanelAPI;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import version1.dishq.dishq.OnClickCallbacks;
import version1.dishq.dishq.R;
import version1.dishq.dishq.adapters.QuickFiltersRecyclerAdapter;
import version1.dishq.dishq.fragments.dialogfragment.filters.models.quickfilters.QuickFilter;
import version1.dishq.dishq.fragments.dialogfragment.filters.models.searchfilters.Datum;
import version1.dishq.dishq.util.Util;

/**
 * A simple {@link Fragment} subclass.
 */
public class QuickFiltersFragment extends Fragment implements
        OnClickCallbacks.OnClickQuickFilterItemCallback, TextWatcher,
        QuickFilterPresenter.SearchResultsCallback, AdapterView.OnItemClickListener {

    QuickFiltersRecyclerAdapter recyclerAdapter = null;
    List<QuickFilter> quickFilterList;
    List<Datum> datumList = new ArrayList<>();
    QuickFilterPresenter presenter;
    ArrayAdapter arrayAdapter;
    Datum datum = null;
    QuickFilter quickFilter = null;

    TextView textView;
    AutoCompleteTextView searchAutoCompleteText;
    ProgressBar progressBar;
    RecyclerView recyclerView;
    private MixpanelAPI mixpanel = null;

    public QuickFiltersFragment() {
        // Required empty public constructor
    }

    public static QuickFiltersFragment getInstance() {
        return new QuickFiltersFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //MixPanel Instantiation
        mixpanel = MixpanelAPI.getInstance(getActivity(), getResources().getString(R.string.mixpanel_token));
        presenter = new QuickFilterPresenter(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_filters, container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        textView = (TextView) view.findViewById(R.id.filter_quick_no_item_text);
        searchAutoCompleteText = (AutoCompleteTextView) view.findViewById(R.id.filter_quick_search_auto_complete);
        searchAutoCompleteText.setTypeface(Util.opensansregular);
        searchAutoCompleteText.setThreshold(1);
        searchAutoCompleteText.addTextChangedListener(this);
        searchAutoCompleteText.setOnItemClickListener(this);

        progressBar = (ProgressBar) view.findViewById(R.id.filter_quick_search_progress_bar);
        progressBar.getIndeterminateDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);

        recyclerView = (RecyclerView) view.findViewById(R.id.filter_quick_recycler_view);
        recyclerView.setHasFixedSize(true);

        setRecyclerAdapter();
    }

    private void setRecyclerAdapter() {
        if (getParentFragment() != null && getParentFragment() instanceof FiltersDialogFragment) {
            quickFilterList = ((FiltersDialogFragment) getParentFragment()).quickFilterList;
            if (quickFilterList != null && quickFilterList.size() > 0) {
                if (recyclerAdapter == null) {
                    recyclerAdapter = new QuickFiltersRecyclerAdapter(getActivity(), quickFilterList, this);
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
        if (quickFilterList != null && quickFilterList.size() > 0) {
            this.quickFilter = quickFilterList.get(position);
            // Toggle the apply button depends on the selection
            if (getParentFragment() != null) {
                if (getParentFragment() instanceof FiltersDialogFragment) {
                    if (isAnyItemSelected) {
                        ((FiltersDialogFragment) getParentFragment()).toggleApplyButton(true, true);
                    }
                }
            }
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        try {
            final JSONObject properties = new JSONObject();
            properties.put("Mood filter search", "moodfilters");
            mixpanel.track("Mood filter search", properties);
        } catch (final JSONException e) {
            throw new RuntimeException("Could not encode hour of the day in JSON");
        }
        if (getParentFragment() != null) {
            if (getParentFragment() instanceof FiltersDialogFragment) {
                ((FiltersDialogFragment) getParentFragment()).toggleApplyButton(false, false);
            }
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        String searchString = s.toString();
        if (searchString.length() > 0) {
            if (progressBar != null && !progressBar.isShown())
                progressBar.setVisibility(View.VISIBLE);

            presenter.getSearchQueryResults(searchString, this);

            setRecyclerAdapter();
            datum = null;
            recyclerView.setVisibility(View.INVISIBLE);
        } else {
            if (progressBar != null && progressBar.isShown())
                progressBar.setVisibility(View.INVISIBLE);

            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    public QuickFiltersRecyclerAdapter getRecyclerAdapter() {
        return recyclerAdapter;
    }

    public void setRecyclerAdapter(QuickFiltersRecyclerAdapter recyclerAdapter) {
        this.recyclerAdapter = recyclerAdapter;
    }


    @Override
    public void onSuccess(List<Datum> data) {
        if (progressBar != null && progressBar.isShown())
            progressBar.setVisibility(View.INVISIBLE);

        datumList.clear();
        datumList.addAll(data);

        List<String> stringData = new ArrayList<>();
        if (data.size() > 0) {
            for (Datum datum : data)
                stringData.add(datum.getName());

            arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_dropdown_item_1line, stringData);
            searchAutoCompleteText.setAdapter(arrayAdapter);
        }
    }

    @Override
    public void onFailure() {
        if (progressBar != null && progressBar.isShown()) {
            progressBar.setVisibility(View.INVISIBLE);
        } else {
            Toast.makeText(getActivity(), "Unable to fetch results", Toast.LENGTH_SHORT).show();
        }

        recyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (datumList != null && datumList.size() > 0) {
            this.datum = datumList.get(position);
            this.recyclerAdapter.clearSelection();
            // Toggle the apply button depends on the selection
            if (getParentFragment() != null) {
                if (getParentFragment() instanceof FiltersDialogFragment) {
                    ((FiltersDialogFragment) getParentFragment()).toggleApplyButton(true, true);
                }
            }
        }
    }

    public Object getSelectedItem() {
        return datum != null ? datum : quickFilter;
    }

    public String getSelectedFilterName() {
        Object selectedItem = this.getSelectedItem();

        if (selectedItem instanceof Datum) {
            return ((Datum) selectedItem).getName();
        } else if (selectedItem instanceof QuickFilter) {
            return ((QuickFilter) selectedItem).getName();
        }
        return null;
    }

    public String getSelectedFilterClassName() {
        Object selectedItem = this.getSelectedItem();

        if (selectedItem instanceof Datum) {
            return ((Datum) selectedItem).getClassName();
        } else if (selectedItem instanceof QuickFilter) {
            return ((QuickFilter) selectedItem).getClassName();
        }
        return null;
    }

    public int getSelectedFilterEntityId() {
        Object selectedItem = this.getSelectedItem();

        if (selectedItem instanceof Datum) {
            return ((Datum) selectedItem).getEntityId();
        } else if (selectedItem instanceof QuickFilter) {
            return ((QuickFilter) selectedItem).getEntityId();
        }
        return -1;
    }

    public void clearSelection(boolean isBeingShown) {
        if (this.recyclerAdapter != null) {
            this.recyclerAdapter.clearSelection();
        }
        if (isBeingShown) {
            setRecyclerAdapter();
            searchAutoCompleteText.clearListSelection();
            searchAutoCompleteText.setText(null);
        }
    }

    @Override
    public void onDestroy() {
        mixpanel.flush();
        super.onDestroy();
    }
}
