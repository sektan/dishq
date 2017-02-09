package version1.dishq.dishq.fragments.moodFoodFiltersFragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import version1.dishq.dishq.R;
import version1.dishq.dishq.adapters.filtersAdapters.FoodFilterRecyclerAdapter;
import version1.dishq.dishq.custom.OnClickFilterCallbacks;
import version1.dishq.dishq.server.Config;
import version1.dishq.dishq.server.Response.FoodFilterSearchResponse;
import version1.dishq.dishq.server.RestApi;
import version1.dishq.dishq.util.DishqApplication;
import version1.dishq.dishq.util.Util;

/**
 * Created by dishq on 07-02-2017.
 * Package name version1.dishq.dishq.
 */

public class FoodFilterFragment extends Fragment implements
        OnClickFilterCallbacks.OnClickFoodFilterItemCallback, AdapterView.OnItemClickListener {

    private static final String TAG = "FoodFilterFragment";
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    AutoCompleteTextView searchAutoCompleteText;
    ArrayAdapter arrayAdapter;
    private TextView textView;
    private String str = "";
    private String myStr = "";
    FoodFilterRecyclerAdapter recyclerAdapter = null;
    private Boolean selectionMade = false;

    public Boolean getSelectionMade() {
        return selectionMade;
    }

    public void setSelectionMade(Boolean selectionMade) {
        this.selectionMade = selectionMade;
    }

    public FoodFilterFragment() {
        // Required empty public constructor
    }

    public static FoodFilterFragment getInstance() {
        return new FoodFilterFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_food_filter, container, false);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressBar = (ProgressBar) view.findViewById(R.id.filter_progress_bar);
        progressBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(getActivity(), R.color.rupeeGreen), PorterDuff.Mode.MULTIPLY);

        textView = (TextView) view.findViewById(R.id.filter_quick_no_item_text);
        textView.setTypeface(Util.opensansregular);
        textView.setVisibility(View.GONE);

        searchAutoCompleteText = (AutoCompleteTextView) view.findViewById(R.id.food_search);
        searchAutoCompleteText.setTypeface(Util.opensansregular);
        searchAutoCompleteText.setThreshold(1);
        if (Util.getItemSetFromSearch()) {
            searchAutoCompleteText.setText(Util.getFilterName());
            searchAutoCompleteText.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.menufinder_search, 0, R.drawable.menufinder_cross, 0);

        }
        searchAutoCompleteText.setOnItemClickListener(this);
        recyclerView = (RecyclerView) view.findViewById(R.id.food_filter_recyclerview);
        recyclerView.setHasFixedSize(true);

        setRecyclerAdapter();
        setFunctionality();
    }

    private void setRecyclerAdapter() {
        if (recyclerAdapter == null) {
            recyclerAdapter = new FoodFilterRecyclerAdapter(getActivity(), this);
        }
        if (recyclerView != null) {
            recyclerView.setVisibility(View.VISIBLE);
            if (progressBar != null && progressBar.isShown())
                progressBar.setVisibility(View.GONE);
            recyclerView.setAdapter(recyclerAdapter);
            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        }
    }

    protected void setFunctionality() {
        searchAutoCompleteText.setInputType(InputType.TYPE_CLASS_TEXT);
        searchAutoCompleteText.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                searchAutoCompleteText.requestFocus();
                setSelectionMade(false);
                final int DRAWABLE_RIGHT = 2;
                if (searchAutoCompleteText.getText().length() > 1) {
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        if (event.getRawX() >= (searchAutoCompleteText.getRight() - searchAutoCompleteText.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                            // your action here
                            searchAutoCompleteText.setText("");
                            Util.menuFinderRestInfos.clear();
                            return true;
                        }
                    }
                }
                return false;
            }
        });

        searchAutoCompleteText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                str = s.toString();
                if (count == 0) {
                    setSelectionMade(false);
                }

                if (s.length() > 1) {
                    if (!getSelectionMade() && !str.equals(((MoodFoodDialogFragment) getParentFragment()).getFilterName())) {
                        if (progressBar != null && !progressBar.isShown())
                            progressBar.setVisibility(View.VISIBLE);
                        searchAutoCompleteText.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.menufinder_search, 0, R.drawable.menufinder_cross, 0);
                        checkInternetConnection(str);
                    }else {
                        recyclerView.setVisibility(View.VISIBLE);
                    }
                } else {
                    searchAutoCompleteText.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.menufinder_search, 0, 0, 0);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                searchAutoCompleteText.clearFocus();
            }
        });
    }


    //Method to check if the internet is connected or not
    private void checkInternetConnection(String query) {
        SharedPreferences settings;
        final String PREFS_NAME = "MyPrefsFile";
        settings = getActivity().getSharedPreferences(PREFS_NAME, 0);

        if (settings.getBoolean("android_M", true)) {
            //the app is being launched for first time, do something
            Log.d("Comments", " android_M");
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // only for gingerbread and newer versions
                checkNetwork(query);
            } else {
                checkNetwork(query);
            }
            settings.edit().putBoolean("android_M", false).apply();
        } else {
            checkNetwork(query);
        }
    }

    //Check for internet
    private void checkNetwork(String query) {
        if (!Util.checkAndShowNetworkPopup(getActivity())) {
            //Check for version
            Log.d(TAG, "Checking for GPS");
            if (!getSelectionMade()) {
                fetchFilters(query);
            }
        }
    }

    public static void closeKeyboard(Context c, IBinder windowToken) {
        InputMethodManager mgr = (InputMethodManager) c.getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(windowToken, 0);
    }


    public void fetchFilters(String query) {
        RestApi restApi = Config.createService(RestApi.class);
        Call<FoodFilterSearchResponse> call = restApi.getFoodFilterSuggestion(query, DishqApplication.getUniqueID(),
                DishqApplication.getUserID());
        call.enqueue(new Callback<FoodFilterSearchResponse>() {
            @Override
            public void onResponse(Call<FoodFilterSearchResponse> call, Response<FoodFilterSearchResponse> response) {
                Log.d(TAG, "Success");
                try {
                    if (response.isSuccessful()) {
                        ArrayList<FoodFilterSearchResponse.FoodFilterSearchInfo> body = response.body().getFoodFilterSearchInfos();
                        if (body != null) {
                            if(body.size() == 0) {
                                textView.setVisibility(View.VISIBLE);
                            }else {
                                textView.setVisibility(View.GONE);
                            }
                            Util.foodFilterSearchInfos.clear();
                            List<String> stringData = new ArrayList<>();
                            for (int i = 0; i < body.size(); i++) {
                                Util.foodFilterSearchInfos = body;
                                stringData.add(Util.foodFilterSearchInfos.get(i).getFoodFilterSearchName());

                            }
                            recyclerView.setVisibility(View.GONE);
                            if (progressBar != null && progressBar.isShown())
                                progressBar.setVisibility(View.INVISIBLE);
                            arrayAdapter = new ArrayAdapter<>(DishqApplication.getContext(), R.layout.simple_dropdown_food_search_item, stringData);
                            searchAutoCompleteText.setAdapter(arrayAdapter);
                            if(!getSelectionMade()) {
                                searchAutoCompleteText.showDropDown();
                            }
                        }
                    } else {
                        if (progressBar != null && progressBar.isShown())
                            progressBar.setVisibility(View.INVISIBLE);
                        textView.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                        String error = response.errorBody().string();
                        Log.d(TAG, "Error: " + error);
                    }
                } catch (IOException e) {
                    if (progressBar != null && progressBar.isShown())
                        progressBar.setVisibility(View.INVISIBLE);
                    textView.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<FoodFilterSearchResponse> call, Throwable t) {
                Log.d(TAG, "Failure");
                if (progressBar != null && progressBar.isShown())
                    progressBar.setVisibility(View.INVISIBLE);
                textView.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                if (t instanceof SocketTimeoutException) {
                    Log.e("timeout", "timeout" + t.getMessage());

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("Your Internet connection is slow. Please find a better connection.").setCancelable(false)
                            .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    fetchFilters(str);
                                }
                            });
                }
            }
        });
    }

    /**
     * Method for setting the UI when an item from the listView is clicked
     *
     * @param adapterView is the adapter being showing
     * @param view        the view showing the UI
     * @param i           integer parameter for the position of the item
     * @param l           long parameter
     */
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        setSelectionMade(true);
        this.recyclerAdapter.clearSelection();
        searchAutoCompleteText.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.menufinder_search, 0, R.drawable.menufinder_cross, 0);
        searchAutoCompleteText.clearFocus();
        recyclerView.setVisibility(View.VISIBLE);
        closeKeyboard(getActivity(), searchAutoCompleteText.getWindowToken());
        if (getParentFragment() != null) {
            if (getParentFragment() instanceof MoodFoodDialogFragment) {
                ((MoodFoodDialogFragment) getParentFragment()).setFilterName(Util.foodFilterSearchInfos.get(i).getFoodFilterSearchName());
                ((MoodFoodDialogFragment) getParentFragment()).setFilterClassName(Util.foodFilterSearchInfos.get(i).getFoodFilterSearchClassName());
                ((MoodFoodDialogFragment) getParentFragment()).setFilterEntityId(Util.foodFilterSearchInfos.get(i).getFoodFilterSearchEntityId());
                ((MoodFoodDialogFragment) getParentFragment()).setQuickFilterPosition(-1);
                ((MoodFoodDialogFragment) getParentFragment()).setSearchFoodItemSelected(true);
                ((MoodFoodDialogFragment) getParentFragment()).toggleApplyButton(true);
                ((MoodFoodDialogFragment) getParentFragment()).toggleResetButton(true);
            }
        }
    }
    /**********************************************************************************************/

    /**
     * Method for displaying the UI when an item from the recyclerView is clicked
     *
     * @param position          is the position of the item in the recyclerView
     * @param isAnyItemSelected is a boolean which lets you know if an item has been selected or de-selected
     */
    @Override
    public void onItemClicked(int position, boolean isAnyItemSelected) {
        if (getParentFragment() != null) {
            if (getParentFragment() instanceof MoodFoodDialogFragment) {
                if (isAnyItemSelected) {
                    ((MoodFoodDialogFragment) getParentFragment()).setSearchFoodItemSelected(false);
                    ((MoodFoodDialogFragment) getParentFragment()).setQuickFilterPosition(recyclerAdapter.getSelectedPos());
                    ((MoodFoodDialogFragment) getParentFragment()).setFilterName(Util.foodFiltersInfos.get(position).getFoodFilterName());
                    ((MoodFoodDialogFragment) getParentFragment()).setFilterClassName(Util.foodFiltersInfos.get(position).getFoodFilterClassName());
                    ((MoodFoodDialogFragment) getParentFragment()).setFilterEntityId(Util.foodFiltersInfos.get(position).getFoodFilterEntityId());
                    ((MoodFoodDialogFragment) getParentFragment()).setFoodItemSelected(true);
                    ((MoodFoodDialogFragment) getParentFragment()).toggleApplyButton(true);
                    ((MoodFoodDialogFragment) getParentFragment()).toggleResetButton(true);
                } else if (((MoodFoodDialogFragment) getParentFragment()).getMoodItemSelected()) {
                    ((MoodFoodDialogFragment) getParentFragment()).setFilterName("");
                    ((MoodFoodDialogFragment) getParentFragment()).setFilterClassName("");
                    ((MoodFoodDialogFragment) getParentFragment()).setFilterEntityId(-1);
                    ((MoodFoodDialogFragment) getParentFragment()).setQuickFilterPosition(-1);
                    ((MoodFoodDialogFragment) getParentFragment()).toggleApplyButton(true);
                    ((MoodFoodDialogFragment) getParentFragment()).toggleResetButton(true);
                    ((MoodFoodDialogFragment) getParentFragment()).setFoodItemSelected(false);
                } else if (((MoodFoodDialogFragment) getParentFragment()).getSearchFoodItemSelected()) {
                    ((MoodFoodDialogFragment) getParentFragment()).setFoodItemSelected(false);
                    ((MoodFoodDialogFragment) getParentFragment()).setQuickFilterPosition(-1);
                    ((MoodFoodDialogFragment) getParentFragment()).toggleApplyButton(true);
                    ((MoodFoodDialogFragment) getParentFragment()).toggleResetButton(true);
                } else {
                    ((MoodFoodDialogFragment) getParentFragment()).setFoodItemSelected(false);
                    ((MoodFoodDialogFragment) getParentFragment()).toggleApplyButton(false);
                    if (!Util.getMoodName().equals("")) {
                        ((MoodFoodDialogFragment) getParentFragment()).toggleResetButton(true);
                    } else if (!Util.getFilterName().equals("")) {
                        ((MoodFoodDialogFragment) getParentFragment()).toggleResetButton(true);
                    } else {
                        ((MoodFoodDialogFragment) getParentFragment()).toggleResetButton(false);
                    }

                    ((MoodFoodDialogFragment) getParentFragment()).setQuickFilterPosition(-1);
                    ((MoodFoodDialogFragment) getParentFragment()).setFilterName("");
                    ((MoodFoodDialogFragment) getParentFragment()).setFilterClassName("");
                    ((MoodFoodDialogFragment) getParentFragment()).setFilterEntityId(-1);
                }
            }
        }
    }

    /*********************************************************************************************************/

    public void clearSelection(boolean isBeingShown) {
        if (this.recyclerAdapter != null) {
            this.recyclerAdapter.clearSelection();
            Util.foodFilterSearchInfos.clear();
        }
        if (isBeingShown) {
            setRecyclerAdapter();
            if (searchAutoCompleteText != null) {
                searchAutoCompleteText.setText("");
            }
        }
    }
}
