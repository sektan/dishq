package version1.dishq.dishq.server.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by dishq on 15-12-2016.
 * Package name version1.dishq.dishq.
 */

public class TastePrefData {

    @SerializedName("data")
    @Expose
    public FoodPreferencesInfo foodPreferencesinfo;

    @SerializedName("response")
    @Expose
    private String foodPrefResponse;

    public class FoodPreferencesInfo {

        @SerializedName("allergies")
        @Expose
        public ArrayList<FoodAllergiesInfo> foodAllergiesInfos;

        @SerializedName("favourite_cuisine_choices")
        @Expose
        public ArrayList<FavCuisineInfo> favCuisineInfos;

        @SerializedName("home_cuisine_choices")
        @Expose
        public ArrayList<HomeCuisineInfo> homeCuisineInfos;

        @SerializedName("food_choices")
        @Expose
        public ArrayList<FoodChoicesInfo> foodChoicesInfos;
    }

    public class FoodAllergiesInfo {

        @SerializedName("class_name")
        @Expose
        private String allergyClassName;

        @SerializedName("currently_select")
        @Expose
        private Boolean allergyCurrentlySelect;

        @SerializedName("entity_id")
        @Expose
        private int allergyEntityId;

        @SerializedName("name")
        @Expose
        private String allergyName;

        @SerializedName("food_choice")
        @Expose
        private int allergyFoodChoice;

        public String getAllergyClassName() {
            return allergyClassName;
        }

        public void setAllergyClassName(String allergyClassName) {
            this.allergyClassName = allergyClassName;
        }

        public Boolean getAllergyCurrentlySelect() {
            return allergyCurrentlySelect;
        }

        public void setAllergyCurrentlySelect(Boolean allergyCurrentlySelect) {
            this.allergyCurrentlySelect = allergyCurrentlySelect;
        }
        public int getAllergyEntityId() {
            return allergyEntityId;
        }

        public void setAllergyEntityId(int allergyEntityId) {
            this.allergyEntityId = allergyEntityId;
        }

        public String getAllergyName() {
            return allergyName;
        }

        public int getAllergyFoodChoice() {
            return allergyFoodChoice;
        }

        public void setAllergyFoodChoice(int allergyFoodChoice) {
            this.allergyFoodChoice = allergyFoodChoice;
        }

    }

    public class FavCuisineInfo {

        @SerializedName("class_name")
        @Expose
        private String favCuisClassName;

        @SerializedName("currently_select")
        @Expose
        private Boolean favCuisCurrentSelect;

        @SerializedName("entity_id")
        @Expose
        private int favCuisEntityId;

        @SerializedName("name")
        @Expose
        private String favCuisName;

        public String getFavCuisClassName() {
            return favCuisClassName;
        }

        public void setFavCuisClassName(String favCuisClassName) {
            this.favCuisClassName = favCuisClassName;
        }

        public Boolean getFavCuisCurrentSelect() {
            return favCuisCurrentSelect;
        }

        public void setFavCuisCurrentSelect(Boolean favCuisCurrentSelect) {
            this.favCuisCurrentSelect = favCuisCurrentSelect;
        }

        public int getFavCuisEntityId() {
            return favCuisEntityId;
        }

        public void setFavCuisEntityId(int favCuisEntityId) {
            this.favCuisEntityId = favCuisEntityId;
        }

        public String getFavCuisName() {
            return favCuisName;
        }

        public void setFavCuisName(String favCuisName) {
            this.favCuisName = favCuisName;
        }
    }

    public class HomeCuisineInfo {

        @SerializedName("class_name")
        @Expose
        private String homeCuisClassName;

        @SerializedName("currently_select")
        @Expose
        private Boolean homeCuisCurrentSelect;

        @SerializedName("entity_id")
        @Expose
        private int homeCuisEntityId;

        @SerializedName("name")
        @Expose
        private String homeCuisName;

        public String getHomeCuisClassName() {
            return homeCuisClassName;
        }

        public void setHomeCuisClassName(String homeCuisClassName) {
            this.homeCuisClassName = homeCuisClassName;
        }

        public Boolean getHomeCuisCurrentSelect() {
            return homeCuisCurrentSelect;
        }

        public void setHomeCuisCurrentSelect(Boolean homeCuisCurrentSelect) {
            this.homeCuisCurrentSelect = homeCuisCurrentSelect;
        }

        public int getHomeCuisEntityId() {
            return homeCuisEntityId;
        }

        public void setHomeCuisEntityId(int homeCuisEntityId) {
            this.homeCuisEntityId = homeCuisEntityId;
        }

        public String getHomeCuisName() {
            return homeCuisName;
        }

        public void setHomeCuisName(String homeCuisName) {
            this.homeCuisName = homeCuisName;
        }
    }

    public class FoodChoicesInfo {

        @SerializedName("currently_select")
        @Expose
        private Boolean foodChoiceCurrSel;

        @SerializedName("name")
        @Expose
        private String foodChoiceName;

        @SerializedName("value")
        @Expose
        private int foodChoiceValue;

        public Boolean getFoodChoiceCurrSel() {
            return foodChoiceCurrSel;
        }

        public void setFoodChoiceCurrSel(Boolean foodChoiceCurrSel) {
            this.foodChoiceCurrSel = foodChoiceCurrSel;
        }

        public String getFoodChoiceName() {
            return foodChoiceName;
        }

        public void setFoodChoiceName(String foodChoiceName) {
            this.foodChoiceName = foodChoiceName;
        }

        public int getFoodChoiceValue() {
            return foodChoiceValue;
        }

        public void setFoodChoiceValue(int foodChoiceValue) {
            this.foodChoiceValue = foodChoiceValue;
        }

    }
}
