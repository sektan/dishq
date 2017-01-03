package version1.dishq.dishq.fragments.dialogfragment.filters.models.quickfilters;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by kavin.prabhu on 31/12/16.
 */

public class FoodMoodFilter {

    @SerializedName("food_mood_id")
    @Expose
    private Integer foodMoodId;
    @SerializedName("name")
    @Expose
    private String name;

    public Integer getFoodMoodId() {
        return foodMoodId;
    }

    public void setFoodMoodId(Integer foodMoodId) {
        this.foodMoodId = foodMoodId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
