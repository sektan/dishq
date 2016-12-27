package version1.dishq.dishq.modals.lists;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by dishq on 26-12-2016.
 * Package name version1.dishq.dishq.
 */

public class DontEatSelect {
    @SerializedName("class_name")
    @Expose
    private String deClassName;

    @SerializedName("entity_id")
    @Expose
    private int deEntityId;

    public DontEatSelect(String deClassName, int deEntityId) {
        this.deClassName = deClassName;
        this.deEntityId = deEntityId;
    }

    public String getDeClassName() {
        return deClassName;
    }

    public void setDeClassName(String deClassName) {
        this.deClassName = deClassName;
    }

    public int getDeEntityId() {
        return deEntityId;
    }

    public void setDeEntityId(int deEntityId) {
        this.deEntityId = deEntityId;
    }
}
