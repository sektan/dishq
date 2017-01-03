package version1.dishq.dishq.server.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by kavin.prabhu on 01/01/17.
 */

public class FilterSearchResults {

    @SerializedName("data")
    @Expose
    private List<Datum> data = null;
    @SerializedName("response")
    @Expose
    private String response;

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

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

}
