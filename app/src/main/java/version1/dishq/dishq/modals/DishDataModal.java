package version1.dishq.dishq.modals;
import java.util.ArrayList;


/**
 * Created by dishq on 27-12-2016.
 * Package name version1.dishq.dishq.
 */

public class DishDataModal {

    private Boolean hasAlcohol;

    private Boolean isVeg;

    private ArrayList<String> tags;

    private ArrayList<String> dishPhoto;

    private int genericDishId;

    private Boolean isAddedToFav;

    private Boolean hasEgg;

    private String recipeUrl;

    private String dishName;

    private Boolean isSpicy;

    private ArrayList<String> cuisineText;

    private String dishTypeText;

    public DishDataModal(Boolean hasAlcohol, Boolean isVeg, ArrayList<String> tags,
                         ArrayList<String> dishPhoto, int genericDishId, Boolean isAddedToFav,
                         Boolean hasEgg, String recipeUrl, String dishName, Boolean isSpicy,
                         ArrayList<String> cuisineText, String dishTypeText) {
        this.hasAlcohol = hasAlcohol;
        this.isVeg = isVeg;
        this.tags = tags;
        this.dishPhoto = dishPhoto;
        this.genericDishId = genericDishId;
        this.isAddedToFav = isAddedToFav;
        this.hasEgg = hasEgg;
        this.recipeUrl = recipeUrl;
        this.dishName = dishName;
        this.isSpicy = isSpicy;
        this.cuisineText = cuisineText;
        this.dishTypeText = dishTypeText;
    }

    public Boolean getHasAlcohol() {
        return hasAlcohol;
    }

    public void setHasAlcohol(Boolean hasAlcohol) {
        this.hasAlcohol = hasAlcohol;
    }

    public Boolean getVeg() {
        return isVeg;
    }

    public void setVeg(Boolean veg) {
        isVeg = veg;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

    public ArrayList<String> getDishPhoto() {
        return dishPhoto;
    }

    public void setDishPhoto(ArrayList<String> dishPhoto) {
        this.dishPhoto = dishPhoto;
    }

    public int getGenericDishId() {
        return genericDishId;
    }

    public void setGenericDishId(int genericDishId) {
        this.genericDishId = genericDishId;
    }

    public Boolean getAddedToFav() {
        return isAddedToFav;
    }

    public void setAddedToFav(Boolean addedToFav) {
        isAddedToFav = addedToFav;
    }

    public Boolean getHasEgg() {
        return hasEgg;
    }

    public void setHasEgg(Boolean hasEgg) {
        this.hasEgg = hasEgg;
    }

    public String getRecipeUrl() {
        return recipeUrl;
    }

    public void setRecipeUrl(String recipeUrl) {
        this.recipeUrl = recipeUrl;
    }

    public String getDishName() {
        return dishName;
    }

    public void setDishName(String dishName) {
        this.dishName = dishName;
    }

    public Boolean getSpicy() {
        return isSpicy;
    }

    public void setSpicy(Boolean spicy) {
        isSpicy = spicy;
    }

    public ArrayList<String> getCuisineText() {
        return cuisineText;
    }

    public void setCuisineText(ArrayList<String> cuisineText) {
        this.cuisineText = cuisineText;
    }

    public String getDishTypeText() {
        return dishTypeText;
    }

    public void setDishTypeText(String dishTypeText) {
        this.dishTypeText = dishTypeText;
    }
}

