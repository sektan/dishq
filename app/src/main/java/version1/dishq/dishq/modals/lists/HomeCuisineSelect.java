package version1.dishq.dishq.modals.lists;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by dishq on 26-12-2016.
 * Package name version1.dishq.dishq.
 */

public class HomeCuisineSelect {

    @SerializedName("class_name")
    @Expose
    private String homeClassName;

    @SerializedName("entity_id")
    @Expose
    private int homeEntityId;

    public HomeCuisineSelect(String homeClassName, int homeEntityId) {
        this.homeClassName = homeClassName;
        this.homeEntityId = homeEntityId;
    }

    public String getHomeClassName() {
        return homeClassName;
    }

    public void setHomeClassName(String homeClassName) {
        this.homeClassName = homeClassName;
    }

    public int getHomeEntityId() {
        return homeEntityId;
    }

    public void setHomeEntityId(int homeEntityId) {
        this.homeEntityId = homeEntityId;
    }
}
