package version1.dishq.dishq.server.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by dishq on 09-01-2017.
 * Package name version1.dishq.dishq.
 */

public class DeliveryMenuResponse {

    @SerializedName("data")
    @Expose
    public DeliveryMenuInfo deliveryMenuInfo;

    @SerializedName("response")
    @Expose
    public String deliveryMenuResponse;

    public class DeliveryMenuInfo {

        @SerializedName("menu_data")
        @Expose
        public ArrayList<DeliveryMenuData> deliveryMenuDatas;

        @SerializedName("restaurant_data")
        @Expose
        public DeliveryRestData deliveryRestData;
    }

    public class DeliveryMenuData {

        @SerializedName("photo_popup")
        @Expose
        private ArrayList<String> delMenuPhotoPopup;

        @SerializedName("has_alcohol")
        @Expose
        private Boolean delMenuHasAlcohol;

        @SerializedName("is_veg")
        @Expose
        private Boolean delMenuIsVeg;

        @SerializedName("tags")
        @Expose
        private ArrayList<String> delMenuTags;

        @SerializedName("photo")
        @Expose
        public ArrayList<String> delMenuPhoto;

        @SerializedName("price")
        @Expose
        private String delMenuDishPrice;

        @SerializedName("generic_dish_id")
        @Expose
        private int delMenuGenericDishId;

        @SerializedName("dish_nature")
        @Expose
        private int delMenuDishNature;

        @SerializedName("has_egg")
        @Expose
        private Boolean delMenuHasEgg;

        @SerializedName("dish_name")
        @Expose
        private String delMenuDishName;

        @SerializedName("is_spicy")
        @Expose
        private Boolean delMenuIsSpicy;

        @SerializedName("cuisine_text")
        @Expose
        private ArrayList<String> delMenuCuisineText;

        @SerializedName("dish_type_text")
        @Expose
        private String delMenuDishType;

        public ArrayList<String> getDelMenuPhotoPopup() {
            return delMenuPhotoPopup;
        }

        public void setDelMenuPhotoPopup(ArrayList<String> delMenuPhotoPopup) {
            this.delMenuPhotoPopup = delMenuPhotoPopup;
        }

        public Boolean getDelMenuHasAlcohol() {
            return delMenuHasAlcohol;
        }

        public void setDelMenuHasAlcohol(Boolean delMenuHasAlcohol) {
            this.delMenuHasAlcohol = delMenuHasAlcohol;
        }

        public Boolean getDelMenuIsVeg() {
            return delMenuIsVeg;
        }

        public void setDelMenuIsVeg(Boolean delMenuIsVeg) {
            this.delMenuIsVeg = delMenuIsVeg;
        }

        public ArrayList<String> getDelMenuTags() {
            return delMenuTags;
        }

        public void setDelMenuTags(ArrayList<String> delMenuTags) {
            this.delMenuTags = delMenuTags;
        }

        public ArrayList<String> getDelMenuPhoto() {
            return delMenuPhoto;
        }

        public void setDelMenuPhoto(ArrayList<String> delMenuPhoto) {
            this.delMenuPhoto = delMenuPhoto;
        }

        public String getDelMenuDishPrice() {
            return delMenuDishPrice;
        }

        public void setDelMenuDishPrice(String delMenuDishPrice) {
            this.delMenuDishPrice = delMenuDishPrice;
        }

        public int getDelMenuGenericDishId() {
            return delMenuGenericDishId;
        }

        public void setDelMenuGenericDishId(int delMenuGenericDishId) {
            this.delMenuGenericDishId = delMenuGenericDishId;
        }

        public int getDelMenuDishNature() {
            return delMenuDishNature;
        }

        public void setDelMenuDishNature(int delMenuDishNature) {
            this.delMenuDishNature = delMenuDishNature;
        }

        public Boolean getDelMenuHasEgg() {
            return delMenuHasEgg;
        }

        public void setDelMenuHasEgg(Boolean delMenuHasEgg) {
            this.delMenuHasEgg = delMenuHasEgg;
        }

        public String getDelMenuDishName() {
            return delMenuDishName;
        }

        public void setDelMenuDishName(String delMenuDishName) {
            this.delMenuDishName = delMenuDishName;
        }

        public Boolean getDelMenuIsSpicy() {
            return delMenuIsSpicy;
        }

        public void setDelMenuIsSpicy(Boolean delMenuIsSpicy) {
            this.delMenuIsSpicy = delMenuIsSpicy;
        }

        public ArrayList<String> getDelMenuCuisineText() {
            return delMenuCuisineText;
        }

