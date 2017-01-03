package version1.dishq.dishq.fragments.dialogfragment.filters.models.quickfilters;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by kavin.prabhu on 31/12/16.
 */

public class FoodFilters {

    @SerializedName("data")
    @Expose
    private Data data;
    @SerializedName("response")
    @Expose
    private String response;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

}
