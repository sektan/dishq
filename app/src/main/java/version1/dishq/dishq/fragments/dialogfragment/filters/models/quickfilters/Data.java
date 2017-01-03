package version1.dishq.dishq.fragments.dialogfragment.filters.models.quickfilters;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by kavin.prabhu on 31/12/16.
 */

public class Data {

    @SerializedName("quick_filters")
    @Expose
    private List<QuickFilter> quickFilters = null;
    @SerializedName("food_mood_filters")
    @Expose
    private List<FoodMoodFilter> foodMoodFilters = null;

    public List<QuickFilter> getQuickFilters() {
        return quickFilters;
    }

    public void setQuickFilters(List<QuickFilter> quickFilters) {
        this.quickFilters = quickFilters;
    }

    public List<FoodMoodFilter> getFoodMoodFilters() {
        return foodMoodFilters;
    }

    public void setFoodMoodFilters(List<FoodMoodFilter> foodMoodFilters) {
        this.foodMoodFilters = foodMoodFilters;
    }
}
