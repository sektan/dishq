package version1.dishq.dishq.modals.lists;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by dishq on 26-12-2016.
 * Package name version1.dishq.dishq.
 */

public class FavCuisineSelect {

    @SerializedName("class_name")
    @Expose
    private String favClassName;

    @SerializedName("entity_id")
    @Expose
    private int favEntityId;

    public FavCuisineSelect(String favClassName, int favEntityId) {
        this.favClassName = favClassName;
        this.favEntityId = favEntityId;
    }

    public String getFavClassName() {
        return favClassName;
    }

    public void setFavClassName(String favClassName) {
        this.favClassName = favClassName;
    }

    public int getFavEntityId() {
        return favEntityId;
    }

    public void setFavEntityId(int favEntityId) {
        this.favEntityId = favEntityId;
    }
}
