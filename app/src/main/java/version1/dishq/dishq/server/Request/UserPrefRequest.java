package version1.dishq.dishq.server.Request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import version1.dishq.dishq.modals.lists.DontEatSelect;
import version1.dishq.dishq.modals.lists.FavCuisineSelect;
import version1.dishq.dishq.modals.lists.HomeCuisineSelect;

/**
 * Created by dishq on 26-12-2016.
 * Package name version1.dishq.dishq.
 */

public class UserPrefRequest {
    @SerializedName("food_choice")
    @Expose
    private int foodChoiceId;

    @SerializedName("home_cuisines")
    @Expose
    public ArrayList<HomeCuisineSelect> homeCuisineSelects;

    @SerializedName("favourite_cuisines")
    @Expose
    public ArrayList<FavCuisineSelect> favCuisineSelects;

    @SerializedName("dont_eats")
    @Expose
    public ArrayList<DontEatSelect> dontEatSelects;

    public UserPrefRequest(int foodChoiceId, ArrayList<HomeCuisineSelect>homeCuisineSelects, ArrayList<FavCuisineSelect> favCuisineSelects,
                           ArrayList<DontEatSelect>dontEatSelects) {
        this.foodChoiceId = foodChoiceId;
        this.homeCuisineSelects = homeCuisineSelects;
        this.favCuisineSelects = favCuisineSelects;
        this.dontEatSelects = dontEatSelects;
    }
}
