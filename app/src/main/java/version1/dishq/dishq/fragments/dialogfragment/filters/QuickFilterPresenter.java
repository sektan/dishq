package version1.dishq.dishq.fragments.dialogfragment.filters;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import version1.dishq.dishq.fragments.dialogfragment.filters.models.searchfilters.Datum;
import version1.dishq.dishq.fragments.dialogfragment.filters.models.searchfilters.SearchFilter;
import version1.dishq.dishq.server.Config;
import version1.dishq.dishq.server.RestApi;
import version1.dishq.dishq.util.DishqApplication;
import version1.dishq.dishq.util.Util;

/**
 * Created by kavin.prabhu on 01/01/17.
 */

public class QuickFilterPresenter {

    private QuickFiltersFragment view;

    public QuickFilterPresenter(QuickFiltersFragment view) {
        this.view = view;
    }

    public void getSearchQueryResults(String searchKey, final SearchResultsCallback callback) {
        RestApi restApi = Config.createService(RestApi.class);

        Call<SearchFilter> call = restApi.getSearchFilters(searchKey, DishqApplication.getUniqueID(), DishqApplication.getUserID());
        call.enqueue(new Callback<SearchFilter>() {
            @Override
            public void onResponse(Call<SearchFilter> call, Response<SearchFilter> response) {
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
            public void onFailure(Call<SearchFilter> call, Throwable t) {
                callback.onFailure();
            }
        });
    }

    public interface SearchResultsCallback {
        void onSuccess(List<Datum> data);

        void onFailure();
    }
}
