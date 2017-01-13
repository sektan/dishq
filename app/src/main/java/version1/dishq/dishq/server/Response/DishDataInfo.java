package version1.dishq.dishq.server.Response;

import android.os.Bundle;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by dishq on 27-12-2016.
 * Package name version1.dishq.dishq.
 */

public class DishDataInfo {

    public static final String  GENERIC_DISH_ID = "genericDishId";
    public static final String  DISH_NAME_KEY = "dishNameKey";
    public static final String  DISH_HAS_ALCOHOL = "dishHasAlcohol";
    public static final String  DISH_IS_VEG = "dishIsVeg";
    public static final String  DISH_TAGS = "dishTags";
    public static final String  DISH_PHOTO = "dishPhoto";
    public static final String  DISH_SMALL_PIC = "dishSmallPic";
    public static final String  DISH_ADDED_TO_FAV = "dishAddedToFav";
    public static final String  DISH_HAS_EGG = "dishHasEgg";
    public static final String  DISH_RECIPE_URL = "dishRecipeUrl";
    public static final String  DISH_NATURE = "dishNature";
    public static final String  DISH_IS_SPICY = "dishIsSpicy";
    public static final String  DISH_CUISINE_TEXT = "dishCuisineText";
    public static final String  DISH_TYPE_TEXT = "dishTypeText";

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

    @SerializedName("photo_small")
    @Expose
    private ArrayList<String> dishSmallPic;

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

    @SerializedName("dish_nature")
    @Expose
    private int homeDishNature;

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

    public ArrayList<String> getDishSmallPic() {
        return dishSmallPic;
    }

    public void setDishSmallPic(ArrayList<String> dishSmallPic) {
        this.dishSmallPic = dishSmallPic;
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

    public int getHomeDishNature() {
        return homeDishNature;
    }

    public void setHomeDishNature(int homeDishNature) {
        this.homeDishNature = homeDishNature;
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

    public Bundle toBundle() {
        Bundle bundle = new Bundle();
        bundle.putInt(GENERIC_DISH_ID, this.getGenericDishId());
        bundle.putString(DISH_NAME_KEY, this.getDishName());
        bundle.putBoolean(DISH_HAS_ALCOHOL, this.getHasAlcohol());
        bundle.putBoolean(DISH_IS_VEG, this.getVeg());
        bundle.putBoolean(DISH_HAS_EGG, this.getHasEgg());
        bundle.putBoolean(DISH_IS_SPICY, this.getSpicy());
        bundle.putBoolean(DISH_ADDED_TO_FAV, this.getAddedToFav());
        bundle.putString(DISH_TYPE_TEXT, this.getDishTypeText());
        bundle.putString(DISH_RECIPE_URL, this.getRecipeUrl());
        bundle.putInt(DISH_NATURE, this.getHomeDishNature());
        bundle.putStringArrayList(DISH_TAGS, this.getTags());
        bundle.putStringArrayList(DISH_PHOTO, this.getDishPhoto());
        bundle.putStringArrayList(DISH_SMALL_PIC, this.getDishSmallPic());
        bundle.putStringArrayList(DISH_CUISINE_TEXT, this.getCuisineText());
        return bundle;
    }

    public DishDataInfo toModel (Bundle bundle) {

        DishDataInfo dishDataInfo = new DishDataInfo();
        dishDataInfo.setGenericDishId(bundle.getInt(GENERIC_DISH_ID));
        dishDataInfo.setDishName(bundle.getString(DISH_NAME_KEY));
        dishDataInfo.setDishTypeText(bundle.getString(DISH_TYPE_TEXT));
        dishDataInfo.setRecipeUrl(bundle.getString(DISH_RECIPE_URL));
        dishDataInfo.setHomeDishNature(bundle.getInt(DISH_NATURE));
        dishDataInfo.setHasAlcohol(bundle.getBoolean(DISH_HAS_ALCOHOL));
        dishDataInfo.setVeg(bundle.getBoolean(DISH_IS_VEG));
        dishDataInfo.setHasEgg(bundle.getBoolean(DISH_HAS_EGG));
        dishDataInfo.setSpicy(bundle.getBoolean(DISH_IS_SPICY));
        dishDataInfo.setAddedToFav(bundle.getBoolean(DISH_ADDED_TO_FAV));
        dishDataInfo.setTags(bundle.getStringArrayList(DISH_TAGS));
        dishDataInfo.setDishPhoto(bundle.getStringArrayList(DISH_PHOTO));
        dishDataInfo.setDishSmallPic(bundle.getStringArrayList(DISH_SMALL_PIC));
        dishDataInfo.setCuisineText(bundle.getStringArrayList(DISH_CUISINE_TEXT));
        return dishDataInfo;
    }
}
