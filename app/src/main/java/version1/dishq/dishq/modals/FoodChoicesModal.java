package version1.dishq.dishq.modals;

/**
 * Created by dishq on 16-12-2016.
 * Package name version1.dishq.dishq.
 */

public class FoodChoicesModal {

    private Boolean foodChoiceCurrSel;
    private String foodChoiceName;
    private int foodChoiceValue;

    public FoodChoicesModal(Boolean foodChoiceCurrSel, String foodChoiceName, int foodChoiceValue) {
        this.foodChoiceCurrSel = foodChoiceCurrSel;
        this.foodChoiceName = foodChoiceName;
        this.foodChoiceValue = foodChoiceValue;
    }

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
