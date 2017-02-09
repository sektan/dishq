package version1.dishq.dishq.server.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by dishq on 07-02-2017.
 * Package name version1.dishq.dishq.
 */

public class FoodFilterSearchResponse {
    @SerializedName("data")
    @Expose
    private ArrayList<FoodFilterSearchInfo> foodFilterSearchInfos;

    @SerializedName("response")
    @Expose
    private String foodFilterSearchResponse;

    public ArrayList<FoodFilterSearchInfo> getFoodFilterSearchInfos() {
        return foodFilterSearchInfos;
    }

    public void setFoodFilterSearchInfos(ArrayList<FoodFilterSearchInfo> foodFilterSearchInfos) {
        this.foodFilterSearchInfos = foodFilterSearchInfos;
    }

    public class FoodFilterSearchInfo {
        @SerializedName("name")
        @Expose
        private String foodFilterSearchName;

        @SerializedName("entity_id")
        @Expose
        private int foodFilterSearchEntityId;

        @SerializedName("class_name")
        @Expose
        private String foodFilterSearchClassName;

        public String getFoodFilterSearchName() {
            return foodFilterSearchName;
        }

        public void setFoodFilterSearchName(String foodFilterSearchName) {
            this.foodFilterSearchName = foodFilterSearchName;
        }

        public int getFoodFilterSearchEntityId() {
            return foodFilterSearchEntityId;
        }

        public void setFoodFilterSearchEntityId(int foodFilterSearchEntityId) {
            this.foodFilterSearchEntityId = foodFilterSearchEntityId;
        }

        public String getFoodFilterSearchClassName() {
            return foodFilterSearchClassName;
        }

        public void setFoodFilterSearchClassName(String foodFilterSearchClassName) {
            this.foodFilterSearchClassName = foodFilterSearchClassName;
        }
    }
}
