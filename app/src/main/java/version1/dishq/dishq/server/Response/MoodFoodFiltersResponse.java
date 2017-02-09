package version1.dishq.dishq.server.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by dishq on 07-02-2017.
 * Package name version1.dishq.dishq.
 */

public class MoodFoodFiltersResponse {

    @SerializedName("data")
    @Expose
    public MoodFoodFiltersInfo moodFoodFiltersInfo;

    @SerializedName("response")
    @Expose
    private String moodFoodResponse;

    public class MoodFoodFiltersInfo {
        @SerializedName("quick_filters")
        @Expose
        private ArrayList<FoodFiltersInfo> foodFiltersInfos;

        @SerializedName("food_mood_filters")
        @Expose
        private ArrayList<MoodFiltersInfo> moodFiltersInfos;

        public ArrayList<FoodFiltersInfo> getFoodFiltersInfos() {
            return foodFiltersInfos;
        }

        public void setFoodFiltersInfos(ArrayList<FoodFiltersInfo> foodFiltersInfos) {
            this.foodFiltersInfos = foodFiltersInfos;
        }

        public ArrayList<MoodFiltersInfo> getMoodFiltersInfos() {
            return moodFiltersInfos;
        }

        public void setMoodFiltersInfos(ArrayList<MoodFiltersInfo> moodFiltersInfos) {
            this.moodFiltersInfos = moodFiltersInfos;
        }
    }

    public class FoodFiltersInfo {
        @SerializedName("name")
        @Expose
        private String foodFilterName;

        @SerializedName("entity_id")
        @Expose
        private int foodFilterEntityId;

        @SerializedName("class_name")
        @Expose
        private String foodFilterClassName;

        public String getFoodFilterName() {
            return foodFilterName;
        }

        public void setFoodFilterName(String foodFilterName) {
            this.foodFilterName = foodFilterName;
        }

        public int getFoodFilterEntityId() {
            return foodFilterEntityId;
        }

        public void setFoodFilterEntityId(int foodFilterEntityId) {
            this.foodFilterEntityId = foodFilterEntityId;
        }

        public String getFoodFilterClassName() {
            return foodFilterClassName;
        }

        public void setFoodFilterClassName(String foodFilterClassName) {
            this.foodFilterClassName = foodFilterClassName;
        }
    }

    public class MoodFiltersInfo {
        @SerializedName("name")
        @Expose
        private String moodFilterName;

        @SerializedName("food_mood_id")
        @Expose
        private int moodFilterId;

        public String getMoodFilterName() {
            return moodFilterName;
        }

        public void setMoodFilterName(String moodFilterName) {
            this.moodFilterName = moodFilterName;
        }

        public int getMoodFilterId() {
            return moodFilterId;
        }

        public void setMoodFilterId(int moodFilterId) {
            this.moodFilterId = moodFilterId;
        }
    }
}
