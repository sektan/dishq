package version1.dishq.dishq.fragments.dialogfragment.filters;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import version1.dishq.dishq.fragments.dialogfragment.filters.models.quickfilters.Data;
import version1.dishq.dishq.fragments.dialogfragment.filters.models.quickfilters.FoodFilters;
import version1.dishq.dishq.server.Config;
import version1.dishq.dishq.server.RestApi;

/**
 * Created by kavin.prabhu on 31/12/16.
 */

public class FilterPresenter {

    private FiltersDialogFragment view;

    public FilterPresenter(FiltersDialogFragment view) {
        this.view = view;
    }

    public void getFilterResults(final FilterResultsCallback callback) {
        RestApi restApi = Config.createService(RestApi.class);
        // TODO Change the authorization
        Call<FoodFilters> call = restApi.getFoodFilters("Bearer wgdM8XnsWRzgcLSXhMAdzqhFtZOUYI");
        call.enqueue(new Callback<FoodFilters>() {
            @Override
            public void onResponse(Call<FoodFilters> call, Response<FoodFilters> response) {
                if (response != null) {
                    if (response.isSuccessful()) {
                        if (response.body() != null && response.body().getResponse().equalsIgnoreCase("Success")) {
                            callback.onSuccess(response.body().getData());
                        } else {
                            callback.onFailure();
                        }
                    } else {
                        callback.onFailure();
                    }
                } else {
                    callback.onFailure();
                }
            }

            @Override
            public void onFailure(Call<FoodFilters> call, Throwable t) {
                callback.onFailure();
            }
        });
    }

    interface FilterResultsCallback {
        void onSuccess(Data data);

        void onFailure();
    }
}
