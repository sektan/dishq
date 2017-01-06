package version1.dishq.dishq.server.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by dishq on 06-01-2017.
 * Package name version1.dishq.dishq.
 */

public class FavouriteDishesResponse {

    @SerializedName("data")
    @Expose
    private ArrayList<FavouriteDishesInfo> favouriteDishesInfos;

    @SerializedName("response")
    @Expose
    private String favDishesResponse;

    private class FavouriteDishesInfo {
        
    }
}
