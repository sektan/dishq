package version1.dishq.dishq.server.Request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by dishq on 28-12-2016.
 * Package name version1.dishq.dishq.
 */

public class FavDishAddRemHelper {

    @SerializedName("uid")
    @Expose
    private String uid;

    @SerializedName("source")
    @Expose
    private String sourceOfScreen;

    @SerializedName("generic_dish_id")
    @Expose
    private int genericDishId;

    @SerializedName("favourite")
    @Expose
    private int fav;

    //Constructor for the class
    public FavDishAddRemHelper (String uid, String sourceOfScreen, int genericDishId, int fav) {
        this.uid = uid;
        this.sourceOfScreen = sourceOfScreen;
        this.genericDishId = genericDishId;
        this.fav = fav;
    }
}
