package version1.dishq.dishq.fragments.dialogfragment.filters;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
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
import version1.dishq.dishq.util.DishqApplication;
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
    private boolean networkFailed;

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
        textView.setTypeface(Util.opensansregular);
        searchAutoCompleteText = (AutoCompleteTextView) view.findViewById(R.id.filter_quick_search_auto_complete);
        searchAutoCompleteText.setTypeface(Util.opensansregular);
        searchAutoCompleteText.setThreshold(0);
        searchAutoCompleteText.addTextChangedListener(this);
        searchAutoCompleteText.setOnItemClickListener(this);

        progressBar = (ProgressBar) view.findViewById(R.id.filter_quick_search_progress_bar);
        progressBar.getIndeterminateDrawable().setColorFilter(getActivity().getResources().getColor(R.color.rupeeGreen), PorterDuff.Mode.MULTIPLY);

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
                textView.setVisibility(View.GONE);
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
                ((FiltersDialogFragment) getParentFragment()).toggleResetButton(false);
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

            checkInternetConnection(searchString);
            setRecyclerAdapter();
            datum = null;
            recyclerView.setVisibility(View.INVISIBLE);
        } else {
            searchAutoCompleteText.clearFocus();
            if (progressBar != null && progressBar.isShown())
                progressBar.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
            textView.setVisibility(View.GONE);
        }
    }

    //Method to check if the internet is connected or not
    private void checkInternetConnection(String searchString) {
        SharedPreferences settings;
        final String PREFS_NAME = "MyPrefsFile";
        settings = getActivity().getSharedPreferences(PREFS_NAME, 0);

        if (settings.getBoolean("android_M", true)) {
            //the app is being launched for first time, do something
            Log.d("Comments", " android_M");
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // only for gingerbread and newer versions
                checkNetwork(searchString);
            } else {
                checkNetwork(searchString);
            }
            settings.edit().putBoolean("android_M", false).apply();
        } else {
            checkNetwork(searchString);
        }
    }

    //Check for internet
    private void checkNetwork(String searchString) {
        if (!Util.checkAndShowNetworkPopup(getActivity())) {
            //Check for version
            Log.d("", "Checking for GPS");
            presenter.getSearchQueryResults(searchString, this);

        } else {
            networkFailed = true;
        }
    }


    public void hideSoftKeyboard() {
        if (getActivity().getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
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
            textView.setVisibility(View.GONE);
            arrayAdapter = new ArrayAdapter<>(getActivity(), R.layout.simple_dropdown_food_search_item, stringData);
            searchAutoCompleteText.setAdapter(arrayAdapter);
        }else {
            textView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onFailure() {
        if (progressBar != null && progressBar.isShown()) {
            progressBar.setVisibility(View.INVISIBLE);
        } else {
            Toast.makeText(getActivity(), "Unable to fetch results", Toast.LENGTH_SHORT).show();
        }
        textView.setVisibility(View.GONE);
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
                    searchAutoCompleteText.clearFocus();
                    ((FiltersDialogFragment) getParentFragment()).toggleResetButton(true);
                    ((FiltersDialogFragment) getParentFragment()).toggleApplyButton(true, true);
                }
            }
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
                        searchAutoCompleteText.clearFocus();
                        ((FiltersDialogFragment) getParentFragment()).toggleResetButton(true);
                        ((FiltersDialogFragment) getParentFragment()).toggleApplyButton(true, true);
                    }
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
