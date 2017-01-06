package version1.dishq.dishq.fragments.dialogfragment.filters.models.searchfilters;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by kavin.prabhu on 02/01/17.
 */

public class Datum {

    @SerializedName("class_name")
    @Expose
    private String className;
    @SerializedName("entity_id")
    @Expose
    private Integer entityId;
    @SerializedName("name")
    @Expose
    private String name;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Integer getEntityId() {
        return entityId;
    }

    public void setEntityId(Integer entityId) {
        this.entityId = entityId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;

    }
}
