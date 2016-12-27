package version1.dishq.dishq.server.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by dishq on 27-12-2016.
 * Package name version1.dishq.dishq.
 */

public class DishDataInfo {

    @SerializedName("has_alcohol")
    @Expose
    private Boolean hasAlcohol;

    @SerializedName("is_veg")
    @Expose
    private Boolean isVeg;

    @SerializedName("tags")
    @Expose
    private ArrayList<String> tags;

    @SerializedName("photo")
    @Expose
    private ArrayList<String> dishPhoto;

    @SerializedName("generic_dish_id")
    @Expose
    private int genericDishId;

    @SerializedName("is_added_to_favourtie")
    @Expose
    private Boolean isAddedToFav;

    @SerializedName("has_egg")
    @Expose
    private Boolean hasEgg;

    @SerializedName("recipe_url")
    @Expose
    private String recipeUrl;

    @SerializedName("dish_name")
    @Expose
    private String dishName;

    @SerializedName("is_spicy")
    @Expose
    private Boolean isSpicy;

    @SerializedName("cuisine_text")
    @Expose
    private ArrayList<String> cuisineText;

    @SerializedName("dish_type_text")
    @Expose
    private String dishTypeText;

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