        public void setDelMenuCuisineText(ArrayList<String> delMenuCuisineText) {
            this.delMenuCuisineText = delMenuCuisineText;
        }

        public String getDelMenuDishType() {
            return delMenuDishType;
        }

        public void setDelMenuDishType(String delMenuDishType) {
            this.delMenuDishType = delMenuDishType;
        }
    }

    public class DeliveryRestData {

        @SerializedName("restaurant_lat_lon")
        @Expose
        private String delMenuRestLatLong;

        @SerializedName("drive_time")
        @Expose
        private String delMenuDriveTime;

        @SerializedName("price_level")
        @Expose
        private int delMenuPriceLvl;

        @SerializedName("restaurant_id")
        @Expose
        private int delMenuRestId;

        @SerializedName("food_tags")
        @Expose
        private ArrayList<String> delMenuTags;

        @SerializedName("restaurant_name")
        @Expose
        private String delMenuRestName;

        @SerializedName("cuisine_text")
        @Expose
        private ArrayList<String> delMenuCusineText;

        @SerializedName("restaurant_address")
        @Expose
        private ArrayList<String> delMenuRestAddr;

        @SerializedName("swiggy_delivery_url")
        @Expose
        private ArrayList<String> delMenuSwiggyUrl;

        @SerializedName("zomato_delivery_url")
        @Expose
        private ArrayList<String> delMenuZomatoUrl;

        @SerializedName("runnr_delivery_url")
        @Expose
        private ArrayList<String> delMenuRunnrUrl;

        @SerializedName("foodpanda_delivery_url")
        @Expose
        private ArrayList<String> delMenuFoodPandaUrl;

        public String getDelMenuRestLatLong() {
            return delMenuRestLatLong;
        }

        public void setDelMenuRestLatLong(String delMenuRestLatLong) {
            this.delMenuRestLatLong = delMenuRestLatLong;
        }

        public String getDelMenuDriveTime() {
            return delMenuDriveTime;
        }

        public void setDelMenuDriveTime(String delMenuDriveTime) {
            this.delMenuDriveTime = delMenuDriveTime;
        }

        public int getDelMenuPriceLvl() {
            return delMenuPriceLvl;
        }

        public void setDelMenuPriceLvl(int delMenuPriceLvl) {
            this.delMenuPriceLvl = delMenuPriceLvl;
        }

        public int getDelMenuRestId() {
            return delMenuRestId;
        }

        public void setDelMenuRestId(int delMenuRestId) {
            this.delMenuRestId = delMenuRestId;
        }

        public ArrayList<String> getDelMenuTags() {
            return delMenuTags;
        }

        public void setDelMenuTags(ArrayList<String> delMenuTags) {
            this.delMenuTags = delMenuTags;
        }

        public String getDelMenuRestName() {
            return delMenuRestName;
        }

        public void setDelMenuRestName(String delMenuRestName) {
            this.delMenuRestName = delMenuRestName;
        }

        public ArrayList<String> getDelMenuCusineText() {
            return delMenuCusineText;
        }

        public void setDelMenuCusineText(ArrayList<String> delMenuCusineText) {
            this.delMenuCusineText = delMenuCusineText;
        }

        public ArrayList<String> getDelMenuRestAddr() {
            return delMenuRestAddr;
        }

        public void setDelMenuRestAddr(ArrayList<String> delMenuRestAddr) {
            this.delMenuRestAddr = delMenuRestAddr;
        }

        public ArrayList<String> getDelMenuSwiggyUrl() {
            return delMenuSwiggyUrl;
        }

        public void setDelMenuSwiggyUrl(ArrayList<String> delMenuSwiggyUrl) {
            this.delMenuSwiggyUrl = delMenuSwiggyUrl;
        }

        public ArrayList<String> getDelMenuZomatoUrl() {
            return delMenuZomatoUrl;
        }

        public void setDelMenuZomatoUrl(ArrayList<String> delMenuZomatoUrl) {
            this.delMenuZomatoUrl = delMenuZomatoUrl;
        }

        public ArrayList<String> getDelMenuRunnrUrl() {
            return delMenuRunnrUrl;
        }

        public void setDelMenuRunnrUrl(ArrayList<String> delMenuRunnrUrl) {
            this.delMenuRunnrUrl = delMenuRunnrUrl;
        }

        public ArrayList<String> getDelMenuFoodPandaUrl() {
            return delMenuFoodPandaUrl;
        }

        public void setDelMenuFoodPandaUrl(ArrayList<String> delMenuFoodPandaUrl) {
            this.delMenuFoodPandaUrl = delMenuFoodPandaUrl;
        }
    }
}
